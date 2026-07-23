package org.leaf.api.internal.listener.events;

import org.leaf.api.command.Command;
import org.leaf.api.internal.fields.CommandLogEntry;

/// Event representing a Command Raid event. This gets triggered whenever a command's {@link Command#getEvaluation()} is greater or equal to 10, which is the intended raid threshold.
public class RaidEvent extends Event {
    public final CommandLogEntry command;
    private RaidActionStatus status;

    public RaidEvent(CommandLogEntry command) {
        this.command = command;
        this.status = RaidActionStatus.UNHANDLED;
    }

    /// Get the problematic raid command.
    public CommandLogEntry getCommand() {
        return command;
    }

    /// Shortcut to get the command evaluation. More about command evaluation here: {@link Command#getEvaluation()}.
    public int getEvaluation() {
        return command.command.getEvaluation();
    }

    /// Ban the author of the problematic command.
    public boolean banSender() {
        return false;
    }
}
