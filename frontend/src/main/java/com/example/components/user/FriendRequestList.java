package com.example.components.user;

import com.example.model.User;

import javax.swing.*;

import java.util.List;

import com.example.renderer.FriendRequestCardRenderer;

public class FriendRequestList extends JScrollPane {
    public FriendRequestList(List<User> users, int width) {
        DefaultListModel<User> model = new DefaultListModel<>();
        users.forEach(model::addElement);

        JList<User> list = new JList<>(model);
        list.setCellRenderer(new FriendRequestCardRenderer(width));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        list.setOpaque(false);
        list.setFixedCellHeight(70); // slight extra padding
        list.setBorder(null);

        this.setViewportView(list);
        this.setBorder(null);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }
}
