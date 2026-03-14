package org.leaf.roblox;

public enum Permission {
    /// Represents an undefined Permission. A player has this permission if they weren't properly initialized, or if we encountered an API-level error.
    UNDEFINED,

    /// Represents a usual player without any permission. A banned / kicked player can only be a PLAYER.
    PLAYER,

    /// Represents a server helper. Helpers cannot be kicked nor banned, and they have minimal command permissions.
    HELPER,

    /// Server moderators cannot be kicked or banned. They have moderation commands, but they cannot ban.
    MODERATOR,

    /// Private server Administrators have basically all commands, and cannot be affected by moderation actions.
    ADMINISTRATOR,

    /// Server co-owners can perform <b>all</b> commands, are immune to moderation actions, and can access your server's API Key in the settings panel.
    CO_OWNER,

    /// Represents the ERLC Private Server Owner. Has all permissions and a reserved place for them in the server.
    OWNER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /// Check whether a permission is above another permission.
    /// @return {@code true} if this permission is higher than the other one.
    public boolean isHigherThan(Permission other) {
        return ordinal() > other.ordinal();
    }

    public static Permission fromString(String perm) {
        return switch (perm) {
            case "Normal" -> PLAYER;
            case "Server Administrator" -> ADMINISTRATOR;
            case "Server Owner" -> OWNER;
            case "Server Moderator" -> MODERATOR;
            default -> UNDEFINED;
        };
    }
}
