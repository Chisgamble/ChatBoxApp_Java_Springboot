package com.example.api;

import com.example.dto.FriendCardDTO;
import com.example.dto.FriendCardListDTO;
import com.example.util.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class FriendApi {
    private static final String BASE_URL = "http://localhost:8080/api/friend";

    public List<FriendCardDTO> getAllFriends(Long userId){
        String url = BASE_URL + "/" + userId.toString();
        List<FriendCardDTO> response = HttpClientUtil.get(url, new TypeReference<List<FriendCardDTO>>() {});
        return response;
    }
}
