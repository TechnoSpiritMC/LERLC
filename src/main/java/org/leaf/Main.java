package org.leaf;

import org.leaf.api.internal.*;
import org.leaf.api.internal.listener.Listener;
import org.leaf.api.internal.listener.events.CommandEvent;
import org.leaf.api.internal.listener.events.PlayerJoinEvent;
import org.leaf.api.internal.listener.events.PlayerLeaveEvent;
import org.leaf.utils.DataCollector;

import java.time.Duration;
import java.util.logging.Logger;

public class Main extends Listener {
    public static void main(String[] args) {
        // Todo: better initialization?
        Cache c = new Cache("OYWtpwxbpHYTjvvlfbnI-YjqqHSttLLyGgqYVYQOjnTyVxvmYOMSxledjhasb");
        c.getConfig().setOfflineThreshold(Duration.ofMinutes(10)).done();

        ListenerStore.register(new Main());
        FailedRequestStore.addHook(data -> System.out.println("Failed request: " + data));

        while (true) {
            try {
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

    @Override
    public void onCommand(CommandEvent event) {
        System.err.println("Oh look! A command was sent! " + event.getCommand());
        System.err.println("Is it a raid command? " + ((event.getCommand().command.getEvaluation() > 10)? "Oh hell yeah!": "nah."));
    }
}