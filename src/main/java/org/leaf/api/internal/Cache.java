package org.leaf.api.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.attach.VirtualMachine;
import org.jetbrains.annotations.Nullable;
import org.leaf.WrapperConfig;
import org.leaf.api.command.Command;
import org.leaf.api.http.dto.v1.JoinLogDTO;
import org.leaf.api.http.dto.v1.PlayerDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.internal.command.RemoteCommandBuilder;
import org.leaf.api.internal.fields.CommandLogEntry;
import org.leaf.api.internal.fields.JoinLogEntry;
import org.leaf.api.internal.fields.LeaveLogEntry;
import org.leaf.api.internal.listener.events.CommandEvent;
import org.leaf.api.internal.listener.events.PlayerJoinEvent;
import org.leaf.api.internal.listener.events.PlayerLeaveEvent;
import org.leaf.api.internal.listener.events.RaidEvent;
import org.leaf.utils.DataCollector;
import org.leaf.utils.ObservableValue;
import org.leaf.utils.Pair;
import org.leaf.utils.collections.CachedStack;
import org.leaf.utils.LERLCLogger;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public class Cache {
    public static Cache instance = null;
    private static Cache privateInstance = null;

    private volatile CacheField<PlayerData> playerData;
    private volatile CacheField<Server> server;

    private final CachedStack<CommandLogEntry> commandStack = new CachedStack<>(100, Duration.ofMinutes(15).toMillis());

    final Context ctx;
    private final WrapperConfig config;

    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4);

    /// Flag representing the current state of the lockdown mode.
    final AtomicBoolean lockdown = new AtomicBoolean(false);
    final CommandExecutor commandExecutor = new CommandExecutor();

    private void onConfigUpdate() {
        playerData.getValue().configUpdateProvider();
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

        instance = this;
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

        server = new CacheField<>(
                new Server(),
                Duration.ofSeconds(5).minus(ctx.getAverageLatency())
        );
        server.setHook(() -> {
            this.refreshCommands();
            this.refreshV2();
        });
    }

    private void refresh() {

        if (instance != privateInstance) {
            throw new RuntimeException("Cache instance has been externally modified. The cache cannot continue to operate. Terminating...");
        }

        LERLCLogger.getLogger().info("Refreshing cache...");
        if (playerData.isExpired())  new Thread(() -> playerData.refresh()).start();
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
                if (command.command.getEvaluation() >= 10) {
                    ListenerStore.handle(new RaidEvent(command));
                }

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

        server.getValue().updateFromDTO(dto);

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
            if (Instant.now().minusSeconds(dto.Timestamp()).getEpochSecond() >= 60) {
                // Outdated dto, skipped.
                return;
            }

            if (dto.Join()) {
                var entry = new JoinLogEntry(dto);

                boolean added = playerData.getValue().addJoinLog(entry);

                if (added) {
                    ListenerStore.handle(new PlayerJoinEvent(entry));
                }
            } else {
                var entry = new LeaveLogEntry(dto);

                boolean added = playerData.getValue().addLeaveLog(entry);

                if (added) {
                    ListenerStore.handle(new PlayerLeaveEvent(entry));
                }
            }
        });
    }

    private Pair<Boolean, CommandExecutionProcess> runCommand(Command command, DataCollector<CommandExecutionProcess> collector, boolean priority) {
        ObservableValue<CommandExecutionProcess> process = new ObservableValue<>(new CommandExecutionProcess(command));
        if (collector != null) process.setOnChange(collector);

        if (!priority) {
            if (lockdown.get()) {
                LERLCLogger.getLogger().severe("Cannot send command while lockdown is enabled. Only commands with priority can be sent while lockdown is enabled.");
                return new Pair<>(false, process.get());
            }

            return new Pair<>(commandExecutor.submit(process.get()), process.get());
        }

        return new Pair<>(commandExecutor.submitPriority(process.get()), process.get());
    }

    /** Send a command to the game server as Remote Server Management. Please note that this is an asynchronous method that will nor block the execution of your other code after it.
     * Once a command is submitted, it will wait in the queue and get sent automatically.
     *
     * <p>
     *     This asynchronous method creates a new virtual {@link Thread} that will run the command.
     * </p>
     *
     * <p>
     *     Commands to be executed by this method are to be created using the {@link RemoteCommandBuilder} class, or by using already existing {@link Command} objects.<br><br>
     *     <i>Please note that {@link Command} objects already have a {@link Command#run()} method intended to run an already existing command. However, it is intended
     *     for internal use (But can be safely used externally), but it also provides less control about asynchronous execution, and command execution priority.
     *     {@link Command#run()} runs the command asynchronously, and without additional QoL features like a dedicated {@link CommandExecutionProcess} class or
     *     notifying the developer about the execution status.</i>
     * </p>
     *
     * @param collector Optional collector to collect data about the command execution. When the execution state changes, the collector will be notified with the new state, so some custom logic can be run.
     * @param priority Flag controlling whether the command should be considered as a priority command or no.
     *                 Priority commands are polled before regular command by the {@link CommandExecutor},
     *                 and are the only comands able to run during a lockdown. The queue size for priority
     *                 is also smaller than usual commands: 5 can be scheduled instead of 10 for usual commands.
     *                 These priority commands should only be used in case of a raid or in some other time-sensitive event.
     *
     * @return {@code true} if the command was successfully submitted. {@code false} otherwise.
     **/
    public boolean sendCommand(Command command, @Nullable DataCollector<CommandExecutionProcess> collector, boolean priority) {
        final AtomicBoolean success = new AtomicBoolean(false);
        new Thread(() -> {
            success.set(runCommand(command, collector, priority).first);
        });

        return success.get();
    }

    /** Send a command to the game server as Remote Server Management. Please note that this is a blocking method, which means that all code after it (or in the current thread) will be blocked and will not run until this method completes.
     * Once a command is submitted, it will wait in the queue and get sent automatically.
     * A command is considered as completed only when it is sent to the API.
     *
     * <p>
     *     Commands to be executed by this method are to be created using the {@link RemoteCommandBuilder} class, or by using already existing {@link Command} objects.<br><br>
     *     <i>Please note that {@link Command} objects already have a {@link Command#run()} method intended to run an already existing command. However, it is intended
     *     for internal use (But can be safely used externally), but it also provides less control about asynchronous execution, and command execution priority.
     *     {@link Command#run()} runs the command asynchronously, and without additional QoL features like a dedicated {@link CommandExecutionProcess} class or
     *     notifying the developer about the execution status.</i>
     * </p>
     *
     * @param collector Optional collector to collect data about the command execution. When the execution state changes, the collector will be notified with the new state, so some custom logic can be run.
     * @param priority Flag controlling whether the command should be considered as a priority command or no. Priority commands are polled before regular command by the {@link CommandExecutor}, and are the only comands able to run during a lockdown. The queue size for priority is also smaller than usual commands: 5 can be scheduled instead of 10 for usual commands. These priority commands should only be used in case of a raid or in some other time sensitive event.
     *
     * @return {@code true} if the command was successfully submitted. {@code false} otherwise.
    **/
    public boolean sendCommandBlocking(Command command, @Nullable DataCollector<CommandExecutionProcess> collector, boolean priority) {
        var statusProcessPair = runCommand(command, collector, priority);

        if (!statusProcessPair.first) {
            return false;
        }

        try {
            return statusProcessPair.second.awaitCompletion().isSuccessful();
        } catch (InterruptedException e) {
            LERLCLogger.getLogger().severe("Failed to send command...? " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            LERLCLogger.getLogger().severe("Failed to send command...? " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    /// Get your server's leave logs. These represent players leaving the server.
    public List<LeaveLogEntry> getLeaveLogs() {
        return List.copyOf(playerData.getValue().getLeaveLogs());
    }
    /// Returns the local {@link Server} object.
    ///
    /// {@link Server} is a wrapper class for holding the server's data. It contains all information about everything and should be used whenever you need data about the server.
    /// Please note that {@link Cache} refreshes the {@link Server} object every few seconds (Ideally every 5 seconds). Requesting its instance will provide you with an object that is subject to change.
    /// However, the object returned will still update, even after being requested.
    ///
    /// Fore information about the Server class here: {@link Server}.
    public Server getServer() {
        return server.getValue();
    }
}
