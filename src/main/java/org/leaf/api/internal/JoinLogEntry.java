package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.JoinLogDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;

public class JoinLogEntry {
    private final Instant joinedAt;
    private final RobloxPlayer player;

    /// Create an instance of {@link JoinLogEntry}.
    /// <b>This constructor is deprecated! Please consider using {@link JoinLogEntry} using {@link JoinLogDTO} or {@link NewApiDTO.v2JoinLogDTO} instead!</b>
    @Deprecated
    public JoinLogEntry(Instant joinedAt, RobloxPlayer player) {
        this.joinedAt = joinedAt;
        this.player = player;
    }

    public JoinLogEntry(JoinLogDTO dto) {
        joinedAt = Instant.ofEpochSecond(dto.Timestamp());
        player = RobloxPlayer.parse(dto.Player());
    }

    public JoinLogEntry(NewApiDTO.v2JoinLogDTO dto) {
        joinedAt = Instant.ofEpochSecond(dto.Timestamp());
        player = RobloxPlayer.parse(dto.Player());
    }

    /// Get the {@link Instant} when the player joined your server.
    public Instant getJoinedAt() {
        return joinedAt;
    }

    /// Get the {@link RobloxPlayer} who joined your server.
    public RobloxPlayer getPlayer() {
        return player;
    }

    /// Get the playtime of the player during their current session.
    public Duration getInstancePlaytime() {
        return Duration.between(joinedAt, Instant.now());
    }

    @Override
    public String toString() {
        return player.toString() + " joined at " + joinedAt;
    }
    public boolean equals(Object obj) {
        return obj instanceof JoinLogEntry && ((JoinLogEntry) obj).player.equals(player);
    }
    public int hashCode() {
        return player.hashCode();
    }
}