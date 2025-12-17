package com.example.ui;

import com.example.dto.FriendCardDTO;
import com.example.dto.GroupCardDTO;
import com.example.dto.GroupMemberDTO;
import com.example.dto.UserMiniDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatContext {
    private UserMiniDTO thisUser;
    GroupMemberDTO userInGroup;
    private boolean isGroup;
    private Long inboxId;     // for 1-1
    private Long groupId;     // for group
    private FriendCardDTO targetUser;
    private GroupCardDTO targetGroup;
}
