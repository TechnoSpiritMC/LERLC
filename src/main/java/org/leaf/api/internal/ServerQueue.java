package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.QueuePlayerDTO;

import java.util.List;

public class ServerQueue {
    private List<AbstractPlayer> players;

    public ServerQueue(QueuePlayerDTO dto) {
        players = dto.playerIds().stream().map(AbstractPlayer::from).toList();
    }

    public List<AbstractPlayer> getPlayers() {
        return players;
    }
}
