package org.leaf.api.http.cache;

import org.leaf.Main;
import org.leaf.api.exceptions.InvalidatedKeyException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

public class Request {
    private static final String v1_BASE_URL = "https://api.policeroleplay.community/v1";
    private static final String v2_BASE_URL = "https://api.policeroleplay.community/v2/server";

    public Context context;
    public boolean invalidatedAPIKey = false;
    public ConnectionMethod method;

    public int returnCode;
    public String body;
    public String url;
    public Duration latency;

    public Request(Context context, String endpoint, boolean v2, ConnectionMethod method) {
        this.url = (v2? v2_BASE_URL : v1_BASE_URL) + endpoint;
        this.context = context;
    }

    public void send() throws IOException, InvalidatedKeyException {
        Instant start = Instant.now();

        final HttpURLConnection conn = setupConnection(method.toString());

        int responseCode = conn.getResponseCode();
        InputStream stream = (responseCode < 400) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        if (responseCode >= 400) {
            Main.logger.severe("HTTP Error " + responseCode + ": " + response.toString());

            if (responseCode == 403) {
                invalidatedAPIKey = true;
                Main.logger.severe("Invalidated server API Key!");
                throw new InvalidatedKeyException("This API key has been invalidated!");
            }

            throw new IOException("HTTP Error " + responseCode + ": " + response.toString());
        }

        Duration latency = Duration.between(start, Instant.now());

        this.returnCode = responseCode;
        this.body = response.toString();
        this.latency = latency;
    }


    private HttpURLConnection setupConnection(String method) throws IOException {
        URL url = URI.create(this.url).toURL();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Server-Key", context.getKey());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);

        if (method.equals("POST")) {
            conn.setDoOutput(true);
        }

        return conn;
    }
}
