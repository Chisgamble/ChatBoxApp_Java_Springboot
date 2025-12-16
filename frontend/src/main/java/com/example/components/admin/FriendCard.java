package com.example.components.admin;

import com.example.dto.FriendCardDTO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class FriendCard extends JPanel {
    private final FriendCardDTO data;

    public FriendCard(FriendCardDTO data) {
        this.data = data;
        initUI();
    }

    private void initUI() {
        // Layout: Avatar on West, Text on Center
        setLayout(new BorderLayout(15, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setBackground(Color.WHITE);

        // This makes sure the card doesn't stretch vertically too much in a BoxLayout
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        setPreferredSize(new Dimension(350, 70));

        // --- 1. Avatar (Custom Painted) ---
        JLabel avatar = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw Blue Circle
                g2.setColor(new Color(65, 105, 225));
                g2.fill(new Ellipse2D.Double(0, 0, 45, 45));

                // Draw Initials
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));

                String initial = "?";
                if (data.getUsername() != null && !data.getUsername().isEmpty()) {
                    initial = data.getUsername().substring(0, 1).toUpperCase();
                }

                FontMetrics fm = g2.getFontMetrics();
                int x = (45 - fm.stringWidth(initial)) / 2;
                int y = ((45 - fm.getHeight()) / 2) + fm.getAscent();

                g2.drawString(initial, x, y);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(45, 45);
            }
        };

        // --- 2. Text Info ---
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(data.getUsername());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(Color.BLACK);

        // Display last message content (mapped from backend 'content' -> 'last_msg')
        String msgContent = (data.getLast_msg() != null) ? data.getLast_msg() : "No messages";
        JLabel msgLabel = new JLabel(msgContent);
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        msgLabel.setForeground(Color.GRAY);

        textPanel.add(nameLabel);
        textPanel.add(msgLabel);

        add(avatar, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }
}