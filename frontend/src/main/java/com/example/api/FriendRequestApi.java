package com.example.api;

import com.example.dto.request.CreateFriendRequestReqDTO;
import com.example.dto.request.UpdateFriendRequestReqDTO;
import com.example.dto.response.CreateFriendRequestResDTO;
import com.example.dto.response.FriendRequestResDTO;
import com.example.dto.response.UpdateFriendRequestResDTO;
import com.example.util.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class FriendRequestApi {
    private static final String BASE_URL = "http://localhost:8080/api/friend-requests";


    public CreateFriendRequestResDTO createFriendRequest(CreateFriendRequestReqDTO req){
        String url = BASE_URL;
        CreateFriendRequestResDTO response = HttpClientUtil.postJson(url, req, CreateFriendRequestResDTO.class);
        return response;
    }

    public List<FriendRequestResDTO> getAllFriendRequests(Long userId){
        String url = BASE_URL + "/" + userId.toString();
        List<FriendRequestResDTO> response = HttpClientUtil.get(url, new TypeReference<List<FriendRequestResDTO>>() {});
        return response;
    }

    public UpdateFriendRequestResDTO updateFriendRequest(UpdateFriendRequestReqDTO req, Long requestId){
        String url = BASE_URL + "/" + requestId.toString();
        UpdateFriendRequestResDTO response = HttpClientUtil.postJson(url, req, UpdateFriendRequestResDTO.class);
        return response;
    }
}
