package com.example.components.user;

import com.example.model.User;

import javax.swing.*;

import java.util.List;

public class MemberCardList extends JScrollPane {
    public MemberCardList(List<User> users, int width, boolean isAdmin) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        for (User u : users){
            list.add(new MemberCard(u, width, isAdmin));
        }

        this.setOpaque(false);
        this.getViewport().setOpaque(false);
        this.setViewportView(list);
    }
}

