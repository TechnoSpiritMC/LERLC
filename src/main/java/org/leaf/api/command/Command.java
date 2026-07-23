package org.leaf.api.command;

import org.leaf.api.command.special.*;
import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.Cache;
import org.leaf.api.internal.command.CommandName;
import org.leaf.api.internal.command.CommandUtils;
import org.leaf.utils.DataCollector;
import org.leaf.utils.Unimplemented;

import java.time.Instant;

/// This is the <b>abstract</b> {@link Command} class,
/// which means that it may have additional information that can be accessed only by checking if it is an
/// instance of a specific command type. Below are all the special commands supported by this wrapper:
///
///
/// <table style="border-collapse: collapse;">
///     <tr>
///       <th style="border:1px solid gray;padding:4px;">Command Class</th>
///       <th style="border:1px solid gray;padding:4px;">What it encapsulates</th>
///     </tr>
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link CustomCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Custom}</td>
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link DurationCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#PriorityTimer}<br>{@link CommandName#PeaceTimer}</td>
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link FireCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#StartFire}<br>{@link CommandName#StartNearFire}</td>
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link MessageCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Message}<br>{@link CommandName#Hint}</td>
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link PmCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Pm}</td>
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link TimeCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Time}</td>
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link UncategorizedCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Uncategorized}</td>
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link WeatherCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Weather}</td>
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link GenericCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Fixcar}
///                                                         <br>{@link CommandName#Killlogs}
///                                                         <br>{@link CommandName#ToVehicle}
///                                                         <br>{@link CommandName#Bans}
///                                                         <br>{@link CommandName#Commands}
///                                                         <br>{@link CommandName#Logs}
///                                                         <br>{@link CommandName#Admins}
///                                                         <br>{@link CommandName#Mods}
///                                                         <br>{@link CommandName#StopFire}</td>
///
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link SingleTargetCommand}</td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Heal}
///                                                         <br>{@link CommandName#To}
///                                                         <br>{@link CommandName#Bring}
///                                                         <br>{@link CommandName#Admin}</td>
///
///     </tr>
///
///     <tr>
///       <td style="border:1px solid gray;padding:4px;">{@link SingleTargetCommand}<br>or<br>{@link SingleTargetWithMsgCmd}<br></td>
///       <td style="border:1px solid gray;padding:4px;">{@link CommandName#Kill}
///                                                         <br>{@link CommandName#Kick}
///                                                         <br>{@link CommandName#Ban}
///                                                         <br>{@link CommandName#Unadmin}
///                                                         <br>{@link CommandName#Unmod}
///                                                         <br>{@link CommandName#Refresh}
///                                                         <br>{@link CommandName#Wanted}
///                                                         <br>{@link CommandName#Jail}
///                                                         <br>{@link CommandName#Respawn}</td>
///
///     </tr>
///
///     </table>
///
/// <i><b>Please note that commands that is usually a {@link SingleTargetWithMsgCmd} can be a {@link SingleTargetCommand} if no reason for the command was provided in game.</b></i>
///
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

    /// Get the command name as an Enum entry of the {@link CommandName} Enum class.
    public CommandName getCommandName() {
        return commandName;
    }

    /// Run this particular command. Please note that the command executed is the one returned by {@link Command#getRawCommand()}.
    /// <br><br>
    /// <b>Please note that this is only to be used to run a command that has already been executed in game when already
    /// having its corresponding object. To run a custom command using this wrapper,
    /// please use <i>{@link Cache#sendCommand(Command, DataCollector, boolean)} or {@link Cache#sendCommandBlocking(Command, DataCollector, boolean)} instead.
    /// They provide more control about asynchronous execution, provide utility classes to monitor the execution process, and provide control about execution priority.
    /// </i></b>
    ///
    /// <p>
    ///     At a lower level, this class executed the provided command using {@link Cache#sendCommand(Command, DataCollector, boolean)}, with no dedicated {@link DataCollector}, and the priority flag being set to false.
    ///     This means that the only information you get from running this method is whether the command was successfully queued with a boolean. Any other information cannot be retrieved.
    ///     Additionally, the priority flag being set to false, a command cannot be executed using this method while a lockdown is active.
    /// </p>
    ///
    /// @return {@code true} if an issue arose while running the command. {@code false} otherwise. An exception might also be thrown if an API or internal exception occurs.
    public boolean run() {
        return Cache.instance.sendCommand(this, null, false);
    }

    /// This method parses commands. Command parsing consists in evaluating the potential damage a command can do to determine if a command should be considered as a raid or no.
    ///
    /// Evaluation in done by first attributing a number to every potentially harmful command. For example, a {@code :ban} command will be assigned a value of 8.
    /// <br>After that, the command gets parsed, and the number of targets is added to the evaluation.
    /// <br>Finally, if one of the targets is a mass selector (like {@code all}), the evaluation is increased by 5.
    /// <br>Parsing is to be used for flagging raid commands. Users were intended to trigger a raid warning when a command is parsed at 10+, and alert about a potential raid starting from a score of 8.
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