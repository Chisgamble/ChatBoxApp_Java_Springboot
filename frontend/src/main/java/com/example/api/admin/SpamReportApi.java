package com.example.api.admin;

import com.example.dto.SpamReportListDTO;
import com.example.util.HttpClientUtil;

import java.time.LocalDate;
import java.util.Map;

public class SpamReportApi {

    private static final String BASE_URL = "http://localhost:8080/api/spam-reports";

    // GET ALL with Filters
    public SpamReportListDTO getAll(
            String email,
            String username,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            String sort,
            String order
    ) {
        StringBuilder url = new StringBuilder(BASE_URL + "/getall?");

        if (email != null && !email.isBlank()) {
            url.append("email=").append(email).append("&");
        }
        if (username != null && !username.isBlank()) {
            url.append("username=").append(username).append("&");
        }
        if (startDate != null) {
            url.append("startDate=").append(startDate).append("&");
        }
        if (endDate != null) {
            url.append("endDate=").append(endDate).append("&");
        }
        if (status != null && !status.isBlank()) {
            url.append("status=").append(status).append("&");
        }

        url.append("sort=").append(sort).append("&");
        url.append("order=").append(order);

        return HttpClientUtil.get(url.toString(), SpamReportListDTO.class);
    }

    // UPDATE STATUS
    public void updateStatus(Long reportId, String newStatus) {
        String url = BASE_URL + "/update-status/" + reportId + "?status=" + newStatus;

        // Change String.class to Map.class
        HttpClientUtil.putJson(url, null, Map.class);
    }
}