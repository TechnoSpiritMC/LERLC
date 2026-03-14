package org.leaf.api.internal;

import org.leaf.WrapperConfig;
import org.leaf.api.command.Command;

import java.util.List;
import java.util.Stack;

public class CommandData {
    private final Stack<Command> commands = new Stack<>();

    private final WrapperConfig config;


    public CommandData(List<Command> commands, WrapperConfig config) {
        this.config = config;

        this.commands.addAll(commands);
    }

    public void configUpdateProvider() {
        commands.setSize(config.getMaxCommandLogsLength());
    }

    public void addCommand(Command command) {
        commands.push(command);
    }

    public List<Command> getCommands() {
        return commands;
    }
}
