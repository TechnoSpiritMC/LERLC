package org.leaf.api.internal;

import org.leaf.Main;
import org.leaf.api.exceptions.InvalidatedKeyException;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Context {
    private final String key;
    private Duration averageLatency = Duration.ofSeconds(-1);
    private final List<Duration> latencies = new ArrayList<>();

    public Context(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean testApiConnection() throws IOException {
        Request req = new Request(this, "/server", false, ConnectionMethod.GET);
        try {
            req.send();

        } catch (IOException e) {
            e.printStackTrace();
            Main.logger.severe("Failed to connect to API. Is the API up?");
            return false;

        } catch (InvalidatedKeyException e) {
            e.printStackTrace();
            Main.logger.severe("Invalidated server API Key! Are you sure this is the right API Key? The API might also be experiencing some issues at this moment.");
            return false;
        }

        latencies.add(req.latency);
        return req.returnCode == 200;
    }

    public void testLatency() {
        if (latencies.size() >= 5) {
            averageLatency = Duration.ofMillis(latencies.stream().mapToLong(Duration::toMillis).sum() / latencies.size());
            Main.logger.info("Finished API Latency checks. Computed average latency: " + averageLatency.toMillis() + "ms.");
        }

        String[] endpointsToTest = {
                "/server/players",
                "/server/joinlogs",
                "/server/queue",
                "/server/killlogs",
                "server/commandlogs"
        };

        Main.logger.info("Initiated API Latency checks. Estimated end time: " + latencies.stream().mapToLong(Duration::toMillis).sum() * (latencies.size() - 5) + "ms");

        for (int i = 0; i < (5 - latencies.size()); i++) {
            Request request = new Request(this, endpointsToTest[i], false, ConnectionMethod.GET);

            try {
                request.send();
                latencies.add(request.latency);
                Main.logger.info("Latency for " + endpointsToTest[i] + ": " + request.latency.toMillis() + "ms");

            } catch (IOException e) {
                Main.logger.warning("Failed to connect to API during precheck. Is the API up?");

            } catch (InvalidatedKeyException e) {
                Main.logger.warning("Invalidated server API Key! Are you sure this is the right API Key? The API might also be experiencing some issues at this moment.");

                throw e;
            }
        }

        averageLatency = Duration.ofMillis(latencies.stream().mapToLong(Duration::toMillis).sum() / latencies.size());
        Main.logger.info("Finished API Latency checks. Computed average latency: " + averageLatency.toMillis() + "ms.");

        if (averageLatency.toSeconds() >= 5) {
            Main.logger.severe("Average latency is " + averageLatency.toSeconds() + " seconds! This is more than 5 seconds, and this is a lot! Please check your internet connection. Some caching features might not work as expected!");
        }
    }

    public Duration getAverageLatency() {
        return averageLatency;
    }
}
