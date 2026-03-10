package org.leaf.api.features.command.special;

import org.leaf.api.features.command.Command;
import org.leaf.api.features.command.CommandName;
import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public class UncategorizedCommand extends Command {
    public UncategorizedCommand(String rawCommand,
                                CommandName commandName,
                                String name,
                                RobloxPlayer sender,
                                Instant timestamp) {

        super(rawCommand, name, commandName, sender, timestamp);
    }
}
