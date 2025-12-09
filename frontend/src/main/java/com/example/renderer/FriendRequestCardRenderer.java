package com.example.renderer;

import com.example.components.Avatar;
import com.example.components.MyColor;
import com.example.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class FriendRequestCardRenderer extends JPanel implements ListCellRenderer<User> {
    private final Avatar avatar;
    private final JLabel nameLabel;

    public FriendRequestCardRenderer(int width) {
        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, 60));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        this.setMinimumSize(new Dimension(width, 60));
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0,0,1,0, MyColor.DARK_GRAY),  // LineBorder for the border
                new EmptyBorder(5,0,5,0)  // EmptyBorder for padding (10px on all sides)
        ));
        avatar = new Avatar("");

        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        westWrapper.setOpaque(false); // optional, to be transparent
        westWrapper.add(avatar);

        nameLabel = new JLabel();
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

        JButton reject = new JButton("Reject");
        reject.setFont(accept.getFont().deriveFont(16f));
        reject.setForeground(Color.red);
        reject.setBorderPainted(false);
        reject.setOpaque(false);
        reject.setContentAreaFilled(false);
        reject.setFocusPainted(false);

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

    @Override
    public Component getListCellRendererComponent(
            JList<? extends User> list,
            User user,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        // Update data
        avatar.setInitials(user.getInitials());
        nameLabel.setText(user.getName());

        // Selection effect
        if (isSelected) {
            setBackground(new Color(0xDDEAFF)); // light blue
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }
}
