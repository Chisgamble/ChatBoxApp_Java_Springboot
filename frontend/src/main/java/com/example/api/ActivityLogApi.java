package com.example.api;

import com.example.dto.ActivityListDTO;
import com.example.util.HttpClientUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ActivityLogApi {
    private static final String BASE_URL = "http://localhost:8080/api/activity";

    public ActivityListDTO getAllUserActivity(
            List<String> usernameFilter,
            String activityType,
            String comparison,
            String count,
            String sortBy,
            String sortDir
    ) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/getall/data?");

        try {
            if (usernameFilter != null && !usernameFilter.isEmpty()) {
                String joinedNames = String.join(",", usernameFilter);
                String encodedUsername = URLEncoder.encode(joinedNames, StandardCharsets.UTF_8);
                urlBuilder.append("usernameFilter=").append(encodedUsername).append("&");
            }
            if (activityType != null && !activityType.isEmpty()) {
                String encodedActivityType = URLEncoder.encode(activityType, StandardCharsets.UTF_8);
                urlBuilder.append("activityType=").append(encodedActivityType).append("&");
            }
            if (comparison != null && !comparison.isEmpty()) {
                // CRITICAL: Must encode comparison symbols (<, >, =)
                String encodedComparison = URLEncoder.encode(comparison, StandardCharsets.UTF_8.toString());
                urlBuilder.append("comparison=").append(encodedComparison).append("&");
            }
            if (count != null && !count.isEmpty()) {
                String encodedCount = URLEncoder.encode(count, StandardCharsets.UTF_8.toString());
                urlBuilder.append("count=").append(encodedCount).append("&");
            }

            // Standard sort parameters
            if (sortBy != null && !sortBy.isEmpty()) {
                String encodedSortBy = URLEncoder.encode(sortBy, StandardCharsets.UTF_8.toString());
                urlBuilder.append("sortBy=").append(encodedSortBy).append("&");
            }
            if (sortDir != null && !sortDir.isEmpty()) {
                String encodedSortDir = URLEncoder.encode(sortDir, StandardCharsets.UTF_8.toString());
                urlBuilder.append("sortDir=").append(encodedSortDir).append("&");
            }

        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("URL Encoding failed for Activity API parameters", e);
        }

        String finalUrl = urlBuilder.toString();
        if (finalUrl.endsWith("&")) {
            finalUrl = finalUrl.substring(0, finalUrl.length() - 1);
        }

        return HttpClientUtil.get(finalUrl, ActivityListDTO.class);
    }
}