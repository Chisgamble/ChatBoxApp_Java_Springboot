package com.example.dto;

public record FriendCardDTO(
    UserMiniDTO user,
    InboxMsgDTO lastMsg
){}
