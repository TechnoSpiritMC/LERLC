package org.leaf.api.internal.fields;

import org.leaf.api.http.dto.v1.QueuePlayerDTO;
import org.leaf.api.internal.AbstractPlayer;

import java.util.List;

public class ServerQueue {
    private List<AbstractPlayer> players;

    public ServerQueue(QueuePlayerDTO dto) {
        players = dto.playerIds().stream().map(AbstractPlayer::from).toList();
    }

    public List<AbstractPlayer> getPlayers() {
        return List.copyOf(players);
    }
}
