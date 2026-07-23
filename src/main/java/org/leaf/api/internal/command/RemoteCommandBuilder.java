package org.leaf.api.internal.command;

import org.leaf.api.command.Command;
import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.Cache;
import org.leaf.utils.DataCollector;

import java.time.Instant;

public class RemoteCommandBuilder {
    Command command;
    String rawCommand;
    String commandName;
    String content;

    /**
     * Create a new instance of {@link RemoteCommandBuilder} using an existing command. When using this constructor,
     * you still need to use {@link RemoteCommandBuilder#build()} to create the command. To avoid these redundant steps,
     * consider using {@link Command#run()} instead.
     *
     * @param command The command to use as a base.
     */
    public RemoteCommandBuilder(Command command) {
        this.rawCommand = command.getRawCommand();
        this.commandName = command.getName();
        this.content = command.getRawCommand().substring(commandName.length() + 1);
    }

    /**
     * Empty constructor for {@link RemoteCommandBuilder}. Use this constructor when you want to create a new command instance from scratch to send it.
     */
    public RemoteCommandBuilder() {}

    /**
     * Builds the command using the provided raw command directly. A raw command is the command executed by a server staff member. For example, {@code :kick pikpikdu35 Kicked for 2 hours for RDM.}.
     * @param rawCommand The raw command to use.
     */
    public RemoteCommandBuilder setRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
        return this;
    }

    /**
     * Sets the command name. This is the part of the command before the first space. For example, {@code ":kick"} in {@code :kick pikpikdu35 Kicked for 2 hours for RDM.}.
     * @param commandName The command name to use.
     */
    public RemoteCommandBuilder setCommand(String commandName) {
        this.commandName = commandName.strip();
        return this;
    }

    /**
     * Sets the command content. This is the part of the command that comes after the command name. For example, it would be {@code "pikpikdu35 Kicked for 2 hours for RDM."} in {@code :kick pikpikdu35 Kicked for 2 hours for RDM.}
     * @param content The command content to use.
     */
    public RemoteCommandBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Builds the command using the provided raw command directly.
     * <br><br>
     * Running this command will execute the raw command on the server, and can be done using either
     * {@link Command#run()} (At a lower level), or using {@link Cache#sendCommand(Command, DataCollector, boolean)}/{@link Cache#sendCommandBlocking(Command, DataCollector, boolean)} (At a higher level).
     * <br><br>
     * <i>Please note that using {@link Cache}'s methods is advised as they provide more control about asynchronous execution, and command execution priorities.
     * @return The built command.</i>
     */
    public Command build() {
        command = new Command(rawCommand,
                commandName,
                CommandName.resolveCommandName(commandName),
                AbstractPlayer.from("LERLC_RemoteServerMgmt:0"),
                Instant.now()) {
            @Override
            public String getRawCommand() {
                return rawCommand;
            }
        };

        return command;
    }
}
