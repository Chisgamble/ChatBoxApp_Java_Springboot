package components.user;

import listener.UserMenuListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

        JMenuItem friendItem = new JMenuItem("Friends");
        friendItem.addActionListener(e -> listener.onMenuOptionSelected("Friends"));

        JMenuItem findItem = new JMenuItem("Find User");
        findItem.addActionListener(e -> listener.onMenuOptionSelected("Find User"));

        JMenuItem friendReqItem = new JMenuItem("Friend request");
        friendReqItem.addActionListener(e -> listener.onMenuOptionSelected("Friend request"));

        JMenuItem groupItem = new JMenuItem("Groups");
        groupItem.addActionListener(e -> listener.onMenuOptionSelected("Groups"));

        JMenuItem searchItem = new JMenuItem("Search from all messages");
        searchItem.addActionListener(e -> listener.onMenuOptionSelected("SearchMsg"));

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> listener.onMenuOptionSelected("Logout"));

        popupMenu.add(profileItem);
        popupMenu.add(friendItem);
        popupMenu.add(findItem);
        popupMenu.add(friendReqItem);
        popupMenu.add(groupItem);
        popupMenu.add(searchItem);
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
