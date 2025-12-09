package com.example.components;

import javax.swing.*;
import java.awt.*;

public class RoundedDialog extends JDialog {

    private int radius = 20;  // rounded corner radius

    public RoundedDialog(JFrame parent, String title, int width, int height) {
        super(parent, title, true);
        setSize(width, height);
        setUndecorated(true);         // remove window title bar
        setLocationRelativeTo(parent);
        setBackground(new Color(0,0,0,0));   // transparent background

        // main container (rounded)
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MyColor.LIGHT_PURPLE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

                // Draw border
//                g2.setColor(MyColor.DARK_GRAY); // border color
//                g2.setStroke(new BasicStroke(2)); // border thickness
//                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.dispose();
            }
        };
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        setContentPane(content);
    }
}
