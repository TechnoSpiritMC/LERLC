package org.leaf.api.internal.command;

import org.leaf.api.command.Command;

import java.util.Arrays;

/// Represents the name of an ERLC command. Provided by all instances of {@link Command}.
public enum CommandName {
    /**
     * <b>Private Message</b>
     * <p>Sends a private message to one or more players.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :pm <player> <message>                              Sends a private message to the player.
     * :pm <player1>,<player2>,<player3>,... <message>     Sends a private message to all the players provided.
     * }
     * </pre>
     */
    Pm(new String[]{"pm"}),

    /**
     * <b>Fixcar</b>
     * <p>Fixes a car.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :fixcar
     * }</pre>
     */
    Fixcar(new String[]{"fixcar"}),

    /**
     * <b>Kill</b>
     * <p>Kills a specific player.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :kill <player> (reason)
     * :down <player> (reason)
     * }</pre>
     */
    Kill(new String[]{"kill", "down"}),

    /**
     * <b>Killlogs</b>
     * <p>Displays kill logs (client side only).
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :killlogs
     * :kl
     * }</pre>
     */
    Killlogs(new String[]{"killlogs", "kl"}),

    /**
     * <b>Hint</b>
     * <p>Displays a message at the top of the screen.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :h <message>
     * :hint <message>
     * }</pre>
     */
    Hint(new String[]{"hint", "h"}),

    /**
     * <b>Message</b>
     * <p>Displays a message in the middle of the screen.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :m <message>
     * :message <message>
     * }</pre>
     */
    Message(new String[]{"message", "m"}),

    /**
     * <b>Tp</b>
     * <p>Teleportation command.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :tp <player>                   Teleport yourself to a player
     * :tp <player> <target:player>   Teleport player to target
     * }</pre>
     */
    Tp(new String[]{"tp"}),

    /**
     * <b>Refresh</b>
     * <p>Refreshes a player.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :refresh <target:player> (reason)
     * }</pre>
     */
    Refresh(new String[]{"refresh"}),

    /**
     * <b>Bring</b>
     * <p>Teleportation command. Brings someone to your position.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :bring <target:player>
     * }</pre>
     */
    Bring(new String[]{"bring"}),

    /**
     * <b>Heal</b>
     * <p>Heals a player.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :heal <target:player>
     * }</pre>
     */
    Heal(new String[]{"heal"}),

    /**
     * <b>StartNearFire</b>
     * <p>Starts a fire near the player (client side only).
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :startnearfire <location:{house,building,tree,brush,...}>
     * :snf <location:{house,building,tree,brush,...}>
     * }</pre>
     */
    StartNearFire(new String[]{"startnearfire", "snf"}),

    /**
     * <b>StartFire</b>
     * <p>Starts a fire somewhere random on the map.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :startfire <location:{house,building,tree,brush,...}>
     * }</pre>
     */
    StartFire(new String[]{"startfire"}),

    /**
     * <b>PriorityTimer</b>
     * <p>Sets up a priority timer.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :prty <uint: duration in seconds>
     * }</pre>
     */
    PriorityTimer(new String[]{"prty"}),

    /**
     * <b>Wanted</b>
     * <p>Makes someone wanted.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :wanted <target:player> (reason)
     * }</pre>
     */
    Wanted(new String[]{"wanted"}),

    /**
     * <b>Time</b>
     * <p>Changes the time on the server.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :time <uint: time in hours [0..24]>
     * }</pre>
     */
    Time(new String[]{"time"}),

    /**
     * <b>StopFire</b>
     * <p>Stops any ongoing fires.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :stopfire
     * }</pre>
     */
    StopFire(new String[]{"stopfire"}),

    /**
     * <b>ToVehicle</b>
     * <p>Teleports the player to their vehicle (client side only).
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :tocar
     * :toatv
     * }</pre>
     */
    ToVehicle(new String[]{"tocar", "toatv"}),

    /**
     * <b>Bans</b>
     * <p>Displays a list of server bans.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :bans
     * }</pre>
     */
    Bans(new String[]{"bans"}),

