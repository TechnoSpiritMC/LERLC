package org.leaf.api.internal;

import org.jetbrains.annotations.Nullable;
import org.leaf.api.http.dto.v1.JoinLogDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.roblox.RobloxPlayer;
import org.leaf.utils.OnNull;

import java.time.Duration;
import java.time.Instant;

import static org.leaf.api.internal.Cache.playerProvider;

public class JoinLogEntry {
    private final Instant joinedAt;
    private RobloxPlayer player = null;

    /// Create an instance of {@link JoinLogEntry}.
    /// <b>This constructor is deprecated! Please consider using {@link JoinLogEntry} using {@link JoinLogDTO} or {@link NewApiDTO.v2JoinLogDTO} instead!</b>
    @Deprecated
    public JoinLogEntry(Instant joinedAt, RobloxPlayer player) {
        this.joinedAt = joinedAt;
        this.player = player;
    }

    public JoinLogEntry(JoinLogDTO dto) {
        joinedAt = Instant.ofEpochSecond(dto.Timestamp());
        OnNull.onNullAsync(
                () -> playerProvider._getPlayer(RobloxPlayer.parse(dto.Player())),
                () -> playerProvider._getPlayer(RobloxPlayer.parse(dto.Player())),
                Duration.ofSeconds(5),
                RobloxPlayer.parse(dto.Player())
                ).thenAccept(result -> this.player = result);
    }

    public JoinLogEntry(NewApiDTO.v2JoinLogDTO dto) {
        joinedAt = Instant.ofEpochSecond(dto.Timestamp());

        OnNull.onNullAsync(
                () -> playerProvider._getPlayer(RobloxPlayer.parse(dto.Player())),
                () -> playerProvider._getPlayer(RobloxPlayer.parse(dto.Player())),
                Duration.ofSeconds(5),
                RobloxPlayer.parse(dto.Player())
        ).thenAccept(result -> this.player = result);
    }

    /// Get the {@link Instant} when the player joined your server.
    public Instant getJoinedAt() {
        return joinedAt;
    }

    /// Get the {@link RobloxPlayer} who joined your server.
    /// May return null if the cache hasn't been properly initialized yet.
    @Nullable
    public RobloxPlayer getPlayer() {
        return new RobloxPlayer(player);
    }

    /// Get the playtime of the player during their current session.
    public Duration getInstancePlaytime() {
        return Duration.between(joinedAt, Instant.now());
    }

    @Override
    public String toString() {
        return player.toString() + " joined at " + joinedAt;
    }
    public boolean equals(Object obj) {
        return obj instanceof JoinLogEntry && ((JoinLogEntry) obj).player.equals(player);
    }
    public int hashCode() {
        return player.hashCode();
    }
}