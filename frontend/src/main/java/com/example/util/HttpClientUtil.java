package com.example.util;

import com.fasterxml.jackson.core.type.TypeReference;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpClientUtil {

    private static CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    private static final HttpClient client = HttpClient.newBuilder()
                                            .cookieHandler(cookieManager)
                                            .build();

    public static <T> T get(
            String url,
            TypeReference<T> typeRef
    ) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DEBUG Response Body: " + response.body());

            int status = response.statusCode();
            System.out.println("DEBUG Response Code: " + response.statusCode());
            if (status < 200 || status >= 300) {
                throw new RuntimeException("HTTP " + status + ": " + response.body());
            }

            return JsonUtil.fromJson(response.body(), typeRef);

        } catch (Exception e) {
            throw new RuntimeException("GET request failed: " + e.getMessage());
        }
    }

    // GET request returning parsed JSON
    public static <T> T get(String url, Class<T> responseType) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

            System.out.println("DEBUG URL: " + url);

            HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DEBUG Response Body: " + response.body());

            System.out.println("DEBUG Response Code: " + response.statusCode());

            return JsonUtil.fromJson(response.body(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("GET request failed - HTTPUtil.get Class: ", e);
        }
    }

    // POST JSON body and parse response to object
    public static <T> T postJson(String url, Object body, Class<T> responseType) {
        try {
            String jsonBody = JsonUtil.toJson(body);
            System.out.println("DEBUG URL: " + url);
            System.out.println("DEBUG Request Body: " + jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JsonUtil.toJson(body)))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DEBUG Response Body: " + response.body());

            int status = response.statusCode();
            System.out.println("DEBUG Response Code: " + response.statusCode());

            if (status < 200 || status >= 300) {
                throw new RuntimeException("HTTP " + status + ": " + response.body());
            }

            return JsonUtil.fromJson(response.body(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("POST request failed : " + e.getMessage());
        }
    }

    public static <T> T postJson(String url, Object body, TypeReference<T> typeRef) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JsonUtil.toJson(body)))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return JsonUtil.fromJson(response.body(), typeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static void resetCookieManager() {
        cookieManager.getCookieStore().removeAll();
        System.out.println("CookieManager has been reset.");
    }

    public static <T> List<T> getList(String url, Class<T> responseType) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return JsonUtil.fromJsonList(response.body(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("GET request failed", e);
        }
    }


    // Get the current HttpClient (for making requests)
    public static HttpClient getClient() {
        return client;
    }
}
