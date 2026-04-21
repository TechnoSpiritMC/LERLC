package org.leaf;

import org.leaf.api.internal.*;
import org.leaf.api.internal.listener.Listener;
import org.leaf.api.internal.listener.events.PlayerJoinEvent;
import org.leaf.api.internal.listener.events.PlayerLeaveEvent;
import org.leaf.utils.DataCollector;

import java.time.Duration;
import java.util.logging.Logger;

public class Main extends Listener {
    public static void main(String[] args) {
        Cache c = new Cache("OYWtpwxbpHYTjvvlfbnI-YjqqHSttLLyGgqYVYQOjnTyVxvmYOMSxledjhasb");
        c.getConfig().setOfflineThreshold(Duration.ofMinutes(10)).done();

        ListenerStore.register(new Main());

        FailedRequestStore.addHook(data -> System.out.println("Failed request: " + data));

        while (true) {
            try {
//                System.out.println("Current players (Should be AbstractPlayers): " + c.getPlayers());
//                System.out.println("Current join logs: " + c.getJoinLogs());
//                System.out.println("All playerProvider players: " + PlayerProvider.getAllPlayers());
//
//                System.out.println("----------------------------------------");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        System.err.println("Oh look! A player joined! " + event.getPlayer());
    }

    @Override
    public void onPlayerLeave(PlayerLeaveEvent event) {
        System.err.println("Oh look! A player left! " + event.getPlayer());
    }
}