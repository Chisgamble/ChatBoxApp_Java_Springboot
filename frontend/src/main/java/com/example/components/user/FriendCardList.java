package com.example.components.user;

import com.example.dto.FriendCardDTO;

import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
import java.util.List;

import com.example.listener.FriendCardListener;
import com.example.renderer.FriendCardRenderer;

public class FriendCardList extends JScrollPane {
    private final JList<FriendCardDTO> list;
    private int hoverIndex = -1;

    public FriendCardList(List<FriendCardDTO> users, int width, FriendCardListener listener) {
        DefaultListModel<FriendCardDTO> model = new DefaultListModel<>();
        users.forEach(model::addElement);

        list = new JList<>(model);
        FriendCardRenderer renderer = new FriendCardRenderer(width);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setOpaque(false);
        list.setFixedCellHeight(70); // slight extra padding
        list.setBorder(null);

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                FriendCardDTO selected = list.getSelectedValue();
                if (selected != null) {
                    listener.onFriendSelected(selected);
                }
            }
        });

        list.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index != hoverIndex) {
                    hoverIndex = index;
                    list.repaint();
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoverIndex = -1;
                list.repaint();
            }
        });

        // click
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                FriendCardDTO selected = list.getSelectedValue();
                if (selected != null) {
                    listener.onFriendSelected(selected);
                }
            }
        });

        // pass hoverIndex vào renderer
        renderer.setHoverIndexSupplier(() -> hoverIndex);
        renderer.setList(list);

        this.setViewportView(list);
        this.setBorder(null);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }
    public JList<FriendCardDTO> getList() {
        return list;
    }


}
