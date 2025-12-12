package com.example.listener;

import com.example.dto.FriendCardDTO;
import com.example.dto.response.FriendRequestResDTO;

public interface FriendRequestListener {
    void onAccept(FriendRequestResDTO request);
    void onReject(FriendRequestResDTO request);
}
