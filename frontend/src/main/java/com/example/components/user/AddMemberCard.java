package com.example.components.user;

import com.example.components.Avatar;
import com.example.components.MyColor;
import com.example.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddMemberCard extends JPanel {

    private boolean selected = false;
    private User user;

    public AddMemberCard(User user, int width) {
        this.user = user;

        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(true);
        this.setBackground(MyColor.LIGHT_PURPLE);
        this.setPreferredSize(new Dimension(width, 60));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0,0,1,0, Color.BLACK),
                new EmptyBorder(5,5,5,5)
        ));

        Avatar avatar = new Avatar(user.getInitials());

        JPanel west = new JPanel(new FlowLayout(FlowLayout.CENTER));
        west.setOpaque(false);
        west.add(avatar);

        JLabel nameLabel = new JLabel(user.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(nameLabel, BorderLayout.CENTER);

        this.add(west, BorderLayout.WEST);
        this.add(center, BorderLayout.CENTER);

        // CLICK TO SELECT
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggle();
            }
        });
    }

    private void toggle() {
        selected = !selected;
        setBackground(selected ? MyColor.LIGHT_PURPLE.darker() : MyColor.LIGHT_PURPLE);
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public User getUser() {
        return user;
    }
}
