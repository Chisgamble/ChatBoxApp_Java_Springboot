package components.user;

import model.Msg;

import javax.swing.*;

import java.util.List;

import renderer.MsgCardRenderer;

public class MsgCardList extends JScrollPane {
    public MsgCardList(List<Msg> msgs, int width) {
        DefaultListModel<Msg> model = new DefaultListModel<>();
        msgs.forEach(model::addElement);

        JList<Msg> list = new JList<>(model);
        list.setCellRenderer(new MsgCardRenderer(width));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        list.setOpaque(false);
        list.setFixedCellHeight(70); // slight extra padding
        list.setBorder(null);

        this.setViewportView(list);
        this.setBorder(null);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }

}

