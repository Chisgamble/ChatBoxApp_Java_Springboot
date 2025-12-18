package com.example.listener;

import com.example.dto.UserDTO;

public interface ProfileListener {
    void onProfileUpdated(UserDTO updatedUser);
}
