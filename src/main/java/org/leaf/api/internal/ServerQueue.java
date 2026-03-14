package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.QueuePlayerDTO;
import org.leaf.roblox.RobloxPlayer;

import java.util.List;

public class ServerQueue {
    private List<RobloxPlayer> players;

    public ServerQueue(QueuePlayerDTO dto) {
        players = dto.playerIds().stream().map(RobloxPlayer::new).toList();
    }
}
