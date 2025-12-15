package com.example.listener;

import com.example.components.user.GroupCard;
import com.example.dto.GroupCardDTO;

@FunctionalInterface
public interface GroupCardListener {
    void onGroupSelected(GroupCard card, GroupCardDTO group);
}

