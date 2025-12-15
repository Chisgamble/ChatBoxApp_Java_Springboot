package com.example.services;

import com.example.api.UserApi;
import com.example.dto.FriendCardDTO;
import com.example.dto.response.FriendRequestResDTO;
import com.example.dto.response.StrangerCardResDTO;
import com.example.dto.response.UserCardResDTO;

import java.util.List;

public class UserService {
    private final UserApi api = new UserApi();

    public List<FriendCardDTO> getAllFriends (Long id){
        return api.getAllFriends(id);
    }

    public List<FriendRequestResDTO> getAllFriendRequests(long id){
        return api.getAllFriendRequests(id);
    }

    public List<StrangerCardResDTO> getAllStrangerCards(long userId){
        return api.getAllStrangerCards(userId);
    }
}
