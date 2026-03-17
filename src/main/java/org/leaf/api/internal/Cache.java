package org.leaf.api.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.leaf.Main;
import org.leaf.WrapperConfig;
import org.leaf.api.http.dto.v1.JoinLogDTO;
import org.leaf.api.http.dto.v1.PlayerDTO;
import org.leaf.roblox.RobloxPlayer;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Cache {
    private volatile CacheField<PlayerData> playerData;
    private volatile CacheField<CommandData> commandData;

    private final Context ctx;
    private final WrapperConfig config;
    static final PlayerProvider playerProvider = new PlayerProvider();

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
            Main.logger.severe("Failed to connect to the API. Is the API down?");
            e.printStackTrace();
        }

        if (!connected) {
            Main.logger.severe("Failed to connect to the API. Is the API down?");
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
    }

    private void refresh() {
        if (playerData.isExpired())  new Thread(() -> playerData.refresh()).start();
        if (commandData.isExpired()) new Thread(() -> commandData.refresh()).start();
    }

    synchronized public void refreshPlayers() {
        refreshPlayerList();
        Main.logger.info("Waiting for player list to refresh to proceed further... Estimated time: " + ctx.getAverageLatency().toMillis() + "ms");
        while (playerData.getValue().getPlayers().isEmpty()) {;}
        Main.logger.info("Player list refreshed!");

        refreshJoinLogs();

    }

    synchronized public void refreshCommands() {
        //TODO: Add API Calls here!
    }


    private void refreshPlayerList() {
        Request req;

        try {
            req = new Request(ctx, "/server/players", false, ConnectionMethod.GET);
            req.send();

            if (req.returnCode != 200) {
                Main.logger.severe("Failed to refresh player data. Is the API down? Skipping... " + req.returnCode);
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
            Main.logger.severe("Failed to parse player data. Is the API down? Skipping... " + e.getMessage());
            return;
        }

        playerProvider.addAll(players);
        players.forEach(pDTO -> playerData.getValue().addPlayer(pDTO));
    }

    private void refreshJoinLogs() {
        Request req;

        try {
            req = new Request(ctx, "/server/joinlogs", false, ConnectionMethod.GET);
            req.send();

            if (req.returnCode != 200) {
                Main.logger.severe("Failed to refresh player data. Is the API down? Skipping... " + req.returnCode);
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
            Main.logger.severe("Failed to parse player data. Is the API down? Skipping... " + e.getMessage());
            return;
        }

        joins.forEach(dto -> playerData.getValue().getJoinLogs().add(new JoinLogEntry(dto)));
    }




    public List<RobloxPlayer> getPlayers() {
        return List.copyOf(playerData.getValue().getPlayers());
    }
    //TODO: Find out why this ain't working.
    public List<JoinLogEntry> getJoinLogs() {
        return List.copyOf(playerData.getValue().getJoinLogs());
    }
}
