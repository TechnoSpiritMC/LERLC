package org.leaf.api.internal;

import org.leaf.api.command.Command;
import org.leaf.api.command.GenericCommand;
import org.leaf.api.command.SingleTargetCommand;
import org.leaf.api.command.SingleTargetWithMsgCmd;
import org.leaf.api.internal.command.CommandFactory;
import org.leaf.api.http.dto.v1.CommandLogDTO;
import org.leaf.roblox.RobloxPlayer;

import org.leaf.api.command.special.*;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

public class CommandLogEntry {
    public Command command;
    public RobloxPlayer sender;
    public Instant timestamp;

    public CommandLogEntry(CommandLogDTO dto) {
        this.sender = RobloxPlayer.parse(dto.Player());
        this.timestamp = Instant.ofEpochSecond(dto.Timestamp());

        this.command = CommandFactory.parse(dto.Command(), sender, timestamp);
    }

    /// Get the command executed. Please note that this is an instance of the abstract {@link Command} class,
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
    public Command getCommand() {
        return command;
    }

    /// Get the player who executed the command.
    public RobloxPlayer getSender() {
        return sender;
    }

    /// Get the timestamp of when the command was executed.
    public Instant getTimestamp() {
        return timestamp;
    }
}
