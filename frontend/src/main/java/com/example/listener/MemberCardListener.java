package com.example.listener;

import com.example.model.User;

public interface MemberCardListener {
    void onPromote(User user);
    void onRemove(User user);
}

