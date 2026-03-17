package org.leaf;

import org.leaf.api.internal.Cache;

import java.time.Duration;
import java.util.logging.Logger;

public class Main {
    public static final String VERSION = "Alpha 0.1-001";
    public static final Logger logger = Logger.getLogger("Leaf");

    public static void main(String[] args) {
        Cache c = new Cache("OYWtpwxbpHYTjvvlfbnI-YjqqHSttLLyGgqYVYQOjnTyVxvmYOMSxledjhasb");
        c.getConfig().setOfflineThreshold(Duration.ofMinutes(10)).done();

        while (true) {
            try {
                System.out.println(c.getPlayers());
                System.out.println(c.getJoinLogs());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}