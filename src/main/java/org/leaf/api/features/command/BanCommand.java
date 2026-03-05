package org.leaf.api.features.command;

import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public final class BanCommand extends Command {

    private final RobloxPlayer target;
    private final String reason;

    /// Create a new instance of {@link BanCommand} explicitly providing all info about it.
    public BanCommand(String raw,
                       RobloxPlayer sender,
                       Instant timestamp,
                       RobloxPlayer target,
                       String reason) {

        super(raw, "ban", sender, timestamp);
        this.target = target;
        this.reason = reason;
    }

    /// Create a new instance of {@link BanCommand} implicitly providing the target player and the reason for the ban. The target and the reason will be parsed from the raw command.
    public BanCommand(String raw,
                       RobloxPlayer sender,
                       Instant timestamp) {

        super(raw, "ban", sender, timestamp);

        String[] commandArgs = raw.split(" ");
        target = RobloxPlayer.fromUsername(commandArgs[1]);

        raw = raw.substring(commandArgs[0].length() + commandArgs[1].length() + 3);
        reason = raw.trim();
    }

    /// Get the target of the ban command. This was automatically parsed from the command unless provided explicitly.
    public RobloxPlayer getTarget() {
        return target;
    }

    /// Get the reason for the ban. This was automatically parsed from the command unless provided explicitly.
    public String getReason() {
        return reason;
    }
}