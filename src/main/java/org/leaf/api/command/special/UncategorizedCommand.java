package org.leaf.api.command.special;

import org.leaf.api.command.Command;
import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

/** Class representing an uncategorized command.
 * This is used to represent:
 * <ul>
 *     <li>Commands that may have not been yet implemented in the Wrapper.</li>
 *     <li>Potential ERLC Staff commands, unknown to the public.</li>
 * </ul>
 * <br>
 * Please note that an uncategorized command is congruous to the abstract {@link Command} class, in the sense that {@link UncategorizedCommand} doesn't expose any additional logic over the abstract {@link Command} class.
 * <br><br>
 * Uncategorized commands need to be parsed manually using {@link Command}'s methods:
 * <ul>
 *     <li>{@link Command#getCommandName()}
 *     <li>{@link Command#getName()}
 *     <li>{@link Command#getSender()}
 *     <li>{@link Command#getTimestamp()}
 *     <li><i>{@link Command#getEvaluation()}</i> (Available only via the abstract {@link Command} class).
 * </ul>
 */
public class UncategorizedCommand extends Command {
    public UncategorizedCommand(String rawCommand,
                                CommandName commandName,
                                String name,
                                AbstractPlayer sender,
                                Instant timestamp) {

        super(rawCommand, name, commandName, sender, timestamp);
    }
}
