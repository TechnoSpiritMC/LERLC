package org.leaf.api.internal._new;

import org.leaf.api.http.dto.v1.PlayerDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.internal.Player;

public class AbstractPlayer {
    public final String username;
    public final long id;

    public static AbstractPlayer from(String username, long id) {
        return new AbstractPlayer(username, id);
    }

    public static AbstractPlayer from(FullPlayer player) {
        return new AbstractPlayer(player.getUsername(), player.getId());
    }

    public static AbstractPlayer from(PlayerDTO player) {
        String[] split = player.Player().split(":");
        return new AbstractPlayer(split[0], Long.parseLong(split[1]));
    }

    public static AbstractPlayer from(NewApiDTO.v2PlayerDTO player) {
        String[] split = player.Player().split(":");
        return new AbstractPlayer(split[0], Long.parseLong(split[1]));
    }

    public static AbstractPlayer from(String formatted) {
        String[] split = formatted.split(":");
        return new AbstractPlayer(split[0], Long.parseLong(split[1]));
    }

    @Deprecated
    public static AbstractPlayer from(Player oldPlayerFmt) {
        return new AbstractPlayer(oldPlayerFmt.getPlayer().getUsername(), oldPlayerFmt.getPlayer().getUserId());
    }

    public AbstractPlayer(String username, long id) {
        this.username = username;
        this.id = id;
    }

    public String toString() {
        return username + " (" + id + ")";
    }

    public boolean equals(Object obj) {
        if (obj instanceof AbstractPlayer) {
            return ((AbstractPlayer) obj).id == id;
        }
        return false;
    }

    /// Attempts to find the corresponding {@link FullPlayer} object. A fullPlayer encapsulates more data and is cached.
    public FullPlayer getPlayer() {
        return PlayerProvider.get(this);
    }
}
