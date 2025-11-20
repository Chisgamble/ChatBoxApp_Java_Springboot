package components;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private final int radius;

    public RoundedButton(int radius) {
        this.radius = radius;
        setContentAreaFilled(false);
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
        g2.drawRoundRect(0, 1, getWidth() - 1, getHeight() - 2, radius, radius);
        g2.dispose();
    }
}
