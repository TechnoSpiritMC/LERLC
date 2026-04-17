package org.leaf.api.command;

import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

/// A generic command is a command that doesn't have any arguments. For example: {@code :tocar}
public class GenericCommand extends Command {

    /// Create a new instance of {@link GenericCommand}.
    public GenericCommand(String raw,
                          AbstractPlayer sender,
                          CommandName commandName,
                          String trueName,
                          Instant timestamp) {

        super(raw, trueName, commandName, sender, timestamp);
    }
}
