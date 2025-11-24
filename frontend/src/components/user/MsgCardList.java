package components.user;

import model.Msg;

import javax.swing.*;

import java.util.List;

import renderer.MsgCardRenderer;

public class MsgCardList extends JScrollPane {
    private final JList<Msg> list;

    public MsgCardList(List<Msg> msgs, int width) {
        DefaultListModel<Msg> model = new DefaultListModel<>();
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

    public JList<Msg> getList() {
        return list;
    }

    public void updateList(List<Msg> newMsgs) {
        DefaultListModel<Msg> model = (DefaultListModel<Msg>) list.getModel();
        model.clear();
        newMsgs.forEach(model::addElement);
    }
}


