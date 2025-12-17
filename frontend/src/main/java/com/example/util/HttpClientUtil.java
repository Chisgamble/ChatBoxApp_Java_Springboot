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
            System.out.println("DEBUG URL: " + url);
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

//    public static <T> T delete(String url, Class<T> responseType) {
//        try {
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI(url))
//                    .DELETE()
//                    .build();
//
//            System.out.println("DEBUG DELETE URL: " + url);
//
//            HttpResponse<String> response =
//                    client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            System.out.println("DEBUG Response Code: " + response.statusCode());
//            System.out.println("DEBUG Response Body: " + response.body());
//
//            int status = response.statusCode();
//            if (status < 200 || status >= 300) {
//                throw new RuntimeException("HTTP " + status + ": " + response.body());
//            }
//
//            return JsonUtil.fromJson(response.body(), responseType);
//
//        } catch (Exception e) {
//            throw new RuntimeException("DELETE request failed: " + e.getMessage());
//        }
//    }

    public static <T> T deleteJson(
            String url,
            Object body,              // có thể null
            Class<T> responseType
    ) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json");

            if (body == null) {
                // DELETE không body
                builder.method("DELETE", HttpRequest.BodyPublishers.noBody());
            } else {
                String jsonBody = JsonUtil.toJson(body);
                System.out.println("DEBUG DELETE BODY: " + jsonBody);

                builder.method(
                        "DELETE",
                        HttpRequest.BodyPublishers.ofString(jsonBody)
                );
            }

            System.out.println("DEBUG DELETE URL: " + url);

            HttpResponse<String> response =
                    client.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            System.out.println("DEBUG Response Code: " + response.statusCode());
            System.out.println("DEBUG Response Body: " + response.body());

            int status = response.statusCode();
            String responseBody = response.body();
            if (status < 200 || status >= 300) {
                throw new RuntimeException("HTTP " + status + ": " + response.body());
            }
            if (status == 204 || responseBody == null || responseBody.trim().isEmpty()) {
                return null;
            }

            return JsonUtil.fromJson(response.body(), responseType);

        } catch (Exception e) {
            throw new RuntimeException("DELETE request failed: " + e.getMessage(), e);
        }
    }

    public static <T> T putJson(String url, Object body, Class<T> responseType) {
        try {
            String jsonBody = JsonUtil.toJson(body);

            System.out.println("DEBUG PUT URL: " + url);
            System.out.println("DEBUG Request Body: " + jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("DEBUG Response Code: " + response.statusCode());
            System.out.println("DEBUG Response Body: " + response.body());

            int status = response.statusCode();
            if (status < 200 || status >= 300) {
                throw new RuntimeException("HTTP " + status + ": " + response.body());
            }

            return JsonUtil.fromJson(response.body(), responseType);

        } catch (Exception e) {
            throw new RuntimeException("PUT request failed: " + e.getMessage(), e);
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
