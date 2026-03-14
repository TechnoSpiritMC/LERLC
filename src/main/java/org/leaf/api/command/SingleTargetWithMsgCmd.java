package org.leaf.api.command;

import org.leaf.api.internal.command.CommandName;
import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public class SingleTargetWithMsgCmd extends Command {

    private final RobloxPlayer target;
    private final String message;

    public SingleTargetWithMsgCmd(String raw,
                               RobloxPlayer sender,
                               CommandName commandName,
                               String trueName,
                               Instant timestamp,
                               RobloxPlayer target,
                               String message) {

        super(raw, trueName, commandName, sender, timestamp);
        this.target = target;
        this.message = message;
    }

    /// Get the target of the command.
    public RobloxPlayer getTarget() {
        return target;
    }

    /// Get the message of the command.
    public String getMessage() {
        return message;
    }
}