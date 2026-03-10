package org.leaf.api.features.command;

import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public class SingleTargetCommand extends Command {

    private final RobloxPlayer target;

    /// Create a new instance of {@link SingleTargetCommand} explicitly providing all info about it.
    public SingleTargetCommand(String raw,
                      RobloxPlayer sender,
                      CommandName commandName,
                      String trueName,
                      Instant timestamp,
                      RobloxPlayer target) {

        super(raw, trueName, commandName, sender, timestamp);
        this.target = target;
    }

    /// Get the target of the command.
    public RobloxPlayer getTarget() {
        return target;
    }
}