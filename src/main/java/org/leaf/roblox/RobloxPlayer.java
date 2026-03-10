package org.leaf.roblox;

import org.leaf.Main;

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

    /// Creates a new {@link RobloxPlayer} with the given username. They can be compared on the hashCode of their username.
    public RobloxPlayer(String username) {
        this.username = username;
        this.userId = username.hashCode();

        this.flags = 0b00000010;
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

    /// Flags are used to explicitly provide details about the object. It can be used to tell if the object was
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
    public byte getFlags() {
        return flags;
    }

    /// Returns an {@link Instant} corresponding the last time this player was seen online. Please note that: {@code if(p.getLastSeen() > 30 seconds)} the player is considered as offline.
    public Instant getLastSeen() {
        return lastSeen;
    }

    /// Sets the last time this player was seen online. <b>This is meant to be used only by the Cache, and not by the user!</b>
    public void setLastSeen(Instant lastSeen) {
        this.lastSeen = lastSeen;
    }

    /// Compares two {@link RobloxPlayer} objects that have been both initialized using only a username. Comparison between fully and partly initialized objects is impossible.
    public boolean absoluteEquals(RobloxPlayer other) {
        if (other == null) return false;

        if (!((other.flags & 0b00000010) == 0b00000010 && (flags & 0b00000010) == 0b00000010)) {
            Main.logger.warning(this.username + " or " + other.username + " has been initialized properly. Unable to compare them using username only logic!");
            return false;
        }

        return other.username.hashCode() == username.hashCode();
    }

    @Override
    public String toString() {
        return username + " (" + userId + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RobloxPlayer)) return false;

        RobloxPlayer other = (RobloxPlayer) obj;

        boolean thisUsernameOnly = (flags & 0b00000010) != 0;
        boolean otherUsernameOnly = (other.flags & 0b00000010) != 0;

        if (thisUsernameOnly || otherUsernameOnly) {
            if (thisUsernameOnly && otherUsernameOnly) {
                Main.logger.warning(username + " and " + other.username +
                        " are both username-only. Comparing by username.");
            } else {
                Main.logger.warning(username + " and " + other.username +
                        " initialized differently. Comparing by username; may be inaccurate!");
            }
            return username.equals(other.username);
        }

        return userId == other.userId;
    }

    @Override
    public int hashCode() {
        // Follow equals logic: use username if username-only, else userId
        if ((flags & 0b00000010) != 0) {
            return username.hashCode();
        } else {
            return Long.hashCode(userId);
        }
    }

    /// Compares two players based on their highest permission.
    public static boolean isHigherThan(RobloxPlayer p1, RobloxPlayer p2) {
        return p1.isHigherThan(p2);
    }

    /// Parses and constructs a new {@link RobloxPlayer} from a raw API Structure (username:id)
    public static RobloxPlayer parse(String user) {
        return new RobloxPlayer(user.substring(0, user.indexOf(':')), Long.parseLong(user.substring(user.indexOf(':') + 1)));
    }
}
