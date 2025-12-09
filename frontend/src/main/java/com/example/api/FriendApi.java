package com.example.api;

import com.example.dto.FriendCardListDTO;
import com.example.util.HttpClientUtil;

public class FriendApi {
    private static final String BASE_URL = "http://localhost:8080/api/friend";

    public FriendCardListDTO getAllFriends(Long userId){
        String url = BASE_URL + "/" + userId.toString();
        FriendCardListDTO response = HttpClientUtil.get(url, FriendCardListDTO.class);
        return response;
    }
}
