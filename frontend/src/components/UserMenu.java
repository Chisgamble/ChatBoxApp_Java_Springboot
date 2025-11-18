package components;

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

    public UserMenu(int width, int height) {
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
        popupMenu.add(new JMenuItem("Profile"));
        popupMenu.add(new JMenuItem("Settings"));
        popupMenu.add(new JMenuItem("Logout"));

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
