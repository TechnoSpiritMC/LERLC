package org.leaf.api.internal;

import org.leaf.WrapperConfig;
import org.leaf.api.http.dto.v1.PlayerDTO;
import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class PlayerData {
    private final List<PlayerDTO> players;
    private final Stack<JoinLogEntry> joinLogs = new Stack<>();

    private final WrapperConfig config;
    private final PlayerProvider playerProvider;

    
    public PlayerData(List<PlayerDTO> players, List<JoinLogEntry> joinLogs, WrapperConfig config, PlayerProvider playerProvider) {
        this.config = config;

        this.players = players;
        this.joinLogs.setSize(config.getMaxJoinLogsLength());
        this.joinLogs.addAll(joinLogs);
        this.playerProvider = playerProvider;

    }

    public void configUpdateProvider() {
        joinLogs.setSize(config.getMaxJoinLogsLength());
    }

    /// Get your server's player list.
    public List<RobloxPlayer> getPlayers() {
        List<RobloxPlayer> ps = new java.util.ArrayList<>(players.stream().map(playerProvider::getPlayer).toList());
        ps.removeIf(Objects::isNull);

        return List.copyOf(ps);
    }

    public List<JoinLogEntry> getJoinLogs() {
        return joinLogs;
    }
}
