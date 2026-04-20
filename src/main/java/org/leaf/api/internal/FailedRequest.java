package org.leaf.api.internal;

import java.time.Duration;

public record FailedRequest (
        Context context,
        boolean invalidatedAPIKey,
        ConnectionMethod method,
        int returnCode,
        String body,
        String url,
        Duration latency,
        QueryType queryType
) {

    public static FailedRequest from(Request req) {
        return new FailedRequest(
                req.context,
                req.invalidatedAPIKey,
                req.method,
                req.returnCode,
                req.body,
                req.url,
                req.latency,
                req.queryType
        );
    }
}
