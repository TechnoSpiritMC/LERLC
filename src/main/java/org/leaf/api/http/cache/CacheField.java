package org.leaf.api.http.cache;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class CacheField<T> {
    private T value;
    private Runnable hook;
    protected volatile Instant lastUpdate     = Instant.MIN;
    protected volatile AtomicBoolean updating = new AtomicBoolean(false);

    protected final Duration refreshInterval;

    public CacheField(T value, Duration refreshInterval) {
        this.value = value;
        this.refreshInterval = refreshInterval;
    }

    public CacheField<T> setHook(Runnable hook) {
        this.hook = hook;
        return this;
    }

    public CacheField<T> refresh() {
        if (updating.get()) return this;

        updating.set(true);
        hook.run();
        updating.set(false);

        lastUpdate = Instant.now();
        return this;
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
