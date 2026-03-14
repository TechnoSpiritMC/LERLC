package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.KillLogDTO;
import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public class KillLogEntry {
    private final RobloxPlayer killer, victim;
    private final Instant timestamp;

    public KillLogEntry(KillLogDTO dto) {
        killer = RobloxPlayer.parse(dto.Killer());
        victim = RobloxPlayer.parse(dto.Killed());
        timestamp = Instant.ofEpochSecond(dto.Timestamp());
    }

    /// Get the killer.
    public RobloxPlayer getKiller() {
        return killer;
    }

    /// Get the victim.
    public RobloxPlayer getVictim() {
        return victim;
    }

    /// Get the timestamp of when the kill occurred.
    public Instant getTimestamp() {
        return timestamp;
    }
}
