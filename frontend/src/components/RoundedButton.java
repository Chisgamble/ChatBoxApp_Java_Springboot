package components;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private int radius = 15;
    private Color borderColour = MyColor.LIGHT_BLACK;

    public RoundedButton(int radius) {
        this.radius = radius;
        borderColour = new Color(255,255,255,1);
        setContentAreaFilled(false);
        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(false);
        setFocusPainted(false);
    }
    public RoundedButton(int radius, Color border) {
        this.radius = radius;
        borderColour = border;
        setContentAreaFilled(false);
        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(false);
        setFocusPainted(false);
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        if (getModel().isArmed()) {
            g2.setColor(getBackground().darker()); // pressed
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter()); // hover
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Draw button text
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColour);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}
