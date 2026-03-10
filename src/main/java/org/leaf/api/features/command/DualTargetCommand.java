package org.leaf.api.features.command;

import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public class DualTargetCommand extends Command {

    private final RobloxPlayer target1, target2;

    /// Create a new instance of {@link SingleTargetCommand} explicitly providing all info about it.
    public DualTargetCommand(String raw,
                             RobloxPlayer sender,
                             CommandName commandName,
                             String trueName,
                             Instant timestamp,
                             RobloxPlayer target1,
                             RobloxPlayer target2) {

        super(raw, trueName, commandName, sender, timestamp);
        this.target1 = target1;
        this.target2 = target2;
    }

    /// Get the first target. This corresponds to the target player location in a {@code :tp <target1> <target2>} tp command.
    public RobloxPlayer getFirstTarget() {
        return target1;
    }

    /// Get the second target. This corresponds to the player being teleported in a {@code :tp <target1> <target2>} tp command.
    public RobloxPlayer getSecondTarget() {
        return target2;
    }
}