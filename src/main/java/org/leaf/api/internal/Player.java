package org.leaf.api.internal;

import org.jetbrains.annotations.Nullable;
import org.leaf.api.http.dto.v2.Vec2d;
import org.leaf.api.http.dto.v1.PlayerDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.roblox.Permission;
import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

public class Player {
    private final RobloxPlayer player;
    private String callSign;
    private String team;
    Instant lastSeen;
    int wantedStars = 0;
    Vec2d<Float> position = new Vec2d<>(0f, 0f);

    public Player(PlayerDTO dto) {
        this.player = RobloxPlayer.parse(dto.Player(), Permission.fromString(dto.Permission()));
        this.callSign = dto.Callsign();
        this.team= dto.Team();
    }

    public Player(NewApiDTO.v2PlayerDTO dto) {
        this.player = RobloxPlayer.parse(dto.Player(), Permission.fromString(dto.Permission()));
        this.callSign = dto.Callsign();
        this.team= dto.Team();
        this.wantedStars = dto.WantedStars();
        this.position = new Vec2d<>(dto.Location().LocationX(), dto.Location().LocationZ())
    }

    /// Get the {@link RobloxPlayer} assiciated with this player.
    public RobloxPlayer getPlayer() {
        return player;
    }

    /// Get the call sign of this player.
    /// This can be null if in a team that doesn't have call signs (E.g.: civilian.)
    @Nullable
    public String getCallSign() {
        return callSign;
    }

    /// Get the team of this player.
    public String getTeam() {
        return team;
    }

    /// Get the permission level of this player.
    public Permission getPermissions() {
        return player.getPermission();
    }

    /// Get the last time this player was seen.
    /// The cache will forget players that haven't been seen for 20+ minutes until seen again.
    public Instant getLastSeen() {
        return lastSeen;
    }

    void setLastSeen(Instant lastSeen) {
        this.lastSeen = lastSeen;
        this.player.setLastSeen(lastSeen);
    }

    void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    void setTeam(String team) {
        this.team = team;
    }
}
