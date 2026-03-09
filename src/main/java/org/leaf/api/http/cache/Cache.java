package org.leaf.api.http.cache;

import java.util.List;

public class Cache {
    private volatile List<CacheField> cacheFields;
    private final Context ctx;

    public Cache(String key) {
        ctx = new Context(key);
        boolean connected = false;

        try {
            connected = ctx.testApiConnection();
        } catch (Exception _) {}

        if (!connected) {
            throw new RuntimeException("Failed to connect to the API.");
        }

        ctx.testLatency();
    }

    synchronized public void refreshPlayers() {

    }
}
