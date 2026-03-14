package org.leaf.api.command.special;

import org.leaf.api.command.Command;
import org.leaf.api.internal.command.CommandName;
import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;

/// Class representing all timed ERLC Commands. This encapsulates {@code :pt} and {@code :prty} commands.
public class DurationCommand extends Command {
    private final Duration duration;

    public DurationCommand(String raw,
                           RobloxPlayer sender,
                           CommandName commandName,
                           String trueName,
                           Instant timestamp,
                           Duration duration) {

        super(raw, trueName, commandName, sender, timestamp);
        this.duration = duration;
    }

    /// Returns the duration provided in the command.
    public Duration getDuration() {
        return duration;
    }
}
