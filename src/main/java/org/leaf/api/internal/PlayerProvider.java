package org.leaf.api.internal;

import org.leaf.WrapperConfig;
import org.leaf.api.http.dto.v1.PlayerDTO;
import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class PlayerProvider {
    List<RobloxPlayer> players;

    public PlayerProvider() {}

    public RobloxPlayer getPlayer(long userId) {
        return new RobloxPlayer(Objects.requireNonNull(players.stream().filter(player -> player.getUserId() == userId).findFirst().orElse(null)));
    }
    public RobloxPlayer getPlayer(String username) {
        return players.stream().filter(player -> player.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }
    public RobloxPlayer getPlayer(RobloxPlayer player) {
        return players.stream().filter(p -> p.equals(player)).findFirst().orElse(null);
    }
    public RobloxPlayer getPlayer(Player player) {
        return players.stream().filter(p -> p.equals(player.getPlayer())).findFirst().orElse(null);
    }
    public RobloxPlayer getPlayer(PlayerDTO player) {
        return players.stream().filter(p -> Objects.equals(p, RobloxPlayer.parse(player.Player()))).findFirst().orElse(null);
    }

    RobloxPlayer _getPlayer(long userId) {
        return new RobloxPlayer(Objects.requireNonNull(players.stream().filter(player -> player.getUserId() == userId).findFirst().orElse(null)));
    }
    RobloxPlayer _getPlayer(String username) {
        return new RobloxPlayer(Objects.requireNonNull(players.stream().filter(player -> player.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null)));
    }
    RobloxPlayer _getPlayer(RobloxPlayer player) {
        return new RobloxPlayer(Objects.requireNonNull(players.stream().filter(p -> p.equals(player)).findFirst().orElse(null)));
    }
    RobloxPlayer _getPlayer(Player player) {
        return new RobloxPlayer(Objects.requireNonNull(players.stream().filter(p -> p.equals(player.getPlayer())).findFirst().orElse(null)));
    }
    RobloxPlayer _getPlayer(PlayerDTO player) {
        return players.stream().filter(p -> Objects.equals(p, RobloxPlayer.parse(player.Player()))).findFirst().orElse(null);
    }

    public List<RobloxPlayer> getPlayers() {
        return players;
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
        players.stream().filter(p -> this.players.stream().noneMatch(p2 -> p2.equals(p))).forEach(this::addPlayer);
    }

    public void addAll(List<PlayerDTO> _players) {
        List<RobloxPlayer> players = _players.stream().map(p -> RobloxPlayer.parse(p.Player())).toList();
        players.stream().filter(p -> this.players.stream().noneMatch(p2 -> p2.equals(p))).forEach(this::addPlayer);
    }
}
