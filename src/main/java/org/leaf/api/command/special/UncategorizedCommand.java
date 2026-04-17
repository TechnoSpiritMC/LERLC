package org.leaf.api.command.special;

import org.leaf.api.command.Command;
import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

public class UncategorizedCommand extends Command {
    public UncategorizedCommand(String rawCommand,
                                CommandName commandName,
                                String name,
                                AbstractPlayer sender,
                                Instant timestamp) {

        super(rawCommand, name, commandName, sender, timestamp);
    }
}
