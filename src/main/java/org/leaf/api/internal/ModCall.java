package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.ModCallDTO;
import org.leaf.roblox.RobloxPlayer;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class ModCall {
    RobloxPlayer caller, moderator;
    Instant timestamp;

    public ModCall(ModCallDTO dto) {
        caller = RobloxPlayer.parse(dto.Caller());
        moderator = dto.Moderator().isEmpty()? null: RobloxPlayer.parse(dto.Moderator());
        timestamp = Instant.ofEpochSecond(dto.Timestamp());
    }

    /// Get the player who called the moderation action.
    public RobloxPlayer getCaller() {
        return caller;
    }

    /// Get the moderator who came to assist.
    /// <b>This may return null if the call hasn't been responded to yet!</b>
    @Nullable
    public RobloxPlayer getModerator() {
        return moderator;
    }

    /// Get the timestamp of when the call was made.
    public Instant getTimestamp() {
        return timestamp;
    }
}
