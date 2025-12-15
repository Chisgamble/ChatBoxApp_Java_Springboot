package com.example.api.admin;

import com.example.dto.LoginLogDTO;
import com.example.dto.LoginLogListDTO;
import com.example.util.HttpClientUtil;

import java.util.List;
import java.util.StringJoiner;

public class LoginLogApi {
    private static final String BASE_URL = "http://localhost:8080/api/login-log";

    public LoginLogListDTO getAllLogs(List<String> emails, List<String> usernames, String status, String order) {
        StringBuilder url = new StringBuilder(BASE_URL + "/getall?");

        // 1. Append Order (Default to asc if null)
        url.append("order=").append(order != null ? order : "asc");
        // 1. Append Order (Default to asc if null)
        url.append("&status=").append(status != null ? status : "all");

        // 2. Append Emails (comma separated: email=a,b,c)
        if (emails != null && !emails.isEmpty()) {
            StringJoiner joiner = new StringJoiner(",");
            for (String email : emails) joiner.add(email);
            url.append("&email=").append(joiner.toString());
        }

        // 3. Append Usernames (comma separated)
        if (usernames != null && !usernames.isEmpty()) {
            StringJoiner joiner = new StringJoiner(",");
            for (String username : usernames) joiner.add(username);
            url.append("&username=").append(joiner.toString());
        }

        // 4. Send Request (Expect LoginLogListDTO response)
        return HttpClientUtil.get(url.toString(), LoginLogListDTO.class);
    }

    public LoginLogListDTO getLogsByEmail(String email) {
        String url = BASE_URL + "/find-by-email?email=" + email;
        // Use .get(), not .getList()
        return HttpClientUtil.get(url, LoginLogListDTO.class);
    }
}