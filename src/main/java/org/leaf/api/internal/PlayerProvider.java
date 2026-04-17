package org.leaf.api.internal;

import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PlayerProvider {
    static Map<Long, FullPlayer> playerMap = new HashMap<>();

    static void addPlayer(FullPlayer player) {
        if (playerMap.containsKey(player.getId())) {
            updatePlayer(player);
            return;
        };

        playerMap.put(player.getId(), player);
    }

    public static void addAll(List<FullPlayer> players) {
        players.forEach(PlayerProvider::addPlayer);
    }

    static void updatePlayer(FullPlayer player) {
        long id = player.getId();
        FullPlayer old = playerMap.get(id);

        if (old == null) throw new IllegalStateException("Player not found, cannot update it.");

        updateIfChanged(old, player, FullPlayer::getLastSeen, FullPlayer::setLastSeen);
        updateIfChanged(old, player, FullPlayer::getPermission, FullPlayer::setPermission);
        updateIfChanged(old, player, FullPlayer::isOnline, FullPlayer::setOnline);
        updateIfChanged(old, player, FullPlayer::isBanned, FullPlayer::setBanned);
        updateIfChanged(old, player, FullPlayer::getCallSign, FullPlayer::setCallSign);
        updateIfChanged(old, player, FullPlayer::getLocation, FullPlayer::setLocation);
        updateIfChanged(old, player, FullPlayer::getWantedStars, FullPlayer::setWantedStars);
    }

    static void updatePlayers() {
        Instant now = Instant.now();

        for (FullPlayer player : playerMap.values()) {
            if (now.minus(10, java.time.temporal.ChronoUnit.MINUTES).isAfter(player.getLastSeen())) {
                playerMap.remove(player.getId());
            }
        }
    }

    static void removePlayer(long id) {
        playerMap.remove(id);
    }

    static FullPlayer __get(long id) {
        return playerMap.get(id);
    }
    static FullPlayer __get(AbstractPlayer player) {
        return __get(player.id);
    }


    static public FullPlayer get(long id) {
        return new FullPlayer(playerMap.get(id));
    }

    static public FullPlayer get(AbstractPlayer player) {
        return get(player.id);
    }

    static public List<FullPlayer> getAllPlayers() {
        return playerMap.values().stream().map(FullPlayer::new).toList();
    }


    private static <T, V> void updateIfChanged (
            T oldObj,
            T newObj,
            Function<T, V> getter,
            BiConsumer<T, V> setter
    ) {
        V oldVal = getter.apply(oldObj);
        V newVal = getter.apply(newObj);

        if (!Objects.equals(oldVal, newVal)) {
            setter.accept(oldObj, newVal);
        }
    }
}
