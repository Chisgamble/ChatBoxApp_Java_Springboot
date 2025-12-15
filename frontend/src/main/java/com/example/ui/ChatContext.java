package com.example.ui;

import com.example.dto.FriendCardDTO;
import com.example.dto.GroupCardDTO;
import com.example.dto.UserMiniDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatContext {
    UserMiniDTO thisUser;
    private boolean isGroup;
    private boolean isAdmin;
    private Long inboxId;     // for 1-1
    private Long groupId;     // for group
    private FriendCardDTO targetUser;
    private GroupCardDTO targetGroup;
}
