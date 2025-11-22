package components.user;

import model.User;

import javax.swing.*;

import java.util.List;

public class StrangerCardList extends JScrollPane {
    public StrangerCardList(List<User> users, int width) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        for (User u : users){
            list.add(new StrangerCard(u, width));
        }

        this.setOpaque(false);
        this.getViewport().setOpaque(false);
        this.setViewportView(list);
    }
}

