package components.user;

import dto.FriendCardDTO;
import model.User;

import javax.swing.*;

import java.util.List;

import renderer.FriendCardRenderer;

public class FriendCardList extends JScrollPane {
    public FriendCardList(List<FriendCardDTO> users, int width) {
        DefaultListModel<FriendCardDTO> model = new DefaultListModel<>();
        users.forEach(model::addElement);

        JList<FriendCardDTO> list = new JList<>(model);
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
