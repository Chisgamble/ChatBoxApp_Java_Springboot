package com.example.api.admin;

import com.example.dto.UserListDTO;
import com.example.dto.request.AdminCreateOrUpdateUserReqDTO;
import com.example.util.HttpClientUtil;

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
        // Body is null because the backend doesn't expect a @RequestBody
        HttpClientUtil.putJson(url, null, Void.class);
    }

    public void deleteUser(Long id) {
        String url = BASE_URL + "/" + id;
        HttpClientUtil.delete(url);
    }
}
