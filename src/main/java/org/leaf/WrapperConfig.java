package org.leaf;

import java.time.Duration;
import org.leaf.api.command.Command;
import org.leaf.api.internal.listener.events.RaidEvent;

public class WrapperConfig {
    private int maxJoinLogsLength = 100;
    private int maxCommandLogsLength = 500;
    private boolean lockdownModeEnabled = true;
    private Duration offlineThreshold = Duration.ofMinutes(5);

    private int _maxJoinLogsLength = 100;
    private int _maxCommandLogsLength = 500;
    private boolean _lockdownModeEnabled = true;
    private Duration _offlineThreshold = Duration.ofMinutes(5);

    private final Runnable configUpdateProvider;

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
    /// Sets the current offline threshold. Players who left the game will still be remembered by the cache for this duration and forgotten unless they join back within this threshold.
    public WrapperConfig setOfflineThreshold(Duration offlineThreshold) {
        this._offlineThreshold = offlineThreshold;
        return this;
    }
    /// Enabled or disables the lockdown mode. The wrapper automatically enters lockdown mode if this setting is enabled and if a raid is detected in game.
    /// <i>For more information about the raid topic, please refer to {@link Command#getEvaluation()}</i>.
    /// <p>
    ///     While in lockdown mode, the following actions are automatically refused:
    ///     <ul>
    ///         <li>Running in game commands, except for those with required privileges, like those alerting staff in game about the raid (automatically sent on entry into lockdown mode), or counter raid commands.</li>
    ///     </ul>
    ///
    ///     Additionally, all raid ({@link Command#getEvaluation()} {@code  >= 10}) and suspected raid ({@link Command#getEvaluation()} {@code  >= 8}) commands raise an alert with the {@link RaidEvent} event.
    /// </p>
    ///
    public WrapperConfig setLockdownModeEnabled(boolean lockdownModeEnabled) {
        this._lockdownModeEnabled = lockdownModeEnabled;
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

    /// Get the current state of the lockdown enabled flag. More information about the lockdown mode in {@link WrapperConfig#setLockdownModeEnabled}.
    public boolean isLockdownModeEnabled() {
        return lockdownModeEnabled;
    }


    public void done() {
        maxJoinLogsLength    = _maxJoinLogsLength;
        maxCommandLogsLength = _maxCommandLogsLength;
        offlineThreshold     = _offlineThreshold;
        configUpdateProvider.run();
    }
}
