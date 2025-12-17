package com.example.components;

import javax.swing.*;
import java.awt.*;

public class RoundedTextArea extends JTextArea {
    private final int radius;
    private final int pad;
    public RoundedTextArea(int radius, int pad){
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
        g2.fillRoundRect(0, 1, getWidth() - 1, getHeight() - 2, radius, radius);

        super.paintComponent(g); // draw the text and caret
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getForeground()); // or custom border color
        g2.drawRoundRect(0, 1, getWidth() - 1, getHeight() - 2, radius, radius);
        g2.dispose();
    }

    public void autoResize(int minHeight, int maxHeight) {
        int lines = getLineCount();
        FontMetrics fm = getFontMetrics(getFont());
        int lineHeight = fm.getHeight();

        int newHeight = lines * lineHeight + getInsets().top + getInsets().bottom;

        newHeight = Math.max(minHeight, Math.min(maxHeight, newHeight));

        Dimension size = getPreferredSize();
        size.height = newHeight;
        setPreferredSize(size);

        revalidate();
    }

}