    /**
     * <b>Commands</b>
     * <p>Displays the list of available commands.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :commands
     * :cmds
     * }</pre>
     */
    Commands(new String[]{"commands", "cmds"}),

    /**
     * <b>Logs</b>
     * <p>Displays the server command logs.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :logs
     * }</pre>
     */
    Logs(new String[]{"logs"}),

    /**
     * <b>Admins</b>
     * <p>Displays a list of all server administrators.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :admins
     * }</pre>
     */
    Admins(new String[]{"admins"}),

    /**
     * <b>To</b>
     * <p>Teleports the player to another player.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :to <target:player>
     * }</pre>
     */
    To(new String[]{"to"}),

    /**
     * <b>Mods</b>
     * <p>Displays a list of all server moderators.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :mods
     * }</pre>
     */
    Mods(new String[]{"mods"}),

    /**
     * <b>Jail</b>
     * <p>Sends someone to jail for 5 minutes.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :jail <target:player> (reason)
     * }</pre>
     */
    Jail(new String[]{"jail"}),

    /**
     * <b>PeaceTimer</b>
     * <p>Sets up a peace timer during which no PVP damage can be dealt.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :pt <uint: duration in seconds>
     * }</pre>
     */
    PeaceTimer(new String[]{"pt"}),

    /**
     * <b>Respawn</b>
     * <p>Respawns a player at one of the map spawns.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :load <target:player> (reason)
     * :respawn <target:player> (reason)
     * }</pre>
     */
    Respawn(new String[]{"respawn", "load"}),

    /**
     * <b>Kick</b>
     * <p>Kicks a player from the private server.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :kick <target:player> <reason>
     * }</pre>
     */
    Kick(new String[]{"kick"}),

    /**
     * <b>Weather</b>
     * <p>Changes the server weather.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :weather <string:{rain,thunderstorm,fog,clear}>
     * }</pre>
     */
    Weather(new String[]{"weather"}),

    /**
     * <b>Unmod</b>
     * <p>Revokes moderator permissions from a player.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :unmod <player> (reason)
     * :unmod <uint: player id> (reason)
     * }</pre>
     */
    Unmod(new String[]{"unmod"}),

    /**
     * <b>Ban</b>
     * <p>Bans a player from the private server.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :ban <target:player> <reason>
     * }</pre>
     */
    Ban(new String[]{"ban"}),

    /**
     * <b>Admin</b>
     * <p>Grants administrator permissions to a player.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :admin <player>
     * :admin <uint: player id>
     * }</pre>
     */
    Admin(new String[]{"admin"}),

    /**
     * <b>Unadmin</b>
     * <p>Revokes administrator permissions from a player.
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :unadmin <player> (reason)
     * :unadmin <uint: player id> (reason)
     * }</pre>
     */
    Unadmin(new String[]{"unadmin"}),

    /**
     * <b>Custom Commands</b>
     * <p>Represents custom {@code :log} commands. These can then be parsed by the application to infer their usage.</p>
     *
     * <p><b>Usage:</b>
     * <pre>{@code
     * :log <args...>
     * }
     * </pre>
     *
     * <i>Please note that all args provided will be stored in a list, split at every whitespace character.</i>
     */
    Custom(new String[]{"log"}),


    /**
     * <b>Uncategorized</b>
     * <p>Uncategorized command.
     * <p>
     * This is used to represent:
     * <ul>
     *     <li>Commands that may have not been yet implemented in the Wrapper.</li>
     *     <li>Potential ERLC Staff commands, unknown to the public.</li>
     *</ul>
     */
    Uncategorized;

    private final String[] names;

    CommandName(String[] names) {
        this.names = names;
    }
    CommandName() {
        this.names = null;
    }

    /// Returns the names of this command. May be null if the command is uncategorized.
    public String[] getNames() {
        return names;
    }

    public static CommandName resolveCommandName(String name) {
        for (CommandName commandName : CommandName.values()) {
            if (commandName.getNames() != null && Arrays.asList(commandName.getNames()).contains(name)) {
                return commandName;
            }
        }
        return Uncategorized;
    }
}