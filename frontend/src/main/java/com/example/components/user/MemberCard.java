package com.example.components.user;

import com.example.listener.GroupMemberActionListener;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.example.components.Avatar;
import com.example.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MemberCard extends JPanel {

    public MemberCard(String initials, String name, Long groupId, Long userId, String role, int width, boolean viewByAdmin, GroupMemberActionListener listener) {
        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, 60));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        this.setMinimumSize(new Dimension(width, 60));
        this.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0,0,1,0, Color.BLACK),  // LineBorder for the border
                new EmptyBorder(5,5,5,5)  // EmptyBorder for padding (10px on all sides)
        ));

        Avatar avatar = new Avatar(initials);

        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        westWrapper.setOpaque(false);
        westWrapper.add(avatar);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16f));

//        FlatSVGIcon icon = null;
//        if (isActive)
//            icon = new FlatSVGIcon("assets/online-icon.svg", 10, 10);
//        else
//            icon = new FlatSVGIcon("assets/offline-icon.svg", 10, 10);
//
//        // Set icon on JLabel
//        nameLabel.setIcon(icon);

        // Position the icon relative to text
        nameLabel.setHorizontalTextPosition(SwingConstants.LEFT); // text on the left, icon on the right
        nameLabel.setVerticalTextPosition(SwingConstants.CENTER); // vertically centered
        nameLabel.setIconTextGap(5); // gap between text and icon

        JLabel msgLabel = new JLabel(role.equals("admin") ? "Admin" : "Member");
        msgLabel.setForeground(Color.GRAY);
        msgLabel.setOpaque(false);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(nameLabel, BorderLayout.NORTH);
        centerContainer.add(msgLabel, BorderLayout.SOUTH);

        JLabel option = new JLabel(new FlatSVGIcon("assets/ellipsis-solid-full.svg", 30, 30));
        option.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        option.setVisible(viewByAdmin);
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem promoteItem = new JMenuItem("Promote to Admin");
        promoteItem.addActionListener(e -> listener.onPromoteMember(groupId, userId));
        JMenuItem removeItem = new JMenuItem("Remove from group");
        removeItem.addActionListener(e -> listener.onRemoveMember(groupId, userId));

        popupMenu.add(promoteItem);
        popupMenu.add(removeItem);

        option.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(option, 0, option.getHeight());
                System.out.print(true);
            }
        });

        this.add(westWrapper, BorderLayout.WEST);
        this.add(centerContainer, BorderLayout.CENTER);
        this.add(option, BorderLayout.EAST);
    }
}
