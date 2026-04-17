package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.BanDTO;

public class ServerBan {
    private final AbstractPlayer player;

    public ServerBan(BanDTO banDTO) {
        this.player = AbstractPlayer.from(banDTO.PlayerId());
    }

    /// Get the player that was banned.
    public AbstractPlayer getPlayer() {
        return player;
    }
}
