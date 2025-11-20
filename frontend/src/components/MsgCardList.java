package components;

import model.Msg;
import model.User;

import javax.swing.*;
import java.awt.*;

import java.util.List;

public class MsgCardList extends JScrollPane {
    public MsgCardList(List<Msg> msgs, int width) {
        super();
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Msg msg : msgs){
            FriendCard card = new FriendCard(msg.getSenderInitials(), msg.getSenderName(), msg.getContent(), width-5);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            list.add(card);
        }

        this.setViewportView(list);
        this.setBorder(null);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }

}

