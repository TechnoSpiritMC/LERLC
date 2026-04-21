package org.leaf.api.internal.listener.events;

import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.fields.JoinLogEntry;

import java.time.Instant;

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
