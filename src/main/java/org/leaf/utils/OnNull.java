package org.leaf.utils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class OnNull {

    private static final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

    public static <T> T onNull(T t, T def) {
        return t == null ? def : t;
    }

    public static <T> CompletableFuture<T> onNullAsync(
            Supplier<T> supplier,
            Supplier<T> retry,
            Duration wait,
            T fallback
    ) {
        T result = supplier.get();

        if (result != null) {
            return CompletableFuture.completedFuture(result);
        }

        CompletableFuture<T> future = new CompletableFuture<>();

        executor.schedule(() -> {
            try {
                T retryResult = retry.get();
                future.complete(retryResult != null ? retryResult : fallback);
            } catch (Exception e) {
                future.complete(fallback);
            }
        }, wait.toMillis(), TimeUnit.MILLISECONDS);

        return future;
    }
}
