package org.leaf.api.internal;

import org.leaf.api.internal.listener.Listener;
import org.leaf.api.internal.listener.events.Event;
import org.leaf.utils.LERLCLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListenerStore {
    private static final List<Object> listeners = Collections.synchronizedList(new ArrayList<>());

    public static void register(Object listener) {
        if (listener instanceof Listener) {
            listeners.add(listener);
        } else {
            throw new IllegalArgumentException("Listener must be an instance of Listener.");
        }
    }

    static List<Object> getListeners() {
        return listeners;
    }

    public static void unregister(Object listener) {
        listeners.remove(listener);
    }

    static void clear() {
        listeners.clear();
    }

    static void handle(Event event) {
        for (Object listener: listeners) {
            try {
                ((Listener) listener).onEvent(event);
            } catch (Throwable t) {
                if (t instanceof Error) {
                    throw (Error) t;
                }

                LERLCLogger.getLogger().severe("Failed to handle event " + event.getClass().getSimpleName() + " for registered handler: " + listener.getClass().getSimpleName() + "!");
            }
        }
    }
}
