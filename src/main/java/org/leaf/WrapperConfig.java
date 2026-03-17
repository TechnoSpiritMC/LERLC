package org.leaf;

import java.time.Duration;

public class WrapperConfig {
    private int maxJoinLogsLength = 100;
    private int maxCommandLogsLength = 500;
    private Duration offlineThreshold = Duration.ofMinutes(5);

    private int _maxJoinLogsLength = 100;
    private int _maxCommandLogsLength = 500;
    private Duration _offlineThreshold = Duration.ofMinutes(5);
    private Runnable configUpdateProvider;

    public WrapperConfig(Runnable configUpdateProvider) {
        this.configUpdateProvider = configUpdateProvider;
    }

    /// Sets the max join log entries cached.
    public WrapperConfig setMaxJoinLogsLength(int length) {
        _maxJoinLogsLength = length;
        return this;
    }
    /// Sets the max command log entries cached.
    public WrapperConfig setMaxCommandLogsLength(int length) {
        _maxCommandLogsLength = length;
        return this;
    }

    /// Gets the max join log entries cached.
    public int getMaxJoinLogsLength() {
        return maxJoinLogsLength;
    }
    /// Gets the max command log entries cached.
    public int getMaxCommandLogsLength() {
        return maxCommandLogsLength;
    }

    /// Gets the current offline threshold. Players who left the game will still be remembered by the cache for this duration and forgotten unless they join back within this threshold.
    public Duration getOfflineThreshold() {
        return offlineThreshold;
    }
    /// Sets the current offline threshold. Players who left the game will still be remembered by the cache for this duration and forgotten unless they join back within this threshold.
    public WrapperConfig setOfflineThreshold(Duration offlineThreshold) {
        this._offlineThreshold = offlineThreshold;
        return this;
    }


    public void done() {
        maxJoinLogsLength    = _maxJoinLogsLength;
        maxCommandLogsLength = _maxCommandLogsLength;
        offlineThreshold     = _offlineThreshold;
        configUpdateProvider.run();
    }
}
