package com.example.components.user;

import com.example.dto.GroupMemberDTO;
import com.example.listener.GroupMemberActionListener;
import com.example.model.User;
import com.example.util.Utility;

import javax.swing.*;

import java.util.List;

public class MemberCardList extends JScrollPane {
    public MemberCardList(List<GroupMemberDTO> members, int width, boolean isAdmin, GroupMemberActionListener listener) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        for (GroupMemberDTO u : members){
            list.add(new MemberCard(
                    Utility.getInitials(u.getUsername()),
                    u.getUsername(), u.getGroupId(), u.getUserId(), u.getRole(), width, isAdmin, listener));
        }

        this.setOpaque(false);
        this.getViewport().setOpaque(false);
        this.setViewportView(list);
    }
}

