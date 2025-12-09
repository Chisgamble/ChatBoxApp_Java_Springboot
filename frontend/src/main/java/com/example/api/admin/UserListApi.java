package com.example.api.admin;

import com.example.dto.UserListDTO;
import com.example.util.HttpClientUtil;

public class UserListApi {
    private static final String BASE_URL = "http://localhost:8080/api/users";

    public UserListDTO getAllUsers(){
        String url = BASE_URL + "/getall/data";
        UserListDTO response = HttpClientUtil.get(url, UserListDTO.class);
        return response;
    }
}
