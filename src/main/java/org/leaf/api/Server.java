package org.leaf.api;

import org.leaf.api.features.JoinLogEntry;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<JoinLogEntry> joinLogs = new ArrayList<>();

    /// Get the join logs of this server.
    public List<JoinLogEntry> getJoinLogs() {
        return joinLogs;
    }
}
