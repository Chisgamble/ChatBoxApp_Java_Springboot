package components;

import listener.UserMenuListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class UserMenu extends JPanel {

    private final JButton menuButton;
    private final JPopupMenu popupMenu;
    private final UserMenuListener listener;

    public UserMenu(int width, int height, UserMenuListener listener) {
        this.listener = listener;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
//        this.setPreferredSize(new Dimension(width, height));
        this.setOpaque(false);
        this.setBackground(Color.CYAN); // temporary

        menuButton = new JButton();
        menuButton.setFocusPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.setOpaque(false);
        menuButton.setPreferredSize(new Dimension(width, height ));
        menuButton.setText("☰");
        menuButton.setFont(menuButton.getFont().deriveFont(18f));
        menuButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));

        menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Popup menu
        popupMenu = new JPopupMenu();

        JMenuItem profileItem = new JMenuItem("Profile");
        profileItem.addActionListener(e -> listener.onMenuOptionSelected("Profile"));
        JMenuItem inboxItem = new JMenuItem("Inbox");
        inboxItem.addActionListener(e -> listener.onMenuOptionSelected("Inbox"));
        JMenuItem friendReqItem = new JMenuItem("Friend request");
        friendReqItem.addActionListener(e -> listener.onMenuOptionSelected("Friend request"));
        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(e -> listener.onMenuOptionSelected("Settings"));
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> listener.onMenuOptionSelected("Logout"));

        popupMenu.add(profileItem);
        popupMenu.add(inboxItem);
        popupMenu.add(friendReqItem);
        popupMenu.add(settingsItem);
        popupMenu.add(logoutItem);

        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                popupMenu.show(menuButton, 0, menuButton.getHeight());
            }
        });

        this.add(menuButton);
//        this.setBorder(BorderFactory.createLineBorder(Color.RED, 1)); // visible border for debug
    }
}
