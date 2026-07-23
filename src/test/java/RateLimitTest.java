import org.leaf.api.internal.Cache;
import org.leaf.api.internal.ConnectionMethod;
import org.leaf.api.internal.Context;
import org.leaf.api.internal.Request;

import java.io.IOException;

public class RateLimitTest {
    public static void main(String[] args) throws IOException {
        Cache c = new Cache("zeiSYFUkczEeSVsuxJUD-YjqqHSttLLyGgqYVYQOjnTyVxvmYOMSxledjhasb");
        Request request = new Request(new Context("zeiSYFUkczEeSVsuxJUD-YjqqHSttLLyGgqYVYQOjnTyVxvmYOMSxledjhasb"), "/command", true, ConnectionMethod.POST);
        request.setCommandRequestBody(":h Hello!");

        request.send();

        System.out.println("Return Code: " + request.returnCode);
        System.out.println("Response Body: " + request.body);
        System.out.println("Latency: " + request.latency);
        System.out.println("Invalidated API Key?: " + request.invalidatedAPIKey);
        System.out.println("Url: " + request.url);
        System.out.println("Query Type: " + request.queryType);
        System.out.println("Context: " + request.context);
        System.out.println("Request body: " + request.requestBody);
        System.out.println("Connection method: " + request.method);
        System.out.println("Bucket: " + request.rateLimitBucket);
        System.out.println("Remaining: " + request.rateLimitRemaining);
        System.out.println("Reset: " + request.rateLimitReset);
    }
}
