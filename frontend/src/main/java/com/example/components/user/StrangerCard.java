package com.example.components.user;

import com.example.components.Avatar;
import com.example.components.MyColor;
import com.example.dto.response.StrangerCardResDTO;
import com.example.listener.FriendRequestListener;
import com.example.model.User;
import com.example.services.FriendRequestService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StrangerCard extends JPanel {
    FriendRequestService service = new FriendRequestService();

    public StrangerCard(StrangerCardResDTO user, int width,  FriendRequestListener listener) {
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

        JLabel nameLabel = new JLabel(user.getUsername());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16f));

        JPanel options = new JPanel();
        options.setOpaque(false);
        options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));

        JButton addFriend = new JButton();

        if (user.isRequestSent()){
            addFriend.setText("Request Sent");
            addFriend.setForeground(Color.GREEN);
        }else{
            addFriend.setText("Add Friend");
            addFriend.setForeground(MyColor.LIGHT_BLUE);
        }

        addFriend.setFont(addFriend.getFont().deriveFont(16f));
        addFriend.setBorderPainted(false);
        addFriend.setOpaque(false);
        addFriend.setContentAreaFilled(false);
        addFriend.setFocusPainted(false);
        addFriend.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addFriend.addActionListener( e -> {
            addFriend.setText("Request sent");
            addFriend.setForeground(Color.GREEN);
            addFriend.revalidate();
            user.setRequestSent(true);

            listener.onSent(user.getUserId());
        });

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
