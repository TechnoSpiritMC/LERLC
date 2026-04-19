package org.leaf.api.internal;

import java.util.Arrays;
import java.util.List;

public enum QueryType {
    Players,
    Staff,
    JoinLogs,
    Queue,
    KillLogs,
    CommandLogs,
    ModCalls,
    EmergencyCalls,
    Vehicles,
    All, None;

    @Override
    public String toString() {
        return this.name();
    }

    private static final List<QueryType> QUERYABLE = Arrays.stream(QueryType.values())
            .filter(q -> q != All && q != None)
            .toList();

    public List<String> allWanted() {
        if (this == All) {
            return QUERYABLE.stream().map(Enum::name).toList();
        } else if (this == None) {
            return List.of();
        } else {
            return List.of(this.name());
        }
    }
}
