package org.leaf.api.internal.listener.events;

import java.time.Instant;

public abstract class Event {
    private final Instant timestamp;

    public Event() {
        this.timestamp = Instant.now();
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
