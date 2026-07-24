package org.leaf.api.exceptions;

import org.leaf.roblox.Permission;

/**
 * Represents an exception thrown when a user does not have the permission to perform an action.
 * This exception can also be thrown when the Wrapper attempts to perform a moderation action against a player
 * that is immune to them hereby their permission level. For example, when you try to kick a person with a
 * permission level greater or equal to {@link Permission#CO_OWNER} as a part of a counter-raid measure.
 */
public class NoPermissionException extends RuntimeException {

    Permission maxPossiblePermission = Permission.UNDEFINED;
    Permission foundPermission = Permission.UNDEFINED;

    public NoPermissionException(String message, Permission maxPossiblePermission, Permission foundPermission) {
        super(message);
        this.maxPossiblePermission = maxPossiblePermission;
        this.foundPermission = foundPermission;
    }

    /**
     * Represents the maximum possible permission a user can have for some action to affect them. For example,
     * the maxPermissionPossible for kicking a player is {@link Permission#PLAYER} because any permission above
     * that cannot be kicked.
     * <br>
     * Please note that any person with permission above {@link Permission#CO_OWNER} cannot be affected
     * by any action doable with the API.
     * @return The maximum possible permission a user can have for some action to affect them.
     */
    public Permission getMaxPossiblePermission() {
        return maxPossiblePermission;
    }

    /**
     * Represents the permission that was found for the user.
     * <br>
     * This is the role that has been found to be above the maximum allowed one for that action.
     * @return The permission that was found for the user.
     */
    public Permission getFoundPermission() {
        return foundPermission;
    }
}
