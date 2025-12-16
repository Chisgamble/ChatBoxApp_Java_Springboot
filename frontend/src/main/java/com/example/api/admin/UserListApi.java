package com.example.api.admin;

import com.example.dto.NewUserListDTO;
import com.example.dto.UserListDTO;
import com.example.dto.YearlyGraphDTO;
import com.example.dto.request.AdminCreateOrUpdateUserReqDTO;
import com.example.util.HttpClientUtil;

import java.time.LocalDate;
import java.util.List;

public class UserListApi {
    private static final String BASE_URL = "http://localhost:8080/api/users";

    public UserListDTO getAllUsers(List<String> username, List<String> name, String status, String sort, String order) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/getall/data?");

        if (username != null && !username.isEmpty()) {
            urlBuilder.append("username=").append(String.join(",", username)).append("&");
        }

        if (name != null && !name.isEmpty()) {
            urlBuilder.append("name=").append(String.join(",", name)).append("&");
        }

        if (status != null && !status.isEmpty()) {
            urlBuilder.append("status=").append(status).append("&");
        }

        if (sort != null && !sort.isEmpty()) {
            urlBuilder.append("sort=").append(sort).append("&");
        }

        if (order != null && !order.isEmpty()) {
            urlBuilder.append("order=").append(order).append("&");
        }

        String finalUrl = urlBuilder.toString();
        if (finalUrl.endsWith("&") || finalUrl.endsWith("?")) {
            finalUrl = finalUrl.substring(0, finalUrl.length() - 1);
        }

        return HttpClientUtil.get(finalUrl, UserListDTO.class);
    }

    public void createUser(AdminCreateOrUpdateUserReqDTO req) {
        String url = BASE_URL + "/create";
        // Assuming your HttpClientUtil can handle void/null returns or returns the new object
        HttpClientUtil.postJson(url, req, Object.class);
    }

    public void updateUser(Long id, AdminCreateOrUpdateUserReqDTO req) {
        String url = BASE_URL + "/update/" + id;
        HttpClientUtil.putJson(url, req, Object.class);
    }

    public void setLockStatus(Long id, boolean locked) {
        String url = BASE_URL + "/lock/" + id + "?locked=" + locked;

        // Change Void.class to Map.class so the parser is happy
        HttpClientUtil.putJson(url, null, java.util.Map.class);
    }

    public void deleteUser(Long id) {
        String url = BASE_URL + "/" + id;
        HttpClientUtil.deleteJson(url, null, String.class);
    }

    public NewUserListDTO getNewUserList(String username, String email, LocalDate start, LocalDate end, String order) {
        StringBuilder url = new StringBuilder(BASE_URL + "/new-list?");

        // Append filters if they exist
        if (username != null && !username.isEmpty()) {
            url.append("username=").append(username).append("&");
        }
        if (email != null && !email.isEmpty()) {
            url.append("email=").append(email).append("&");
        }
        if (start != null) {
            url.append("startDate=").append(start.toString()).append("&");
        }
        if (end != null) {
            url.append("endDate=").append(end.toString()).append("&");
        }

        // Sorting (Default to 'desc' if logic fails, though service handles this)
        url.append("order=").append((order != null) ? order : "desc");

        // Execute GET request expecting the Wrapper DTO
        return HttpClientUtil.get(url.toString(), NewUserListDTO.class);
    }

    public YearlyGraphDTO getNewUserGraph(Integer year) {
        String url = BASE_URL + "/new-graph";
        if (year != null) {
            url += "?year=" + year;
        }
        return HttpClientUtil.get(url, YearlyGraphDTO.class);
    }
}
