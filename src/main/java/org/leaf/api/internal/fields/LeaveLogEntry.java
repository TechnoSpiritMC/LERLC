package org.leaf.api.internal.fields;

import org.jetbrains.annotations.Nullable;
import org.leaf.api.http.dto.v1.JoinLogDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.internal.AbstractPlayer;

import java.time.Duration;
import java.time.Instant;

public class LeaveLogEntry {
    private final Instant leftAt;
    private AbstractPlayer player = null;

    public LeaveLogEntry(JoinLogDTO dto) {
        leftAt = Instant.ofEpochSecond(dto.Timestamp());
        player = AbstractPlayer.from(dto.Player());
    }

    public LeaveLogEntry(NewApiDTO.v2JoinLogDTO dto) {
        leftAt = Instant.ofEpochSecond(dto.Timestamp());
        player = AbstractPlayer.from(dto.Player());
    }

    /// Get the {@link Instant} when the player left your server.
    public Instant getLeftAt() {
        return leftAt;
    }

    /// Get the {@link AbstractPlayer} who left your server.
    /// May return null if the cache hasn't been properly initialized yet.
    @Nullable
    public AbstractPlayer getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return player.toString() + " left at " + leftAt;
    }
    public boolean equals(Object obj) {
        return obj instanceof LeaveLogEntry && ((LeaveLogEntry) obj).player.equals(player);
    }
    public int hashCode() {
        return player.hashCode();
    }
}