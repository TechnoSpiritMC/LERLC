package org.leaf.api.internal;

import org.leaf.Main;
import org.leaf.api.exceptions.InvalidatedKeyException;
import org.leaf.utils.LERLCLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.time.Duration;
import java.time.Instant;

public class Request {
    private static final String v1_BASE_URL = "https://api.erlc.gg/v1";
    private static final String v2_BASE_URL = "https://api.erlc.gg/v2/server";

    public Context context;
    public boolean invalidatedAPIKey = false;
    public ConnectionMethod method;

    public int returnCode;
    public String body;
    public String url;
    public Duration latency;

    public QueryType queryType = QueryType.All;
    public String requestBody = null;

    public String rateLimitBucket = null;
    public int rateLimitRemaining = -1;
    public Instant rateLimitReset = Instant.EPOCH;

    public Request(Context context, String endpoint, boolean v2, ConnectionMethod method) {
        this.url = (v2? v2_BASE_URL : v1_BASE_URL) + endpoint;
        this.context = context;
        this.method = method;
    }

    /// Constructor for API V2 Requests.
    public Request(Context context, ConnectionMethod method, QueryType queries) {
        this.url = v2_BASE_URL;
        this.context = context;
        this.method = method;
        this.queryType = queries;
    }

    public void setCommandRequestBody(String rawCommand) {
        this.requestBody = "{\"command\": \"" + rawCommand +"\"}";
    }

    public void send() throws IOException, InvalidatedKeyException {
        Instant start = Instant.now();

        final HttpURLConnection conn = setupConnection(method.toString());

        if (method == ConnectionMethod.POST && requestBody != null) {
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes(StandardCharsets.UTF_8));
            }
        }

        int responseCode;
        try {
            responseCode = conn.getResponseCode();
        } catch (IOException e) {
            LERLCLogger.error("Failed to establish connection with the API. Perhaps you are not connected with the internet, or has the URL changed..?");
            throw new RuntimeException("Failed to establish connection with the API.", e);
        }

        String bucket = conn.getHeaderField("X-RateLimit-Bucket");
        if (bucket != null) {
            this.rateLimitBucket = bucket;
        }

        String remaining = conn.getHeaderField("X-RateLimit-Remaining");
        if (remaining != null) {
            try {
                this.rateLimitRemaining = Integer.parseInt(remaining);
            } catch (NumberFormatException e) {
                LERLCLogger.getLogger().warning("Failed to parse X-RateLimit-Remaining: " + remaining);
            }
        }

        String reset = conn.getHeaderField("X-RateLimit-Reset");
        if (reset != null) {
            try {
                this.rateLimitReset = Instant.ofEpochSecond(Long.parseLong(reset));
            } catch (NumberFormatException e) {
                LERLCLogger.getLogger().warning("Failed to parse X-RateLimit-Reset: " + reset);
            }
        }

        InputStream stream = (responseCode < 400) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        if (responseCode >= 400) {
            LERLCLogger.getLogger().severe("HTTP Error " + responseCode + ": " + response.toString());
            FailedRequestStore.add(this);

            if (responseCode == 403) {
                invalidatedAPIKey = true;
                LERLCLogger.getLogger().severe("Invalidated server API Key!");
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
        List<String> wanted = queryType.allWanted();
        String queryString = wanted.isEmpty() ? "" :
                "?" + wanted.stream().map(q -> q + "=true").collect(java.util.stream.Collectors.joining("&"));
        URL url = URI.create(this.url + queryString).toURL();

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
