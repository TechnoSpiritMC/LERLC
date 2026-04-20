package org.leaf;

import org.leaf.api.internal.Cache;
import org.leaf.api.internal.FailedRequest;
import org.leaf.api.internal.FailedRequestStore;
import org.leaf.api.internal.PlayerProvider;
import org.leaf.utils.DataCollector;

import java.time.Duration;
import java.util.logging.Logger;

public class Main {
    public static final String VERSION = "Alpha 0.1-001";
    public static final Logger logger = Logger.getLogger("Leaf");

    public static void main(String[] args) {
        Cache c = new Cache("OYWtpwxbpHYTjvvlfbnI-YjqqHSttLLyGgqYVYQOjnTyVxvmYOMSxledjhasb");
        c.getConfig().setOfflineThreshold(Duration.ofMinutes(10)).done();

        FailedRequestStore.addHook(data -> System.out.println("Failed request: " + data));

        while (true) {
            try {
                System.out.println("Current players (Should be AbstractPlayers): " + c.getPlayers());
                System.out.println("Current join logs: " + c.getJoinLogs());
                System.out.println("All playerProvider players: " + PlayerProvider.getAllPlayers());

                System.out.println("----------------------------------------");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}