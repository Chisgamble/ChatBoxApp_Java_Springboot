package components;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FriendList extends JPanel {

    public FriendList(List<User> friends, int width) {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (User u : friends) {
            FriendCard card = new FriendCard(u, width-5);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(card);
        }
    }
}
