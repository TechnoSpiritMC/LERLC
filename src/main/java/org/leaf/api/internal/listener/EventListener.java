package org.leaf.api.internal.listener;

import org.jetbrains.annotations.NotNull;
import org.leaf.api.internal.listener.events.Event;

@FunctionalInterface
public interface EventListener {
    void onEvent(@NotNull Event event);
}
