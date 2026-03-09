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
    public void setMaxJoinLogsLength(int length) {
        _maxJoinLogsLength = length;
    }
    /// Sets the max command log entries cached.
    public void setMaxCommandLogsLength(int length) {
        _maxCommandLogsLength = length;
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
    public void setOfflineThreshold(Duration offlineThreshold) {
        this._offlineThreshold = offlineThreshold;
    }


    public void done() {
        maxJoinLogsLength    = _maxJoinLogsLength;
        maxCommandLogsLength = _maxCommandLogsLength;
        offlineThreshold     = _offlineThreshold;
        configUpdateProvider.run();
    }
}
