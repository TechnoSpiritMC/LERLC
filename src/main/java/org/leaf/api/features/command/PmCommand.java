package org.leaf.api.features.command;

import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PmCommand extends Command {
    private List<RobloxPlayer> targets = new ArrayList<>();
    private final String message;

    /// Construct a new {@link PmCommand} instance providing explicitly all of its elements.
    public PmCommand(String raw,
                     RobloxPlayer sender,
                     Instant timestamp,
                     List<RobloxPlayer> targets,
                     String message) {

        super(raw, "pm", sender, timestamp);
        this.targets = targets;
        this.message = message;
    }

    /// Construct a new {@link PmCommand} instance implicitly providing the targets and the message. The targets and the message will be parsed from the raw command.
    public PmCommand(String raw,
                     RobloxPlayer sender,
                     Instant timestamp) {

        super(raw, "pm", sender, timestamp);

        raw = raw.substring(raw.indexOf(" ") + 1).strip();
        StringBuilder targetBuilder = new StringBuilder();

        while (!raw.isEmpty()) {
            if (raw.charAt(0) == ' ') {
                break;
            }
            targetBuilder.append(raw.charAt(0));
            raw = raw.substring(1);
        }

        //TODO: Replace this with a proper player getter from the Cache, which defaults to an unknown player if the command has been sent too long ago.
        if (!targetBuilder.isEmpty()) targets = Stream.of(targetBuilder.toString().split(",")).map(s -> new RobloxPlayer(s, 0)).toList();

        raw = raw.substring(1);

        message = raw;
    }

    /// Get the list of targets of this {@link PmCommand}.
    public List<RobloxPlayer> getTargets() {
        return targets;
    }

    /// Get the message of this {@link PmCommand}.
    public String getMessage() {
        return message;
    }
}
