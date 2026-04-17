package org.leaf.api.command.special;

import org.leaf.api.command.Command;
import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

/// Class representing fire related ERLC Commands.
/// This encapsulates both {@code :startnearfire} and {@code :startfire}
public class FireCommand extends Command {
    private final String location;

    public FireCommand(String raw,
                       AbstractPlayer sender,
                        CommandName commandName,
                        String trueName,
                        Instant timestamp,
                        String location) {

        super(raw, trueName, commandName, sender, timestamp);
        this.location = location;
    }

    /// Get the location of the fire command. This can be "house", "brush", etc.
    public String getLocation() {
        return location;
    }
}
