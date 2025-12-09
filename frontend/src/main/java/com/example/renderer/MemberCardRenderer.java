package com.example.renderer;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.example.components.Avatar;
import com.example.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MemberCardRenderer extends JPanel implements ListCellRenderer<User>  {
    private final Avatar avatar;
    private final JLabel nameLabel;
    private final JLabel roleLabel;

    public MemberCardRenderer(int width, boolean isAdmin){
        User user = new User();
        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, 60));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        this.setMinimumSize(new Dimension(width, 60));
        this.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 1),  // LineBorder for the border
                new EmptyBorder(5,5,5,5)  // EmptyBorder for padding (10px on all sides)
        ));

        avatar = new Avatar(user.getInitials());

        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        westWrapper.setOpaque(false); // optional, to be transparent
        westWrapper.add(avatar);

        nameLabel = new JLabel(user.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16f));

        FlatSVGIcon icon = null;
        if (user.isActive())
            icon = new FlatSVGIcon("assets/online-icon.svg", 10, 10);
        else
            icon = new FlatSVGIcon("assets/offline-icon.svg", 10, 10);

        // Set icon on JLabel
        nameLabel.setIcon(icon);

        // Position the icon relative to text
        nameLabel.setHorizontalTextPosition(SwingConstants.LEFT); // text on the left, icon on the right
        nameLabel.setVerticalTextPosition(SwingConstants.CENTER); // vertically centered
        nameLabel.setIconTextGap(5); // gap between text and icon

        roleLabel = new JLabel(user.isAdmin() ? "Admin" : "Member");
        roleLabel.setForeground(Color.GRAY);
        roleLabel.setOpaque(false);
        roleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JLabel option = new JLabel(new FlatSVGIcon("assets/ellipsis-solid-full.svg", 30, 30));
        option.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        option.setVisible(isAdmin);
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem promoteItem = new JMenuItem("Promote to Admin");
//        promoteItem.addActionListener(e -> listener.onMenuOptionSelected("Promote"));
        JMenuItem removeItem = new JMenuItem("Remove from group");
//        removeItem.addActionListener(e -> listener.onMenuOptionSelected("Remove From Group"));

        popupMenu.add(promoteItem);
        popupMenu.add(removeItem);

        option.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(option, 0, option.getHeight());
                System.out.print(true);
            }
        });

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(nameLabel, BorderLayout.NORTH);
        centerContainer.add(roleLabel, BorderLayout.SOUTH);

        this.add(westWrapper, BorderLayout.WEST);
        this.add(centerContainer, BorderLayout.CENTER);
        this.add(option, BorderLayout.EAST);
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
        FlatSVGIcon icon;
        if (user.isActive())
            icon = new FlatSVGIcon("assets/online-icon.svg", 10, 10);
        else
            icon = new FlatSVGIcon("assets/offline-icon.svg", 10, 10);
        nameLabel.setIcon(icon);
        roleLabel.setText(user.isAdmin() ? "Admin" : "Member");

        // Selection effect
        if (isSelected) {
            setBackground(new Color(0xDDEAFF)); // light blue
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }
}
