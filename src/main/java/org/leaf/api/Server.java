package org.leaf.api;

import org.leaf.api.features.JoinLogEntry;
import org.leaf.api.features.command.Command;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<JoinLogEntry> joinLogs = new ArrayList<>();
    private List<Command> commandLogs = new ArrayList<>();

    /// Get the join logs of this server.
    public List<JoinLogEntry> getJoinLogs() {
        return joinLogs;
    }

    public List<Command> getCommandLogs() {
        return commandLogs;
    }
}
