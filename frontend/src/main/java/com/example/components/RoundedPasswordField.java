package com.example.components;


import javax.swing.*;
import java.awt.*;

public class RoundedPasswordField extends JPasswordField {

    private final int radius;

    public RoundedPasswordField(int radius) {
        this.radius = radius;
        setOpaque(false); // important for custom painting
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // inner padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();

        // draw password text + caret AFTER background
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getForeground()); // border color
        g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, radius, radius);

        g2.dispose();
    }
}
