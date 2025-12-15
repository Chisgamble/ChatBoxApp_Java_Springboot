package com.example.components.user;

import com.example.components.Avatar;
import com.example.dto.response.FriendRequestResDTO;
import com.example.listener.FriendRequestListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class FriendRequestCard extends JPanel {

    public FriendRequestCard(
            FriendRequestResDTO req,
            int width,
            FriendRequestListener listener,
            FriendRequestList parentList) {
        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, 60));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        this.setMinimumSize(new Dimension(width, 60));
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0,0,1,0, Color.BLACK),  // LineBorder for the border
                new EmptyBorder(5,0,5,0)  // EmptyBorder for padding (10px on all sides)
        ));
        Avatar avatar = new Avatar(req.getSenderUsername().substring(0,1).toUpperCase());

        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        westWrapper.setOpaque(false); // optional, to be transparent
        westWrapper.add(avatar);

        JLabel nameLabel = new JLabel(req.getSenderUsername());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16f));

        JPanel options = new JPanel();
        options.setOpaque(false);
        options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));

        JButton accept = new JButton("Accept");
        accept.setFont(accept.getFont().deriveFont(16f));
        accept.setForeground(Color.green);
        accept.setBorderPainted(false);
        accept.setOpaque(false);
        accept.setContentAreaFilled(false);
        accept.setFocusPainted(false);
        accept.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton reject = new JButton("Reject");
        reject.setFont(accept.getFont().deriveFont(16f));
        reject.setForeground(Color.red);
        reject.setBorderPainted(false);
        reject.setOpaque(false);
        reject.setContentAreaFilled(false);
        reject.setFocusPainted(false);
        reject.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // EVENTS
        accept.addActionListener(e -> {
            listener.onAccept(req);
            parentList.removeCard(this);   // remove UI
        });

        reject.addActionListener(e -> {
            listener.onReject(req);
            parentList.removeCard(this);
        });

        options.add(Box.createHorizontalStrut(40));
        options.add(accept);
        options.add(Box.createHorizontalGlue());
        options.add(reject);
        options.add(Box.createHorizontalStrut(40));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(nameLabel, BorderLayout.NORTH);
        centerContainer.add(options, BorderLayout.SOUTH);

        this.add(westWrapper, BorderLayout.WEST);
        this.add(centerContainer, BorderLayout.CENTER);

    }
}
