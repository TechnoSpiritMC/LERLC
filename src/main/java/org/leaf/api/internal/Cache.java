package org.leaf.api.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.leaf.WrapperConfig;
import org.leaf.api.http.dto.v1.JoinLogDTO;
import org.leaf.api.http.dto.v1.PlayerDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.internal.fields.CommandData;
import org.leaf.api.internal.fields.CommandLogEntry;
import org.leaf.api.internal.fields.JoinLogEntry;
import org.leaf.api.internal.fields.LeaveLogEntry;
import org.leaf.api.internal.listener.events.CommandEvent;
import org.leaf.api.internal.listener.events.Event;
import org.leaf.api.internal.listener.events.PlayerJoinEvent;
import org.leaf.api.internal.listener.events.PlayerLeaveEvent;
import org.leaf.utils.collections.CachedStack;
import org.leaf.utils.LERLCLogger;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Cache {
    private volatile CacheField<PlayerData> playerData;
    private volatile CacheField<CommandData> commandData;
    private volatile CacheField<Server> server;

    private final CachedStack<CommandLogEntry> commandStack = new CachedStack<>(100, Duration.ofMinutes(15).toMillis());

    private final Context ctx;
    private final WrapperConfig config;

    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4);

    private void onConfigUpdate() {
        playerData.getValue().configUpdateProvider();
        commandData.getValue().configUpdateProvider();
    }

    public Cache(String key) {
        config = new WrapperConfig(this::onConfigUpdate);

        ctx = new Context(key);
        boolean connected = false;

        try {
            connected = ctx.testApiConnection();
        } catch (Exception e) {
            LERLCLogger.getLogger().severe("Failed to connect to the API. Is the API down?");
            e.printStackTrace();
        }

        if (!connected) {
            LERLCLogger.getLogger().severe("Failed to connect to the API. Is the API down?");
            throw new RuntimeException("Failed to connect to the API.");
        }

        ctx.testLatency();

        initialize();
        scheduler.scheduleAtFixedRate(this::refresh, 0, 2, java.util.concurrent.TimeUnit.SECONDS);
    }

    public WrapperConfig getConfig() {
        return config;
    }

    private void initialize() {
        playerData = new CacheField<>(
                new PlayerData(
                        new HashSet<>(), new ArrayList<>(), config
                ),

                Duration.ofSeconds(5).minus(ctx.getAverageLatency())
        );
        playerData.setHook(this::refreshPlayers);

        commandData = new CacheField<>(
                new CommandData(
                        new ArrayList<>(), config
                ),

                Duration.ofSeconds(5).minus(ctx.getAverageLatency())
        );
        commandData.setHook(this::refreshCommands);

        server = new CacheField<>(
                new Server(),
                Duration.ofSeconds(5).minus(ctx.getAverageLatency())
        );
        server.setHook(this::refreshV2);
    }

    private void refresh() {
        LERLCLogger.getLogger().info("Refreshing cache...");
        if (playerData.isExpired())  new Thread(() -> playerData.refresh()).start();
        if (commandData.isExpired()) new Thread(() -> commandData.refresh()).start();
        if (server.isExpired()) new Thread(() -> server.refresh()).start();
    }

    synchronized void refreshPlayers() {
        refreshPlayerList();
        refreshJoinLogs();
    }

    synchronized void refreshCommands() {
        LERLCLogger.getLogger().info("Refreshing command cache...");
        if (server.getValue() == null) return;
        if (server.getValue().getCommandLogs().isEmpty()) return;

        LERLCLogger.getLogger().info("Command cache size: " + server.getValue().getCommandLogs().size());

        List<CommandLogEntry> commands = server.getValue().getCommandLogs();

        for (var command: commands) {
            if (Instant.now().minusSeconds(command.timestamp.getEpochSecond()).getEpochSecond() >= 15) {
                // Command was sent more than 15 seconds ago, most likely irrelevant.
                continue;
            }

            if (!commandStack.contains(command)) {
                commandStack.add(command);

                // This means that the command is most likely new. If it isn't, well too bad, I guess...?

                ListenerStore.handle(new CommandEvent(command));
            }
        }
    }

    private void refreshV2() {
        LERLCLogger.getLogger().info("Refreshing cache (v2)...");
        Request req;

        try {
            req = new Request(ctx, ConnectionMethod.GET, QueryType.All);
            req.send();
            System.out.println(req.body);

            if (req.returnCode != 200) {
                LERLCLogger.getLogger().severe("Failed to refresh player data. Is the API down? Skipping... " + req.returnCode);
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();

        NewApiDTO dto;

        try {
            dto = mapper.readValue(req.body, NewApiDTO.class);
        } catch (JsonProcessingException e) {
            LERLCLogger.getLogger().severe("Failed to parse player data. Is the API down? Skipping... " + e.getMessage());
            return;
        }

        dto = NewApiDTO.from(dto);

        // System.out.println(dto);

        for (var player: dto.Players()) {
            PlayerProvider.addPlayer(new FullPlayer(player));
        }

        server.setValue(new Server(dto));

        refreshJoinLogs();
    }

    private void refreshPlayerList() {
        Request req;

        try {
            req = new Request(ctx, "/server/players", false, ConnectionMethod.GET);
            req.send();

            if (req.returnCode != 200) {
                LERLCLogger.getLogger().severe("Failed to refresh player data. Is the API down? Skipping... " + req.returnCode);
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();

        List<PlayerDTO> players = new ArrayList<>();

        try {
            players = mapper.readValue(
                    req.body,
                    new TypeReference<List<PlayerDTO>>() {}
            );
        } catch (JsonProcessingException e) {
            LERLCLogger.getLogger().severe("Failed to parse player data. Is the API down? Skipping... " + e.getMessage());
            return;
        }

        players.forEach(pDTO -> playerData.getValue().addPlayer(pDTO));
    }

    private void refreshJoinLogs() {
        Request req;

        try {
            req = new Request(ctx, "/server/joinlogs", false, ConnectionMethod.GET);
            req.send();

            if (req.returnCode != 200) {
                LERLCLogger.getLogger().severe("Failed to refresh player data. Is the API down? Skipping... " + req.returnCode);
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();

        List<JoinLogDTO> joins = new ArrayList<>();

        try {
            joins = mapper.readValue(
                    req.body,
                    new TypeReference<List<JoinLogDTO>>() {}
            );
        } catch (JsonProcessingException e) {
            LERLCLogger.getLogger().severe("Failed to parse player data. Is the API down? Skipping... " + e.getMessage());
            return;
        }

        joins.forEach(dto -> {
            LERLCLogger.getLogger().info("Received join log: " + dto.Player() + " (" + dto.Timestamp() + ")" + (dto.Join() ? " joined" : " left") + " (at) " + Instant.ofEpochSecond(dto.Timestamp()).toString());
            if (Instant.now().minusSeconds(dto.Timestamp()).getEpochSecond() >= 60) {
                // Outdated dto, skipped.
                LERLCLogger.getLogger().info("Join log is outdated.");
                return;
            }

            if (dto.Join()) {
                LERLCLogger.getLogger().info("Join log is for a player that is already in the player list.");
                var entry = new JoinLogEntry(dto);

                boolean added = playerData.getValue().addJoinLog(entry);

                if (added) {
                    ListenerStore.handle(new PlayerJoinEvent(entry));
                }
            } else {
                LERLCLogger.getLogger().info("Join log is for a player that is not in the player list.");
                var entry = new LeaveLogEntry(dto);

                boolean added = playerData.getValue().addLeaveLog(entry);

                if (added) {
                    ListenerStore.handle(new PlayerLeaveEvent(entry));
                }
            }
        });
    }


    public List<AbstractPlayer> getPlayers() {
        return List.copyOf(playerData.getValue().getPlayers());
    }
    public List<JoinLogEntry> getJoinLogs() {
        return List.copyOf(playerData.getValue().getJoinLogs());
    }
}
