package util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientUtil {

    private static final HttpClient client = HttpClient.newHttpClient();

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
}
