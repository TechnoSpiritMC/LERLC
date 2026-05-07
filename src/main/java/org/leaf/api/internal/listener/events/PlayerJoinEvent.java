package org.leaf.api.internal.listener.events;

import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.fields.JoinLogEntry;

import java.time.Instant;

/// Event fired when a player joins the server.
/// These are automatically detected and triggered whenever a player joins your server. This class provides handy information about the join:
///
/// {@link AbstractPlayer} - The player that joined.
///
/// {@link Instant} - The time the player joined.
public class PlayerJoinEvent extends Event {
    private final AbstractPlayer player;
    private final Instant joinTime;

    public PlayerJoinEvent(AbstractPlayer player, Instant joinTime) {
        this.player = player;
        this.joinTime = joinTime;
    }

    public PlayerJoinEvent(JoinLogEntry joinLogEntry) {
        this(joinLogEntry.getPlayer(), joinLogEntry.getJoinedAt());
    }

    public AbstractPlayer getPlayer() {
        return player;
    }
    public Instant getJoinTime() {
        return joinTime;
    }
}
