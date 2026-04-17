package org.leaf.api.command;

import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

public class DualTargetCommand extends Command {

    private final AbstractPlayer target1, target2;

    /// Create a new instance of {@link SingleTargetCommand} explicitly providing all info about it.
    public DualTargetCommand(String raw,
                             AbstractPlayer sender,
                             CommandName commandName,
                             String trueName,
                             Instant timestamp,
                             AbstractPlayer target1,
                             AbstractPlayer target2) {

        super(raw, trueName, commandName, sender, timestamp);
        this.target1 = target1;
        this.target2 = target2;
    }

    /// Get the first target. This corresponds to the target player location in a {@code :tp <target1> <target2>} tp command.
    public AbstractPlayer getFirstTarget() {
        return target1;
    }

    /// Get the second target. This corresponds to the player being teleported in a {@code :tp <target1> <target2>} tp command.
    public AbstractPlayer getSecondTarget() {
        return target2;
    }
}