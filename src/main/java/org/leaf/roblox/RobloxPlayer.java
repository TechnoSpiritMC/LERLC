package org.leaf.roblox;

public class RobloxPlayer {
    private final String username;
    private final long userId;
    public Permission permission = Permission.UNDEFINED;

    /// Creates a new {@link RobloxPlayer} with the given username and id. They can be compared based on their user id.
    public RobloxPlayer(String username, long userId) {
        this.username = username;
        this.userId = userId;
    }

    /// Creates a new {@link RobloxPlayer} with the given username and id. They can be compared based on their user id.
    public RobloxPlayer(String username, long userId, Permission permission) {
        this.username = username;
        this.userId = userId;
        this.permission = permission;
    }

    /// Returns the username of this player.
    public String getUsername() {
        return username;
    }

    /// Returns the id of this player.
    public long getUserId() {
        return userId;
    }

    /// Get the player's highest permission. (From {@code PLAYER} (lowest) to {@code OWNER} (highest)).
    public Permission getPermission() {
        return permission;
    }

    public boolean isHigherThan(RobloxPlayer other) {
        return permission.isHigherThan(other.permission);
    }

    @Override
    public String toString() {
        return username + " (" + userId + ")";
    }
    public boolean equals(Object obj) {
        return obj instanceof RobloxPlayer && ((RobloxPlayer) obj).userId == userId;
    }
    public int hashCode() {
        return Long.hashCode(userId);
    }

    /// Generates a new {@link RobloxPlayer} with the given username. Please note that such a player instance will not behave like a normal {@link RobloxPlayer} instance, as the id will be set to -1, a substitute for "unknown".
    /// This method is the equivalent of {@code new RobloxPlayer(username, -1)}.
    public static RobloxPlayer fromUsername(String username) {
        return new RobloxPlayer(username, -1);
    }

    /// Compares two players based on their highest permission.
    public static boolean isHigherThan(RobloxPlayer p1, RobloxPlayer p2) {
        return p1.isHigherThan(p2);
    }
}
