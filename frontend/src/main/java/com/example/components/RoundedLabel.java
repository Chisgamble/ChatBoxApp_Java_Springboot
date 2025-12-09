package com.example.components;

import javax.swing.*;
import java.awt.*;
//import java.awt.geom.RoundRectangle2D;

public class RoundedLabel extends JLabel{
    private final int radius;
    private final int pad;
    public RoundedLabel(int radius, int pad){
        this.pad = pad;
        this.radius = radius;
        setOpaque(false); // VERY important
        setBorder(BorderFactory.createEmptyBorder(pad, pad, pad, pad)); // padding inside
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // background
        g2.setColor(getBackground());
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

        super.paintComponent(g); // draw the text and caret
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getForeground()); // or custom border color
        g2.drawRoundRect(1, 1, getWidth()-2, getHeight() - 2, radius, radius);
        g2.dispose();
    }

}
