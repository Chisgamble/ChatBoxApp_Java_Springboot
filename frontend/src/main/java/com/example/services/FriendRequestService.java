package com.example.services;

import com.example.api.FriendRequestApi;
import com.example.dto.FriendCardDTO;
import com.example.dto.request.UpdateFriendRequestReqDTO;
import com.example.dto.response.FriendRequestResDTO;
import com.example.dto.response.UpdateFriendRequestResDTO;

import java.util.List;

public class FriendRequestService {
    private final FriendRequestApi api = new FriendRequestApi();

    public List<FriendRequestResDTO> getAll (long id){
        return api.getAllFriendRequests(id);
    }

    public UpdateFriendRequestResDTO updateFriendRequest (FriendRequestResDTO request, long userId, String status){
        UpdateFriendRequestReqDTO req = new UpdateFriendRequestReqDTO(userId, request.getSenderId(), status);
        return api.updateFriendRequest(req, request.getId());
    }
}
