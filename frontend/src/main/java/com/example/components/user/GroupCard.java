package com.example.components.user;

import com.example.components.Avatar;
import com.example.dto.GroupCardDTO;
import com.example.listener.GroupCardListener;
import com.example.util.Utility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GroupCard extends JPanel {
    private boolean selected = false;
    private final GroupCardDTO group;
    private JLabel nameLabel;

    public GroupCard(
            GroupCardDTO group,
            int width,
            GroupCardListener listener) {
        this.group = group;

        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(width, 60));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        this.setMinimumSize(new Dimension(width, 60));
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0,0,1,0, Color.BLACK),  // LineBorder for the border
                new EmptyBorder(5,0,5,0)  // EmptyBorder for padding (10px on all sides)
        ));
        Avatar avatar = new Avatar(Utility.getInitials(group.getGroupname()));

        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        westWrapper.setOpaque(false); // optional, to be transparent
        westWrapper.add(avatar);

        nameLabel = new JLabel(group.getGroupname());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16f));

        JLabel MsgLabel = new JLabel();
        if (group.getLast_msg() != null  && !group.getLast_msg().isBlank()){
            MsgLabel.setText(group.getLast_msg());
            MsgLabel.setVisible(true);
        }else{
            MsgLabel.setVisible(false);
        }
        MsgLabel.setForeground(Color.GRAY);
        MsgLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(nameLabel, BorderLayout.NORTH);
        centerContainer.add(MsgLabel, BorderLayout.SOUTH);

        this.add(westWrapper, BorderLayout.WEST);
        this.add(centerContainer, BorderLayout.CENTER);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) setBackground(new Color(0xF2F2F2));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) setBackground(Color.WHITE);
                setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onGroupSelected(GroupCard.this, group);
            }
        });
    }

    public GroupCardDTO getGroup() {
        return group;
    }

    public void setSelected(boolean value) {
        this.selected = value;
        setBackground(value ? new Color(0xDDEAFF) : Color.WHITE);
        repaint();
    }

    public void refresh() {
        nameLabel.setText(group.getGroupname());
        repaint();
    }
}
