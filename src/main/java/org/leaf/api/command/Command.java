package org.leaf.api.command;

import org.leaf.api.internal.command.CommandName;
import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public abstract class Command {

    private final String raw;
    private final String name;
    private final RobloxPlayer sender;
    private final Instant timestamp;

    private final CommandName commandName;

    /// Generic constructor for an ERLC Command.
    Command(String raw,
                      String name,
                      CommandName commandName,
                      RobloxPlayer sender,
                      Instant timestamp) {

        this.raw = raw;
        this.name = name;
        this.sender = sender;
        this.timestamp = timestamp;
        this.commandName = commandName;
    }

    /// Getter for the raw command executed. This returns the command provided by the API. (i.e. {@code :kick john Kicked for 2 hours for RDM.})
    public String getRawCommand() {
        return raw;
    }

    /// Returns the parsed command name. The command name is the command executed without its arguments. For example, if the raw command is {@code :kick john Kicked for 2 hours for RDM.}, the command name would be {@code kick}.
    public String getName() {
        return name;
    }

    /// Returns an instance of {@link RobloxPlayer} representing the player who executed the command.
    /// Please note that their permission level will be deduced from the command they ran. (i.e., a ban command will make the sender appear as an {@link org.leaf.roblox.Permission#ADMINISTRATOR})
    public RobloxPlayer getSender() {
        return sender;
    }

    /// Returns the timestamp of when the command was executed.
    public Instant getTimestamp() {
        return timestamp;
    }


    public CommandName getCommandName() {
        return commandName;
    }
}