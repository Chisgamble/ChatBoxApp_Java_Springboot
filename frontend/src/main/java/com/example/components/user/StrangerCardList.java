package com.example.components.user;

import com.example.dto.response.StrangerCardResDTO;
import com.example.listener.FriendRequestListener;

import javax.swing.*;

import java.util.List;

public class StrangerCardList extends JScrollPane {
    public StrangerCardList(List<StrangerCardResDTO> users, int width, FriendRequestListener listener) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        for (StrangerCardResDTO u : users){
            list.add(new StrangerCard(u, width, listener));
        }

        this.setOpaque(false);
        this.getViewport().setOpaque(false);
        this.setViewportView(list);
    }
}

