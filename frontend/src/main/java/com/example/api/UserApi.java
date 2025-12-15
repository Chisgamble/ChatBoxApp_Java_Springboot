package com.example.api;

import com.example.dto.FriendCardDTO;
import com.example.dto.GroupCardDTO;
import com.example.dto.MsgDTO;
import com.example.dto.response.FriendRequestResDTO;
import com.example.dto.response.GeneralResDTO;
import com.example.dto.response.StrangerCardResDTO;
import com.example.dto.response.UserCardResDTO;
import com.example.util.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class UserApi {
    private static final String BASE_URL = "http://localhost:8080/api/users";

    public List<FriendCardDTO> getAllFriends(Long userId){
        String url = BASE_URL + "/" + userId.toString() + "/friends";
        List<FriendCardDTO> response = HttpClientUtil.get(url, new TypeReference<List<FriendCardDTO>>() {});
        return response;
    }

    public List<GroupCardDTO> getAllGroups(Long userId){
        String url = BASE_URL + "/" + userId.toString() + "/groups";
        List<GroupCardDTO> response = HttpClientUtil.get(url, new TypeReference<List<GroupCardDTO>>() {});
        return response;
    }

    public List<FriendRequestResDTO> getAllFriendRequests(Long userId){
        String url = BASE_URL + "/" + userId.toString() + "/friend-requests";
        List<FriendRequestResDTO> response = HttpClientUtil.get(url, new TypeReference<List<FriendRequestResDTO>>() {});
        return response;
    }

    public List<StrangerCardResDTO> getAllStrangerCards(Long userId){
        String url = BASE_URL + "/" + userId.toString() + "/strangers";
        List<StrangerCardResDTO> response = HttpClientUtil.get(url, new TypeReference<List<StrangerCardResDTO>>() {});
        return response;
    }

    public GeneralResDTO blockUser(Long targetUserId) {
        String url = BASE_URL + "/" + targetUserId.toString() + "/block";
        GeneralResDTO res = HttpClientUtil.postJson(url, null, GeneralResDTO.class);
        return res;
    }

    public GeneralResDTO reportSpam(Long targetUserId) {
        String url = BASE_URL + "/" + targetUserId.toString() + "/report-spam";
        GeneralResDTO res = HttpClientUtil.postJson(url, null, GeneralResDTO.class);
        return res;
    }

    public List<MsgDTO> getAllRelatedMessages(){
        String url = BASE_URL + "/messages/me";
        List<MsgDTO> res = HttpClientUtil.get(url, new TypeReference<List<MsgDTO>>() {});
        return res;
    }
}
