package org.leaf.api.command.special;

import org.leaf.api.command.Command;
import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

/// Class representing a message command.<br>
/// ## Important!
/// <p><b>This class encapsulates both {@link CommandName#Hint} ({@code :h}) and {@link CommandName#Message} ({@code :m})!</b></p>
public class MessageCommand extends Command {
    private final String msg;

    public MessageCommand(String raw,
                          AbstractPlayer sender,
                          CommandName commandName,
                          String trueName,
                          Instant timestamp,
                          String msg) {

        super(raw, trueName, commandName, sender, timestamp);
        this.msg = msg;
    }

    /// Get the message broadcasted. For example, if the command was {@code :m hello!}, the message will be {@code "hello!"}
    public String getMessage() {
        return msg;
    }
}
