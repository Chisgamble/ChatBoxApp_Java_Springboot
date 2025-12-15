package com.example.listener;

import java.util.List;

public interface CreateGroupListener {
    void onGroupCreated(String groupName, List<Long> memberIds);
}
