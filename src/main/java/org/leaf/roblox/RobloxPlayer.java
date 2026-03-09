package org.leaf.roblox;

import java.time.Instant;

public class RobloxPlayer {
    private final String username;
    private final long userId;
    public Permission permission = Permission.UNDEFINED;
    public byte flags = 0b0;
    public Instant lastSeen = Instant.now();

    /// Creates a new {@link RobloxPlayer} with the given username and id. They can be compared based on their user id.
    public RobloxPlayer(String username, long userId) {
        this.username = username;
        this.userId = userId;
    }

    /// Creates a new {@link RobloxPlayer} with the given username and id. They can be compared based on their user id.<br>
    /// @param flags Field used to explicitly provide details about the object. It can be used to tell if the object was
    /// initialized using only a player ID, or if the user has left the server. Flags are mostly used internally but can also
    /// be used by the user. Below is a table detailing their meaning. <b>Please note that flags may not be available for all {@link RobloxPlayer} objects!</b>
    /// <br>
    /// <br>
    ///
    /// <table style="border-collapse: collapse;">
    /// <tr>
    ///   <th style="border:1px solid gray;padding:4px;">Flag bit</th>
    ///   <th style="border:1px solid gray;padding:4px;">Description</th>
    /// </tr>
    /// <tr>
    ///   <td style="border:1px solid gray;padding:4px;">{@code 00000001}</td>
    ///   <td style="border:1px solid gray;padding:4px;">Initialized using only a user ID.</td>
    /// </tr>
    ///
    /// <tr>
    ///   <td style="border:1px solid gray;padding:4px;">{@code 00000010}</td>
    ///   <td style="border:1px solid gray;padding:4px;">Initialized using only a username.</td>
    /// </tr>
    ///
    /// <tr>
    ///   <td style="border:1px solid gray;padding:4px;">{@code 00000100}</td>
    ///   <td style="border:1px solid gray;padding:4px;">User is no longer online.
    ///     <br><br><i>Please note that player objects are kept in<br> the cache for 10 minutes after they left.<br>While they are cached and not in game,<br>this flag will automatically be set.</i></td>
    /// </tr>
    ///
    /// <tr>
    ///   <td style="border:1px solid gray;padding:4px;">{@code 00001000}</td>
    ///   <td style="border:1px solid gray;padding:4px;">User is the server owner. <br><br><i>Please note that this is equivalent to:<br>{@code getPermission() ->} {@link Permission#OWNER}</i></td>
    /// </tr>
    ///
    /// <tr>
    ///   <td style="border:1px solid gray;padding:4px;">{@code 00010000}</td>
    ///   <td style="border:1px solid gray;padding:4px;">User is banned.</td>
    /// </tr>
    /// </table>
    ///

    public RobloxPlayer(String username, long userId, byte flags) {
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

    /// Compares this player to another based on their highest permission.
    /// @return true if the current player has higher permissions than the other one.
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
