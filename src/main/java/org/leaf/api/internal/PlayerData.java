package org.leaf.api.internal;

import org.leaf.WrapperConfig;
import org.leaf.api.http.dto.v1.PlayerDTO;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.leaf.api.internal.fields.JoinLogEntry;
import org.leaf.api.internal.fields.LeaveLogEntry;
import org.leaf.utils.collections.StaticStack;

public class PlayerData {
    private final Set<PlayerDTO> players;
    private final StaticStack<JoinLogEntry> joinLogs = new StaticStack<>(15);
    private final StaticStack<LeaveLogEntry> leaveLogs = new StaticStack<>(15);

    private final WrapperConfig config;

    
    public PlayerData(Set<PlayerDTO> players, List<JoinLogEntry> joinLogs, WrapperConfig config) {
        this.config = config;

        this.players = players;
        this.joinLogs.setSize(config.getMaxJoinLogsLength());
        this.joinLogs.addAll(joinLogs);

        this.joinLogs.setSize(config.getMaxJoinLogsLength());

    }

    public void configUpdateProvider() {
        joinLogs.setSize(config.getMaxJoinLogsLength());
    }

    /// Get your server's player list.
    public List<AbstractPlayer> getPlayers() {
        return players.stream().map(AbstractPlayer::from).toList();
    }


    boolean addJoinLog(JoinLogEntry entry) {
        if (joinLogsContainsEntry(entry)) return false;
        joinLogs.push(entry);
        return true;
    }
    boolean addLeaveLog(LeaveLogEntry entry) {
        if (leaveLogsContainsEntry(entry)) return false;
        leaveLogs.push(entry);
        return true;
    }

    void addPlayer(PlayerDTO player) {
        players.add(player);
    }

    JoinLogEntry getNewestJoinLog() {
        return joinLogs.peek();
    }
    LeaveLogEntry getNewestLeaveLog() {
        return leaveLogs.peek();
    }

    /// This removes a player from the join logs. This should be called only when the said player leaves.
    void removeJoinLog(AbstractPlayer player) {
        joinLogs.get().removeIf(p -> Objects.equals(p.getPlayer(), player));
    }

    boolean joinLogsContainsEntry(JoinLogEntry entry) {
        return joinLogs.getAsList().stream()
                .anyMatch(e -> e.getJoinedAt().equals(entry.getJoinedAt()));
    }
    boolean leaveLogsContainsEntry(LeaveLogEntry entry) {
        return leaveLogs.getAsList().stream()
                .anyMatch(e -> e.getLeftAt().equals(entry.getLeftAt()));
    }

    public List<JoinLogEntry> getJoinLogs() {

        List<JoinLogEntry> logs = new java.util.ArrayList<>(joinLogs.stream().toList());
        logs.removeIf(Objects::isNull);

        return List.copyOf(logs);
    }
    public List<LeaveLogEntry> getLeaveLogs() {
        return List.copyOf(leaveLogs.stream().toList());
    }
}
