package org.leaf.api.command;

import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;
import org.leaf.api.internal.command.CommandUtils;

import java.time.Instant;

public abstract class Command {

    private final String raw;
    private final String name;
    private final AbstractPlayer sender;
    private final Instant timestamp;

    private final CommandName commandName;

    /// Generic constructor for an ERLC Command.
    protected Command(String raw,
                      String name,
                      CommandName commandName,
                      AbstractPlayer sender,
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

    /// Returns an instance of {@link AbstractPlayer} representing the player who executed the command.
    public AbstractPlayer getSender() {
        return sender;
    }

    /// Returns the timestamp of when the command was executed.
    public Instant getTimestamp() {
        return timestamp;
    }


    public CommandName getCommandName() {
        return commandName;
    }

    /// This method parses commands. Command parsing consists in evaluating the potential damage a command can do to determine if a command should be considered as a raid or no.
    ///
    /// Evaluation in done by first attributing a number to every potentially harmful command. For example, a {@code :ban} command will be assigned a value of 8.
    /// <br>After that, the command gets parsed, and the number of targets is added to the evaluation.
    /// <br>Finally, if one of the targets is a mass selector (like {@code all}), the evaluation is increased by 5.
    ///
    /// <ul>
    /// <li> {@code :ban}: {@code 8}</li>
    /// <li> {@code :ban all}: {@code 8 + 5}</li>
    /// <li> {@code :ban john, jane}: {@code 8 + 2}</li>
    /// <li> {@code :ban john, all}: {@code 8 + 5 + 2}</li>
    /// <br>
    ///
    /// <li> {@code :kick}: {@code 7}</li>
    /// <li> {@code :kill}: {@code 6}</li>
    /// <li> {@code :down}: {@code 6}</li>
    /// <li> {@code :jail}: {@code 6}</li>
    /// <li> {@code :mod}: {@code 8}</li>
    /// <li> {@code :admin}: {@code 9}</li>
    /// <li> {@code :load}: {@code 4}</li>
    /// <li> {@code :refresh}: {@code 2}</li>
    /// </ul>
    public int getEvaluation() {
        return CommandUtils.evaluate(this);
    }
}