package com.example.components.user;

import com.example.dto.BaseMsgDTO;
import com.example.model.Msg;

import javax.swing.*;

import java.util.List;

import com.example.renderer.MsgCardRenderer;

public class MsgCardList extends JScrollPane {
    private final JList<BaseMsgDTO> list;

    public MsgCardList(List<? extends BaseMsgDTO> msgs, int width) {
        DefaultListModel<BaseMsgDTO> model = new DefaultListModel<>();
        msgs.forEach(model::addElement);

        list = new JList<>(model);
        list.setCellRenderer(new MsgCardRenderer());
        list.setFixedCellHeight(60);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        setViewportView(list);
        setBorder(null);
        setOpaque(false);
        getViewport().setOpaque(false);
    }

    public JList<BaseMsgDTO> getList() {
        return list;
    }

    public void updateList(List<? extends BaseMsgDTO> newMsgs) {
        DefaultListModel<BaseMsgDTO> model = (DefaultListModel<BaseMsgDTO>) list.getModel();
        model.clear();
        newMsgs.forEach(model::addElement);
    }
}


