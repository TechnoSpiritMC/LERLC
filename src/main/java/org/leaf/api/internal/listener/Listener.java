package org.leaf.api.internal.listener;

import org.jetbrains.annotations.NotNull;
import org.leaf.api.internal.listener.events.CommandEvent;
import org.leaf.api.internal.listener.events.Event;
import org.leaf.api.internal.listener.events.PlayerJoinEvent;
import org.leaf.api.internal.listener.events.PlayerLeaveEvent;
import org.leaf.utils.ClassWalker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class Listener implements EventListener {

    /// Event triggering when a command is sent in-game.
    public void onCommand(CommandEvent event) {}

    /// Event triggering when a player joins the server.
    public void onPlayerJoin(PlayerJoinEvent event) {}

    /// Event triggering when a player leaves the server.
    public void onPlayerLeave(PlayerLeaveEvent event) {}

    /// Event encapsulating any event. Please note that overwriting a specific event method is recommended over this.
    public void onGenericEvent(Event event) {}

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final ConcurrentMap<Class<?>, MethodHandle> methods = new ConcurrentHashMap<>();
    private static final Set<Class<?>> unresolved;

    static {
        unresolved = ConcurrentHashMap.newKeySet();
        Collections.addAll(
                unresolved,
                Object.class, // Objects aren't events
                Event.class, // onEvent is final and would never be found
                Event.class // onGenericEvent has already been called
        );
    }

    @Override
    public final void onEvent(@NotNull Event event) {
        // System.out.println("Received event: " + event.getClass().getSimpleName() + " trying to run it...");
        onGenericEvent(event);

        for (Class<?> clazz : ClassWalker.range(event.getClass(), Event.class)) {
            if (unresolved.contains(clazz)) {
                continue;
            }
            MethodHandle mh = methods.computeIfAbsent(clazz, Listener::findMethod);
            if (mh == null) {
                unresolved.add(clazz);
                continue;
            }

            try {
                mh.invoke(this, event);

            } catch (Throwable throwable) {
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                }
                if (throwable instanceof Error) {
                    throw (Error) throwable;
                }
                throw new IllegalStateException(throwable);
            }
        }
    }

    private static MethodHandle findMethod(Class<?> clazz) {
        String name = clazz.getSimpleName();
        MethodType type = MethodType.methodType(Void.TYPE, clazz);

        try {
            name = "on" + name.substring(0, name.length() - "Event".length());
            return lookup.findVirtual(Listener.class, name, type);
        } catch (NoSuchMethodException | IllegalAccessException ignored) {}

        return null;
    }
}
