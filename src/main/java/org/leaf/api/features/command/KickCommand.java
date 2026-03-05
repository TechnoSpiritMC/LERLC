package org.leaf.api.features.command;

import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public final class KickCommand extends Command {

    private final RobloxPlayer target;
    private final String reason;

    /// Create a new instance of {@link KickCommand} explicitly providing all info about it.
    public KickCommand(String raw,
                       RobloxPlayer sender,
                       Instant timestamp,
                       RobloxPlayer target,
                       String reason) {

        super(raw, "kick", sender, timestamp);
        this.target = target;
        this.reason = reason;
    }

    /// Create a new instance of {@link KickCommand} implicitly providing the target player and the reason for the kick. The target and the reason will be parsed from the raw command.
    public KickCommand(String raw,
                       RobloxPlayer sender,
                       Instant timestamp) {

        super(raw, "kick", sender, timestamp);

        String[] commandArgs = raw.split(" ");
        target = RobloxPlayer.fromUsername(commandArgs[1]);
        reason = String.join(" ", commandArgs).substring(commandArgs[0].length() + commandArgs[1].length() + 2);
    }

    /// Get the target of the kick command. This was automatically parsed from the command unless provided explicitly.
    public RobloxPlayer getTarget() {
        return target;
    }

    /// Get the reason for the kick. This was automatically parsed from the command unless provided explicitly.
    public String getReason() {
        return reason;
    }
}