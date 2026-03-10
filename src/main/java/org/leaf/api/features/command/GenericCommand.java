package org.leaf.api.features.command;

import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;

/// A generic command is a command that doesn't have any arguments. For example: {@code :tocar}
public class GenericCommand extends Command {

    /// Create a new instance of {@link GenericCommand}.
    public GenericCommand(String raw,
                          RobloxPlayer sender,
                          CommandName commandName,
                          String trueName,
                          Instant timestamp) {

        super(raw, trueName, commandName, sender, timestamp);
    }
}
