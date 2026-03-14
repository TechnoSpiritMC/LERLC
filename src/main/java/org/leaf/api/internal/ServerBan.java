package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.BanDTO;
import org.leaf.roblox.RobloxPlayer;

public class ServerBan {
    private final RobloxPlayer player;

    public ServerBan(BanDTO banDTO) {
        this.player = RobloxPlayer.parse(banDTO.PlayerId());
    }

    /// Get the player that was banned.
    public RobloxPlayer getPlayer() {
        return player;
    }
}
