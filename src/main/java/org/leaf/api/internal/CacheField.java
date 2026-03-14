package org.leaf.api.internal;

import org.leaf.Main;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class CacheField<T> {
    private T value;
    private Runnable hook;
    volatile Instant lastUpdate     = Instant.MIN;
    final AtomicBoolean updating = new AtomicBoolean(false);

    Duration refreshInterval;

    public CacheField(T value, Duration refreshInterval) {
        this.value = value;
        if (refreshInterval.isNegative() || refreshInterval.isZero()) {
            Main.logger.warning("Refresh interval for " + this.getClass().getSimpleName() + " is invalid, defaulting to 1 second.");
            refreshInterval = Duration.ofSeconds(1);
        }
        else {
            this.refreshInterval = refreshInterval;
        }
    }

    public CacheField<T> setHook(Runnable hook) {
        this.hook = hook;
        return this;
    }

    public CacheField<T> refresh() {
        if (!updating.compareAndSet(false, true)) return this;

        try {
            if (hook != null) {
                hook.run();
            }
            lastUpdate = Instant.now();
            return this;
        } finally {
            updating.set(false);
        }
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isExpired() {
        return lastUpdate.plus(refreshInterval).isBefore(Instant.now());
    }
}
