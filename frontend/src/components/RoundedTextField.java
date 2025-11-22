package components;

import javax.swing.*;
import java.awt.*;

public class RoundedTextField extends JTextField {

    protected final int radius;

    public RoundedTextField(int radius) {
        this.radius = radius;
        setOpaque(false); // VERY important
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding inside
    }

    public RoundedTextField(int radius, int pad) {
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
        g2.fillRoundRect(0, 1, getWidth(), getHeight() - 2, radius, radius);

        g2.dispose();
        super.paintComponent(g); // draw the text and caret
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getForeground()); // or custom border color
        g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, radius, radius);

        g2.dispose();
    }
}
