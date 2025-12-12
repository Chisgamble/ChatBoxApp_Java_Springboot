package com.example.components.user;

import com.example.dto.response.FriendRequestResDTO;

import javax.swing.*;

import java.util.List;

import com.example.listener.FriendRequestListener;

public class FriendRequestList extends JScrollPane {

    JPanel listPanel;
    List<FriendRequestResDTO> data;

    public FriendRequestList(List<FriendRequestResDTO> users,
                                 int width,
                                 FriendRequestListener listener) {

        this.data = users;

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        for (FriendRequestResDTO u : users) {
            listPanel.add(new FriendRequestCard(u, width, listener, this));
        }

        this.setViewportView(listPanel);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }

    // helper method để xóa item khỏi UI
    public void removeCard(FriendRequestCard card) {
        listPanel.remove(card);
        listPanel.revalidate();
        listPanel.repaint();
    }
}

