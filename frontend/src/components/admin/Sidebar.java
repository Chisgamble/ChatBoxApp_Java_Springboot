package components.admin;

import components.MyColor;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {

    public Sidebar() {
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        setMinimumSize(new Dimension(180, 1080));
        setMaximumSize(new Dimension(225, 1080));
        setPreferredSize(new Dimension(180, 1080));
        setBackground(MyColor.WHITE_BG);
        setBorder(BorderFactory.createLineBorder(MyColor.LIGHT_BLACK));
        // Header
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;  // <-- anchor to the left
        c.weightx = 1.0;

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 55, 109));

        JLabel headerLabel = new JLabel("Categories");
        headerLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        headerLabel.setForeground(Color.WHITE);

        header.add(headerLabel, BorderLayout.WEST);
        add(header, c);

        // Menu items
        String[] menuItems = {
                "User list",
                "Login activity log",
                "Chat group list",
                "Spam report list",
                "New user list",
                "Graphs",
                "User and friends",
                "Active"
        };

        int row = 1;
        for (String item : menuItems) {
            c.gridy = row++;
            c.insets = new Insets(0, 0, 0, 0);   // optional: padding around button

            JButton button = createMenuItem(item);
            add(button, c);
        }

        // Push items to top: add a "filler" component
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = row;
        c.weighty = 1.0;            // take all extra vertical space
        c.fill = GridBagConstraints.VERTICAL;
        add(Box.createVerticalGlue(), c);
    }

    private JButton createMenuItem(String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setForeground(new Color(60, 60, 60));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(MyColor.WHITE_BG);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0xD1DAE3));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(MyColor.WHITE_BG);
            }
        });

        return button;
    }
}
