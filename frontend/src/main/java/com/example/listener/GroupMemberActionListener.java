package com.example.listener;

import java.util.List;

public interface GroupMemberActionListener {
    void onAddMembers(Long groupId, List<Long> userIds);
    void onRemoveMember(Long groupId, Long userId);
    void onPromoteMember(Long groupId, Long userId);
}