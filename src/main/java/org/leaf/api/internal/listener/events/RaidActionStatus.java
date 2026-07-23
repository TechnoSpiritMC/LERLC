package org.leaf.api.internal.listener.events;

/// Enum representing all the different actions that can be / have been taken against a used in the case of a command raid in game.
public enum RaidActionStatus {
    /// Default value. Set if a raid has not been dealt with yet.
    UNHANDLED,
    /// Value set if a raid has been handled but deemed not problematic.
    IGNORED,
    /// Value representing an action taken against the raid author. This sends them a warning message in game, informing them of their wrongdoings.
    WARN,
    /// Value representing an action taken against the raid author. This kicks the raid author from the private server.
    KICK,
    /// Value representing an action taken against the raid author. This bans the raid author from the private server.
    BAN,
    /// Value representing an action taken against the raid author. This removed mod perms from the author.
    REMOVE_MOD,
    /// Value representing an action taken against the raid author. This removed admin perms from the author.
    REMOVE_ADMIN,
    /// Value set if the raid author couldn't been handled. For example, if they have permissions that cannot be revoked with the API. (e.g. Co Owner permissions)
    NO_PERMISSION;
}
