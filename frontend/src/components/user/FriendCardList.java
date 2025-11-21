package components.user;

import model.User;

import javax.swing.*;

import java.util.List;

import renderer.FriendCardRenderer;

public class FriendCardList extends JScrollPane {
    public FriendCardList(List<User> users, int width) {
        DefaultListModel<User> model = new DefaultListModel<>();
        users.forEach(model::addElement);

        JList<User> list = new JList<>(model);
        list.setCellRenderer(new FriendCardRenderer(width));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setOpaque(false);
        list.setFixedCellHeight(70); // slight extra padding
        list.setBorder(null);

        this.setViewportView(list);
        this.setBorder(null);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }
}
