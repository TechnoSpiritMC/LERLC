package org.leaf.api.internal;

import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/// Class responsible for holding and caching {@link FullPlayer} instances for this server. The cache refreshes these every few seconds (More precisely on every API v2 Refresh)
/// The user can request a {@link FullPlayer} instance by calling {@link PlayerProvider#get(long)} or {@link PlayerProvider#get(AbstractPlayer)}.
///
/// {@link FullPlayer} instances are immutable and are used when you need more information about a user than their username and user ID.
///
/// <b>Warning:</b><br>
/// Returned FullPlayers are copies of original objects that are cached by {@link PlayerProvider}.
/// Returned objects are also immutable, which means that it is advised to call {@link FullPlayer#refreshCopy()}
/// if an instance has been living for long enough for something to have happened to it.
public class PlayerProvider {
    static Map<Long, FullPlayer> playerMap = new HashMap<>();

    static void addPlayer(FullPlayer player) {
        if (playerMap.containsKey(player.getId())) {
            updatePlayer(player);
            return;
        }
        ;

        playerMap.put(player.getId(), player);
    }

    static void addAll(List<FullPlayer> players) {
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


    /// Attempts to find a {@link FullPlayer} instance associated with a certain {@link AbstractPlayer}'s ID. A {@link FullPlayer} instance
    /// provides much more information about a player, but AbstractPlayers are passed around as they are more memory
    /// efficient and should suffise for most tasks.
    ///
    /// <b>Warning:</b><br>
    /// Returned FullPlayers are copies of original objects that are cached by {@link PlayerProvider}.
    /// Returned objects are also immutable, which means that it is advised to call {@link FullPlayer#refreshCopy()}
    /// if an instance has been living for long enough for something to have happened to it.
    static public FullPlayer get(long id) {
        return new FullPlayer(playerMap.get(id));
    }

    /// Attempts to find a {@link FullPlayer} instance associated with a certain {@link AbstractPlayer}'s ID. A {@link FullPlayer} instance
    /// provides much more information about a player, but AbstractPlayers are passed around as they are more memory
    /// efficient and should suffise for most tasks.
    ///
    /// <i>Note: This method fallbacks to {@link PlayerProvider#get(long)} internally. Calling {@link PlayerProvider#get(long)} directly providing {@code player.id} is also possible and will lead to the same effect.</i>
    ///
    /// <b>Warning:</b><br>
    /// Returned FullPlayers are copies of original objects which are cached by {@link PlayerProvider}.
    /// Returned objects are also immutable, which means that it is advised to call {@link FullPlayer#refreshCopy()}
    /// if an instance has been living for long enough for something to have happened to it.
    static public FullPlayer get(AbstractPlayer player) {
        return get(player.id);
    }

    /// Returns a list of all {@link FullPlayer} instances currently cached.
    ///
    /// <b>Warning:</b><br>
    /// Returned FullPlayers are copies of original objects that are cached by {@link PlayerProvider}.
    /// Returned objects are also immutable, which means that it is advised to call {@link FullPlayer#refreshCopy()}
    /// if an instance has been living for long enough for something to have happened to it.
    static public List<FullPlayer> getAllPlayers() {
        return playerMap.values().stream().map(FullPlayer::new).toList();
    }


    private static <T, V> void updateIfChanged(
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
