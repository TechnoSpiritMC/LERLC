package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.ModCallDTO;
import org.jetbrains.annotations.Nullable;
import org.leaf.api.http.dto.v2.NewApiDTO;

import java.time.Instant;

public class ModCall {
    AbstractPlayer caller, moderator;
    Instant timestamp;

    public ModCall(ModCallDTO dto) {
        caller = AbstractPlayer.from(dto.Caller());
        moderator = dto.Moderator().isEmpty()? null: AbstractPlayer.from(dto.Moderator());
        timestamp = Instant.ofEpochSecond(dto.Timestamp());
    }

    public ModCall(NewApiDTO.v2ModCallDTO dto) {
        caller = AbstractPlayer.from(dto.Caller());
        moderator = dto.Moderator().isEmpty()? null: AbstractPlayer.from(dto.Moderator());
        timestamp = Instant.ofEpochSecond(dto.Timestamp());
    }

    /// Get the player who called the moderation action.
    public AbstractPlayer getCaller() {
        return caller;
    }

    /// Get the moderator who came to assist.
    /// <b>This may return null if the call hasn't been responded to yet!</b>
    @Nullable
    public AbstractPlayer getModerator() {
        return moderator;
    }

    /// Get the timestamp of when the call was made.
    public Instant getTimestamp() {
        return timestamp;
    }
}
