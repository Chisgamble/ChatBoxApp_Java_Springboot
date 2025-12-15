package com.example.ui;

import com.example.dto.UserMiniDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatContext {
    private boolean isGroup;
    private Long inboxId;     // for 1-1
    private Long groupId;     // for group
    private UserMiniDTO targetUser;
}
