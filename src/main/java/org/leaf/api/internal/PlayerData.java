package org.leaf.api.internal;

import org.leaf.WrapperConfig;
import org.leaf.api.http.dto.v1.PlayerDTO;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.leaf.utils.Stack;

public class PlayerData {
    private final Set<PlayerDTO> players;
    private final Stack<JoinLogEntry> joinLogs = new Stack<>();

    private final WrapperConfig config;

    
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
    public List<AbstractPlayer> getPlayers() {
        return players.stream().map(AbstractPlayer::from).toList();
    }


    void addJoinLog(JoinLogEntry player) {
        if (joinLogsContainsPlayer(Objects.requireNonNull(player.getPlayer()))) return;
        joinLogs.push(player);
    }

    void addPlayer(PlayerDTO player) {
        players.add(player);
    }

    JoinLogEntry getNewestJoinLog() {
        return joinLogs.peek();
    }

    /// This removes a player from the join logs. This should be called only when the said player leaves.
    void removeJoinLog(AbstractPlayer player) {
        joinLogs.get().removeIf(p -> Objects.equals(p.getPlayer(), player));
    }

    boolean joinLogsContainsPlayer(AbstractPlayer p) {
        return joinLogs.stream().map(JoinLogEntry::getPlayer).anyMatch(p::equals);
    }

    public List<JoinLogEntry> getJoinLogs() {
        System.out.println(joinLogs);

        List<JoinLogEntry> logs = new java.util.ArrayList<>(joinLogs.stream().toList());
        logs.removeIf(Objects::isNull);

        return List.copyOf(logs);
    }
}
