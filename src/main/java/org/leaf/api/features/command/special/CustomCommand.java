package org.leaf.api.features.command.special;

import org.leaf.api.features.command.Command;
import org.leaf.api.features.command.CommandName;
import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;
import java.util.List;

/**
 * <b>Custom Commands</b>
 * <p>Represents custom {@code :log} commands. These can then be parsed by the application to infer their usage.</p>
 *
 * <p><b>Usage:</b>
 * <pre>{@code
 * :log <args...>
 * }
 * </pre>
 */
public class CustomCommand extends Command {
    private List<String> args;

    public CustomCommand(String raw,
                         RobloxPlayer sender,
                         CommandName commandName,
                         String trueName,
                         List<String> args) {
        super(raw, trueName, commandName, sender, Instant.now());
        this.args = args;
    }

    /// Get the arguments provided in the command.
    /// <p>If the command was {@code :log shift start}, {@link CustomCommand#getArgs()} will return <pre>{@code {"shift", "start"}}</pre></p>
    public List<String> getArgs() {
        return args;
    }
}
