package org.leaf.api.internal;

import org.leaf.WrapperConfig;
import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class PlayerProvider {
    List<RobloxPlayer> players;

    public PlayerProvider() {}

    public List<RobloxPlayer> getPlayers() {
        return players;
    }
    public void setPlayers(List<RobloxPlayer> players) {
        this.players = players;
    }
    public void addPlayer(RobloxPlayer player) {
        if (players.contains(player)) return;
        players.add(player);
    }
    public void removePlayer(RobloxPlayer player) {
        players.remove(player);
    }
    public void clearPlayers() {
        players.clear();
    }
    public int getPlayerCount() {
        return players.size();
    }

    public void updatePlayers(WrapperConfig config, List<RobloxPlayer> players) {
        players.removeIf(player -> Duration.between(player.getLastSeen(), Instant.now()).toSeconds() > config.getOfflineThreshold().toSeconds());
    }
}
