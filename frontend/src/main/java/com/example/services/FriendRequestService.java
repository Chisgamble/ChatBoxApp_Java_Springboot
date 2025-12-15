package com.example.services;

import com.example.api.FriendRequestApi;
import com.example.dto.FriendCardDTO;
import com.example.dto.request.CreateFriendRequestReqDTO;
import com.example.dto.request.UpdateFriendRequestReqDTO;
import com.example.dto.response.CreateFriendRequestResDTO;
import com.example.dto.response.FriendRequestResDTO;
import com.example.dto.response.UpdateFriendRequestResDTO;

import java.util.List;

public class FriendRequestService {
    private final FriendRequestApi api = new FriendRequestApi();

    public CreateFriendRequestResDTO create (Long senderId, Long receiverId){
        return api.createFriendRequest(new CreateFriendRequestReqDTO(senderId, receiverId));
    }

    public List<FriendRequestResDTO> getAll (long id){
        return api.getAllFriendRequests(id);
    }

    public UpdateFriendRequestResDTO updateFriendRequest (FriendRequestResDTO request, long userId, String status){
        UpdateFriendRequestReqDTO req = new UpdateFriendRequestReqDTO(userId, request.getSenderId(), status);
        return api.updateFriendRequest(req, request.getId());
    }
}
