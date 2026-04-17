package org.leaf.api.command;

import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

public class SingleTargetCommand extends Command {

    private final AbstractPlayer target;

    /// Create a new instance of {@link SingleTargetCommand} explicitly providing all info about it.
    public SingleTargetCommand(String raw,
                               AbstractPlayer sender,
                               CommandName commandName,
                               String trueName,
                               Instant timestamp,
                               AbstractPlayer target)
    {

        super(raw, trueName, commandName, sender, timestamp);
        this.target = target;
    }

    /// Get the target of the command.
    public AbstractPlayer getTarget() {
        return target;
    }
}