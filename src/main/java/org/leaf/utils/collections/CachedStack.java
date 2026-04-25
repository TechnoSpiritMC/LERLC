package org.leaf.utils.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedStack<T> {
    private final Map<Long, T> map = new ConcurrentHashMap<>();
    final int maxSize;
    final long lifetimeMs;

    public CachedStack(int maxSize, long lifetimeMs) {
        this.maxSize = maxSize;
        this.lifetimeMs = lifetimeMs;
    }

    public void add(T t) {
        map.put(System.nanoTime(), t);
        lazyRefresh();
    }

    private void lazyRefresh() {
        if (map.size() >= maxSize) {
            while (map.size() >= maxSize) {
                long oldest = map.keySet().stream().min(Long::compare).get();
                remove(oldest);
            }
        }

        map.entrySet().removeIf(entry -> (System.nanoTime() - entry.getKey()) / 1000 < lifetimeMs);
    }

    public void remove(long key) {
        map.remove(key);
    }

    public boolean contains(T value) {
        return map.containsValue(value);
    }

    public Map<Long, T> getMap() {
        return map;
    }
}
