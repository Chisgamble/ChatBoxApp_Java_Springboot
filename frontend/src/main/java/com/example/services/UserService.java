package com.example.services;

import com.example.api.UserApi;
import com.example.dto.FriendCardDTO;
import com.example.dto.GroupCardDTO;
import com.example.dto.response.FriendRequestResDTO;
import com.example.dto.response.StrangerCardResDTO;
import com.example.dto.response.UserCardResDTO;

import java.util.List;

public class UserService {
    private final UserApi api = new UserApi();

    public List<FriendCardDTO> getAllFriends (Long userId){
        return api.getAllFriends(userId);
    }

    public List<GroupCardDTO> getAllGroups (Long userId){return api.getAllGroups(userId);}

    public List<FriendRequestResDTO> getAllFriendRequests(long userId){
        return api.getAllFriendRequests(userId);
    }

    public List<StrangerCardResDTO> getAllStrangerCards(long userId){
        return api.getAllStrangerCards(userId);
    }
}
