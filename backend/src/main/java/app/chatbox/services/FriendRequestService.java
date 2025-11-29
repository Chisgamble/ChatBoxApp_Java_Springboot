package app.chatbox.services;

import app.chatbox.dto.response.FriendRequestResDTO;

import java.util.List;

public interface FriendRequestService {
    List<FriendRequestResDTO> getIncomingRequests(Long userId);
}
