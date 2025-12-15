package com.example.components.user;

import com.example.dto.MsgDTO;
import com.example.renderer.GlobalMsgCardRenderer;

import javax.swing.*;
import java.util.List;

public class GlobalMsgCardList extends JScrollPane {

    private final JList<MsgDTO> list;

    public GlobalMsgCardList(List<MsgDTO> msgs, int width) {
        DefaultListModel<MsgDTO> model = new DefaultListModel<>();
        msgs.forEach(model::addElement);

        list = new JList<>(model);
        list.setCellRenderer(new GlobalMsgCardRenderer());
        list.setFixedCellHeight(70);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        setViewportView(list);
        setBorder(null);
        setOpaque(false);
        getViewport().setOpaque(false);
    }

    public JList<MsgDTO> getList() {
        return list;
    }

    public void updateList(List<MsgDTO> newMsgs) {
        DefaultListModel<MsgDTO> model =
                (DefaultListModel<MsgDTO>) list.getModel();
        model.clear();
        newMsgs.forEach(model::addElement);
    }
}
