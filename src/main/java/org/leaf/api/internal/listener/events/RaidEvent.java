package org.leaf.api.internal.listener.events;

import org.leaf.api.command.Command;
import org.leaf.api.exceptions.NoPermissionException;
import org.leaf.api.internal.Cache;
import org.leaf.api.internal.CommandExecutionProcess;
import org.leaf.api.internal.FullPlayer;
import org.leaf.api.internal.PlayerProvider;
import org.leaf.api.internal.command.RemoteCommandBuilder;
import org.leaf.api.internal.fields.CommandLogEntry;
import org.leaf.roblox.Permission;
import org.leaf.utils.DataCollector;
import org.leaf.utils.Triplet;

/// Event representing a Command Raid event. This gets triggered whenever a command's {@link Command#getEvaluation()} is greater or equal to 10, which is the intended raid threshold.
public class RaidEvent extends Event {
    public final CommandLogEntry command;
    private RaidActionStatus status;
    private final boolean enteredLockdown;

    public RaidEvent(CommandLogEntry command, boolean enteredLockdown) {
        this.command = command;
        this.status = RaidActionStatus.UNHANDLED;
        this.enteredLockdown = enteredLockdown;
    }

    /// Get the problematic raid command.
    public CommandLogEntry getCommand() {
        return command;
    }

    /**
     * Check if the server entered lockdown during the raid because of this command. The cache sets this flag when entering lockdown mode. {@link Cache#isLockdownEnabled()} returns {@code true} if this flag is toggled on in this RaidEvent.
     * @return true if the server entered lockdown because of this command, false otherwise.
     */
    public boolean causedLockdown() {
        return enteredLockdown;
    }

    /// Shortcut to get the command evaluation. More about command evaluation here: {@link Command#getEvaluation()}.
    public int getEvaluation() {
        return command.command.getEvaluation();
    }

    /// Ban the author of the problematic command.
    /// @param collector The data collector used to process the command execution results.
    public boolean banSender(DataCollector<CommandExecutionProcess> collector) {
        if (status == RaidActionStatus.BAN) return false;
        var triplet = noPermissionCheck(collector);
        if (triplet.first) throw new NoPermissionException("Cannot ban a player with no permission.", triplet.second, triplet.third);

        boolean queued = Cache.instance.sendCommand(new RemoteCommandBuilder().fromRawCommand(":ban " + command.sender.id + " Banned by LERLC As a counter raid action.").build(), collector, true);
        if (queued) status = RaidActionStatus.BAN;

        return queued;
    }

    /// Kick the author of the problematic command.
    /// @param collector The data collector used to process the command execution results.
    public boolean kickSender(DataCollector<CommandExecutionProcess> collector) {
        if (status == RaidActionStatus.BAN) return false;
        var triplet = noPermissionCheck(collector);
        if (triplet.first) throw new NoPermissionException("Cannot ban a player with no permission.", triplet.second, triplet.third);

        boolean queued = Cache.instance.sendCommand(new RemoteCommandBuilder().fromRawCommand(":kick " + command.sender.id + " Kicked by LERLC As a counter raid action.").build(), collector, true);
        if (queued) status = RaidActionStatus.BAN;

        return queued;
    }

    /// Remove moderator permissions from the author of the problematic command.
    /// @param collector The data collector used to process the command execution results.
    public boolean deModSender(DataCollector<CommandExecutionProcess> collector) {
        if (status == RaidActionStatus.BAN) return false;
        var triplet = noPermissionCheck(collector);
        if (triplet.first) throw new NoPermissionException("Cannot ban a player with no permission.", triplet.second, triplet.third);

        boolean queued = Cache.instance.sendCommand(new RemoteCommandBuilder().fromRawCommand(":unmod " + command.sender.id + " Demoted (removed mod perms) by LERLC As a counter raid action.").build(), collector, true);
        if (queued) status = RaidActionStatus.BAN;

        return queued;
    }

    /// Remove administrator permissions from the author of the problematic command.
    /// @param collector The data collector used to process the command execution results.
    public boolean deAdminSender(DataCollector<CommandExecutionProcess> collector) {
        if (status == RaidActionStatus.BAN) return false;
        var triplet = noPermissionCheck(collector);
        if (triplet.first) throw new NoPermissionException("Cannot ban a player with no permission.", triplet.second, triplet.third);

        boolean queued = Cache.instance.sendCommand(new RemoteCommandBuilder().fromRawCommand(":unadmin " + command.sender.id + " Demoted (removed admin perms) by LERLC As a counter raid action.").build(), collector, true);
        if (queued) status = RaidActionStatus.BAN;

        return queued;
    }


    /**
     * Sends a warning message to the sender of the problematic command.
     * Please note that your message is still subject to Roblox's chat filtering, so an inappropriate message/word may get the whole message tagged.
     *
     * @param collector The data collector used to process the command execution results.
     * @param message The warning message to be sent to the sender.
     * @return true if the warning command was successfully sent, false otherwise.
     */
    public boolean warnSender(DataCollector<CommandExecutionProcess> collector, String message) {
        if (status == RaidActionStatus.BAN) return false;
        boolean queued = Cache.instance.sendCommand(new RemoteCommandBuilder().fromRawCommand(":pm " + command.sender.id + " " + message).build(), collector, true);
        if (queued) status = RaidActionStatus.BAN;

        return queued;
    }

    private Triplet<Boolean, Permission, Permission> noPermissionCheck(DataCollector<CommandExecutionProcess> collector) {
        FullPlayer fp = Cache.instance.getPlayerProvider().get(command.sender);
        return new Triplet<>(fp.getPermission().equals(Permission.CO_OWNER) || fp.getPermission().equals(Permission.OWNER), Permission.CO_OWNER, fp.getPermission());
    }
}
