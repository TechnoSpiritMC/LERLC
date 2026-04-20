package org.leaf.api.internal;

import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.internal.fields.Location;
import org.leaf.roblox.Permission;

import java.time.Instant;

public class FullPlayer {
    private final String username;
    private final long id;
    private Permission permission = Permission.UNDEFINED;
    public Instant lastSeen = Instant.now();

    // V2 API.
    private String callSign = null;
    Location location = null;
    int wantedStars = -1;

    // Flags.
    public boolean isOnline = true;
    public boolean isBanned = false;

    public FullPlayer fromAbstract(AbstractPlayer abstractPlayer) {
        return PlayerProvider.get(abstractPlayer);
    }

    public FullPlayer(String username, long id, Permission permission, boolean isOnline, boolean isBanned, String callSign, Location location, int wantedStars) {
        this.username = username;
        this.id = id;
        this.permission = permission;
        this.isOnline = isOnline;
        this.isBanned = isBanned;
        this.callSign = callSign;
        this.location = location;
        this.wantedStars = wantedStars;
    }

    public FullPlayer(FullPlayer other) {
        this.username = other.username;
        this.id = other.id;
        this.permission = other.permission;
        this.lastSeen = other.lastSeen;
        this.isOnline = other.isOnline;
        this.isBanned = other.isBanned;
        this.callSign = other.callSign;
        this.location = other.location;
        this.wantedStars = other.wantedStars;
    }

    public FullPlayer(NewApiDTO.v2PlayerDTO player) {
        this.username = player.Player().split(":")[0];
        this.id = Long.parseLong(player.Player().split(":")[1]);
        this.permission = switch (player.Permission()) {
            case "Normal" -> Permission.PLAYER;
            case "Server Administrator" -> Permission.ADMINISTRATOR;
            case "Server Owner" -> Permission.OWNER;
            case "Server Moderator" -> Permission.MODERATOR;
            default -> Permission.UNDEFINED;
        };
        this.lastSeen = Instant.now();
        this.isOnline = true;
        this.isBanned = false;
        this.callSign = player.Callsign();
        this.location = Location.from(player.Location());
        this.wantedStars = player.WantedStars();
    }

    /// Returns the username of the player.
    public String getUsername() {
        return username;
    }
    /// Returns the id of the player. This is the Roblox player ID. Players are compared with it, and the hash value is based on it.
    public long getId() {
        return id;
    }
    /// Returns the in game permission of the player. Please note that this is the permission of the player in the server and does not take into account any potential global ERLC permissions.
    public Permission getPermission() {
        return permission;
    }
    /// Returns the timestamp of the cache update when the player was last mentioned in the player list.
    public Instant getLastSeen() {
        return lastSeen;
    }

    /// Checks whether this object has higher {@link Permission} than another player object.
    /// @return true if the ordinal value of this object's {@link Permission} is higher than the other object.
    public boolean isHigherRankedThan(FullPlayer other) {
        return this.permission.isHigherThan(other.permission);
    }

    /// @return true of the user is online. Please note that a user is considered offline if they haven't been seen for more than 20 seconds.
    public boolean isOnline() {
        return isOnline;
    }
    /// @return true if the user is banned.
    public boolean isBanned() {
        return isBanned;
    }

    /// @return true if the user is the owner of the server.
    public boolean isOwner() {
        return permission == Permission.OWNER;
    }

    /// Returns the player's call sign. Please note that this may return null if the player was initialized using the v1 API.
    public String getCallSign() {
        return callSign;
    }

    /// Returns the player's location. Please note that this may return null if the player was initialized using the v1 API.
    public Location getLocation() {
        return location;
    }

    /// Returns the player's wanted stars. Please note that this may return -1 if the player was initialized using the v1 API
    public int getWantedStars() {
        return wantedStars;
    }


    void setPermission(Permission permission) {
        this.permission = permission;
    }

    void setSeenNow() {
        this.lastSeen = Instant.now();
    }
    void setLastSeen(Instant lastSeen) {
        this.lastSeen = lastSeen;
    }
    void setOnline(boolean online) {
        this.isOnline = online;
    }
    void setBanned(boolean banned) {
        this.isBanned = banned;
    }
    void setCallSign(String callSign) {
        this.callSign = callSign;
    }
    void setLocation(Location location) {
        this.location = location;
    }
    void setWantedStars(int wantedStars) {
        this.wantedStars = wantedStars;
    }

    @Override
    public String toString() {
        return "FullUser: (" + permission + "): " + username + " (" + id + ")";
    }
}
