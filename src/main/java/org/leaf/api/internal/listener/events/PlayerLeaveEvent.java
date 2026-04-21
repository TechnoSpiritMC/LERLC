package org.leaf.api.internal.listener.events;

import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.fields.JoinLogEntry;
import org.leaf.api.internal.fields.LeaveLogEntry;

import java.time.Instant;

public class PlayerLeaveEvent extends Event {
    private final AbstractPlayer player;
    private final Instant leaveTime;

    public PlayerLeaveEvent(AbstractPlayer player, Instant joinTime) {
        this.player = player;
        this.leaveTime = joinTime;
    }

        public PlayerLeaveEvent(LeaveLogEntry leaveLogEntry) {
        this(leaveLogEntry.getPlayer(), leaveLogEntry.getLeftAt());
    }

    public AbstractPlayer getPlayer() {
        return player;
    }
    public Instant getLeaveTime() {
        return leaveTime;
    }
}
