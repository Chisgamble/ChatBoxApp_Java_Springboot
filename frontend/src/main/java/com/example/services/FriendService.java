package com.example.services;


import com.example.api.FriendApi;
import com.example.dto.FriendCardDTO;
import com.example.dto.FriendListDataDTO;
import com.example.dto.response.GeneralResDTO;


import java.util.List;

public class FriendService {
    private final FriendApi api = new FriendApi();

    public List<FriendCardDTO> getAll (Long id){
        return api.getAllFriends(id);
    }

    public List<FriendCardDTO> getAllById (Long id){
        return api.getAllFriendsById(id);
    }

    public String deleteFriend(Long friendId) {
        return api.deleteFriend(friendId).message();
    }

    public List<FriendListDataDTO> getFriendListData(String username, String sortBy, String sortDir, String fcSymbol, Integer fcVal) {
        return api.getFriendListData(username, sortBy, sortDir, fcSymbol, fcVal);
    }
}
