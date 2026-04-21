package org.leaf.api.internal;

import org.leaf.Main;
import org.leaf.api.exceptions.InvalidatedKeyException;
import org.leaf.utils.LERLCLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

    public QueryType queryType = QueryType.All;

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
