package org.leaf.api.internal.fields;

import org.leaf.api.http.dto.v1.KillLogDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.internal.AbstractPlayer;

import java.time.Instant;

public class KillLogEntry {
    private final AbstractPlayer killer, victim;
    private final Instant timestamp;

    public KillLogEntry(KillLogDTO dto) {
        killer = AbstractPlayer.from(dto.Killer());
        victim = AbstractPlayer.from(dto.Killed());
        timestamp = Instant.ofEpochSecond(dto.Timestamp());
    }

    public KillLogEntry(NewApiDTO.v2KillLogDTO dto) {
        killer = AbstractPlayer.from(dto.Killer());
        victim = AbstractPlayer.from(dto.Killed());
        timestamp = Instant.ofEpochSecond(dto.Timestamp());
    }

    /// Get the killer.
    public AbstractPlayer getKiller() {
        return killer;
    }

    /// Get the victim.
    public AbstractPlayer getVictim() {
        return victim;
    }

    /// Get the timestamp of when the kill occurred.
    public Instant getTimestamp() {
        return timestamp;
    }
}
