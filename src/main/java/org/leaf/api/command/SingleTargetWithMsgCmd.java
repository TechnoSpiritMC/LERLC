package org.leaf.api.command;

import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

public class SingleTargetWithMsgCmd extends Command {

    private final AbstractPlayer target;
    private final String message;

    public SingleTargetWithMsgCmd(String raw,
                                  AbstractPlayer sender,
                               CommandName commandName,
                               String trueName,
                               Instant timestamp,
                                  AbstractPlayer target,
                               String message) {

        super(raw, trueName, commandName, sender, timestamp);
        this.target = target;
        this.message = message;
    }

    /// Get the target of the command.
    public AbstractPlayer getTarget() {
        return target;
    }

    /// Get the message of the command.
    public String getMessage() {
        return message;
    }
}