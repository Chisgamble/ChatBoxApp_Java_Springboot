package com.example.components.user;

import com.example.dto.MsgDTO;
import com.example.renderer.GlobalMsgCardRenderer;
import com.example.ui.ChatScreen;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GlobalMsgCardList extends JScrollPane {

    private final JList<MsgDTO> list;
    ChatScreen mainFrame;

    public GlobalMsgCardList(List<MsgDTO> msgs, int width, ChatScreen mainFrame) {
        this.mainFrame = mainFrame;

        DefaultListModel<MsgDTO> model = new DefaultListModel<>();
        msgs.forEach(model::addElement);

        list = new JList<>(model);
        list.setCellRenderer(new GlobalMsgCardRenderer());
        list.setFixedCellHeight(70);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index != -1) {
                        list.setSelectedIndex(index);
                        showPopupMenu(e);
                    }
                }
            }
        });

        setViewportView(list);
        setBorder(null);
        setOpaque(false);
        getViewport().setOpaque(false);
    }

    private void showPopupMenu(MouseEvent e) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem jumpItem = new JMenuItem("Jump to message");

        jumpItem.addActionListener(al -> {
            MsgDTO selectedMsg = list.getSelectedValue();
            if (selectedMsg != null) {
                // Gọi hàm xử lý nhảy toàn cục trong ChatScreen
                mainFrame.jumpToGlobalMessage(selectedMsg);
            }
        });

        popup.add(jumpItem);
        popup.show(list, e.getX(), e.getY());
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
