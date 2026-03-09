package org.leaf.api.http.cache.fields;

import org.leaf.WrapperConfig;
import org.leaf.api.features.JoinLogEntry;
import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Stack;

public class PlayerData {
    private final List<RobloxPlayer> players;
    private final Stack<JoinLogEntry> joinLogs = new Stack<>();

    private final WrapperConfig config;

    
    public PlayerData(List<RobloxPlayer> players, List<JoinLogEntry> joinLogs, WrapperConfig config) {
        this.config = config;

        this.players = players;
        this.joinLogs.setSize(config.getMaxJoinLogsLength());
        this.joinLogs.addAll(joinLogs);

    }

    public void configUpdateProvider() {
        joinLogs.setSize(config.getMaxJoinLogsLength());
    }
    
    public void addOrUpdatePlayer(RobloxPlayer player) {
        if (players.stream().anyMatch(p -> p.getUserId() == player.getUserId())) {
            RobloxPlayer existingPlayer = getRealPlayerObject(player);
            if (existingPlayer != null) {
                existingPlayer.setLastSeen(Instant.now());
            }
        } else {
            players.add(player);
        }
    }
    
    public RobloxPlayer getRealPlayerObject(RobloxPlayer player) {
        return players.stream().filter(p -> p.getUserId() == player.getUserId()).findFirst().orElse(null);
    }

    public void refreshDisconnectedPlayers() {
        players.removeIf(player -> Duration.between(player.getLastSeen(), Instant.now()).toSeconds() > config.getOfflineThreshold().toSeconds());
    }

    /// Get your server's player list.
    public List<RobloxPlayer> getPlayers() {
        return players;
    }

    public List<JoinLogEntry> getJoinLogs() {
        return joinLogs;
    }
}
