package org.leaf.api.internal.fields;

import org.jetbrains.annotations.Nullable;
import org.leaf.api.http.dto.v1.JoinLogDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.internal.AbstractPlayer;

import java.time.Duration;
import java.time.Instant;

public class JoinLogEntry {
    private final Instant joinedAt;
    private AbstractPlayer player = null;

    /// Create an instance of {@link JoinLogEntry}.
    /// <b>This constructor is deprecated! Please consider using {@link JoinLogEntry} using {@link JoinLogDTO} or {@link NewApiDTO.v2JoinLogDTO} instead!</b>
    @Deprecated
    public JoinLogEntry(Instant joinedAt, AbstractPlayer player) {
        this.joinedAt = joinedAt;
        this.player = player;
    }

    public JoinLogEntry(JoinLogDTO dto) {
        joinedAt = Instant.ofEpochSecond(dto.Timestamp());
        player = AbstractPlayer.from(dto.Player());
    }

    public JoinLogEntry(NewApiDTO.v2JoinLogDTO dto) {
        joinedAt = Instant.ofEpochSecond(dto.Timestamp());
        player = AbstractPlayer.from(dto.Player());
    }

    /// Get the {@link Instant} when the player joined your server.
    public Instant getJoinedAt() {
        return joinedAt;
    }

    /// Get the {@link AbstractPlayer} who joined your server.
    /// May return null if the cache hasn't been properly initialized yet.
    @Nullable
    public AbstractPlayer getPlayer() {
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