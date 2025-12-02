package util;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtil {

    private static CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    private static final HttpClient client = HttpClient.newBuilder()
                                            .cookieHandler(cookieManager)
                                            .build();

    // GET request returning parsed JSON
    public static <T> T get(String url, Class<T> responseType) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

            HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

            return JsonUtil.fromJson(response.body(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("GET request failed", e);
        }
    }

    // POST JSON body and parse response to object
    public static <T> T postJson(String url, Object body, Class<T> responseType) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JsonUtil.toJson(body)))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return JsonUtil.fromJson(response.body(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("POST request failed", e);
        }
    }

    public static void resetCookieManager() {
        cookieManager.getCookieStore().removeAll();
        cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);  // Create a new CookieManager
        System.out.println("CookieManager has been reset.");
    }

    // Get the current HttpClient (for making requests)
    public static HttpClient getClient() {
        return client;
    }
}
