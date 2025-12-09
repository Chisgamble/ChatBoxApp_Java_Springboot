package com.example.components.user;

import com.example.components.Avatar;
import com.example.components.MyColor;
import com.example.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class StrangerCard extends JPanel {

    public StrangerCard(User user, int width) {
        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, 60));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        this.setMinimumSize(new Dimension(width, 60));
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0,0,1,0, Color.BLACK),  // LineBorder for the border
                new EmptyBorder(5,0,5,0)  // EmptyBorder for padding (10px on all sides)
        ));
        Avatar avatar = new Avatar(user.getInitials());

        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        westWrapper.setOpaque(false); // optional, to be transparent
        westWrapper.add(avatar);

        JLabel nameLabel = new JLabel(user.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16f));

        JPanel options = new JPanel();
        options.setOpaque(false);
        options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));

        JButton addFriend = new JButton("Add Friend");
        addFriend.setFont(addFriend.getFont().deriveFont(16f));
        addFriend.setForeground(MyColor.LIGHT_BLUE);
        addFriend.setBorderPainted(false);
        addFriend.setOpaque(false);
        addFriend.setContentAreaFilled(false);
        addFriend.setFocusPainted(false);

        options.add(Box.createHorizontalStrut(100));
        options.add(addFriend);
        options.add(Box.createHorizontalStrut(40));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(nameLabel, BorderLayout.NORTH);
        centerContainer.add(options, BorderLayout.SOUTH);

        this.add(westWrapper, BorderLayout.WEST);
        this.add(centerContainer, BorderLayout.CENTER);

    }
}
