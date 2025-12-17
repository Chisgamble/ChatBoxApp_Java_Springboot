package com.example.listener;

import com.example.components.user.GroupCard;
import com.example.dto.GroupCardDTO;

public interface GroupListener {
    void onGroupNameChanged(String newName);
}
