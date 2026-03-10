package org.leaf.api.features.command.special;

import org.leaf.api.features.command.Command;
import org.leaf.api.features.command.CommandName;
import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

/// Class representing the time ERLC Command.
public class TimeCommand extends Command {
    private final int timeOfDay;

    public TimeCommand(String raw,
                        RobloxPlayer sender,
                        CommandName commandName,
                        String trueName,
                        Instant timestamp,
                        int timeOfDay) {

        super(raw, trueName, commandName, sender, timestamp);
        this.timeOfDay = timeOfDay;
    }

    /// Returns the new time of day set in the command, a while value from 0 to 24 representing the hours of a day in a 24-hour time system.
    public int getTimeOfDay() {
        return timeOfDay;
    }
}
