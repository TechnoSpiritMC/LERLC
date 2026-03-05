package org.leaf.api.features;

import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;

public class JoinLogEntry {
    private final Instant joinedAt;
    private final RobloxPlayer player;

    /// Create an instance of {@link JoinLogEntry}.
    public JoinLogEntry(Instant joinedAt, RobloxPlayer player) {
        this.joinedAt = joinedAt;
        this.player = player;
    }

    /// Create an instance of {@link JoinLogEntry}, with the join time set to the given Unix timestamp in seconds.
    public JoinLogEntry(Long joinedAt, RobloxPlayer player) {
        this.joinedAt = Instant.ofEpochSecond(joinedAt);
        this.player = player;
    }

    /// Create an instance of {@link JoinLogEntry}. This automatically sets the join time at the current time.
    public JoinLogEntry(RobloxPlayer player) {
        this.joinedAt = Instant.now();
        this.player = player;
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