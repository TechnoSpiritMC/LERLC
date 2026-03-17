package org.leaf.api.internal;

import org.leaf.WrapperConfig;
import org.leaf.api.http.dto.v1.PlayerDTO;
import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

public class PlayerData {
    private final Set<PlayerDTO> players;
    private final Stack<JoinLogEntry> joinLogs = new Stack<>();

    private final WrapperConfig config;
    private final PlayerProvider playerProvider = Cache.playerProvider;

    
    public PlayerData(Set<PlayerDTO> players, List<JoinLogEntry> joinLogs, WrapperConfig config) {
        this.config = config;

        this.players = players;
        this.joinLogs.setSize(config.getMaxJoinLogsLength());
        this.joinLogs.addAll(joinLogs);

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


    void addJoinLog(JoinLogEntry player) {
        joinLogs.push(player);
    }

    void addPlayer(PlayerDTO player) {
        players.add(player);
    }

    public List<JoinLogEntry> getJoinLogs() {
        return joinLogs;
    }
}
