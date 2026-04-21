package org.leaf.api.internal.listener.events;

import org.leaf.api.internal.fields.CommandLogEntry;

public class CommandEvent extends Event {
    public final CommandLogEntry command;

    public CommandEvent(CommandLogEntry command) {
        this.command = command;
    }

    public CommandLogEntry getCommand() {
        return command;
    }
}
