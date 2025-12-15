package com.example.services.admin;

import com.example.api.admin.LoginLogApi;
import com.example.dto.LoginLogDTO;
import com.example.dto.LoginLogListDTO;

import java.util.Collections;
import java.util.List;

public class LoginLogService {
    private final LoginLogApi api = new LoginLogApi();

    public List<LoginLogDTO> getAll(List<String> emails, List<String> usernames, String status, String order) {
        LoginLogListDTO response = api.getAllLogs(emails, usernames, status, order);

        if (response != null && response.logs() != null) {
            return response.logs();
        }
        return Collections.emptyList();
    }

    public List<LoginLogDTO> getByEmail(String email) {
        if (email == null || email.isEmpty()) return Collections.emptyList();

        // 1. Get the Wrapper DTO
        LoginLogListDTO response = api.getLogsByEmail(email);

        // 2. Unwrap and return the list (safe check for null)
        if (response != null && response.logs() != null) {
            return response.logs();
        }

        return Collections.emptyList();
    }
}