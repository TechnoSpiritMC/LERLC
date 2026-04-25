package org.leaf.api.internal.command;

import org.leaf.api.command.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandUtils {

    public static final Map<String, Integer> commandMap = Map.ofEntries(
            Map.entry("ban", 8),
            Map.entry("kick", 7),
            Map.entry("kill", 6),
            Map.entry("down", 6),
            Map.entry("jail", 6),
            Map.entry("mod", 8),
            Map.entry("admin", 9),
            Map.entry("load", 4),
            Map.entry("refresh", 2)
    );

    /// This method parses commands. Command parsing consists in evaluating the potential damage a command can do to determine if a command should be considered as a raid or no.
    ///
    /// Evaluation in done by first attributing a number to every potentially harmful command. For example, a {@code :ban} command will be assigned a value of 8.
    /// <br>After that, the command gets parsed, and the number of targets is added to the evaluation.
    /// <br>Finally, if one of the targets is a mass selector (like {@code all}), the evaluation is increased by 5.
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
    public static int evaluate(Command command) {
        String cmd = command.getRawCommand();

        CommandParser parser = new CommandParser(cmd);
        int eval = 0;

        eval += commandMap.getOrDefault(command.getName(), 0);
        eval += parser.getArguments().contains("all") || parser.getArguments().contains("others") ? 5 : 0;

        if (parser.getArguments().size() > 1 && commandMap.containsKey(command.getName())) eval += (int) parser.targets;

        return eval;
    }
}

class CommandParser {
    public String command;
    public List<String> arguments;
    public long targets;

    public CommandParser(String command) {
        this.command = command;

        parse();
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArguments() {
        return arguments;
    }

    private void parse() {
        List<String> tokens = new ArrayList<>(List.of(command.split(" ")));

        command = tokens.removeFirst();
        arguments = tokens;
        targets = command.chars().filter(c -> c == ',').count()+1;
    }
}