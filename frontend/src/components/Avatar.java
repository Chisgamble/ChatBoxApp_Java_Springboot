package components;

import javax.swing.*;
import java.awt.*;

public class Avatar extends JComponent {

    private String text;

    public Avatar(String text) {
        this.text = text;
        setPreferredSize(new Dimension(40, 40)); // Circle size
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int size = Math.min(getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background circle
        g2.setColor(new Color(100, 150, 255));
        g2.fillOval(0, 0, size, size);

        // Text
        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(Font.BOLD, 14f));

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textAscent = fm.getAscent();

        int x = (size - textWidth) / 2;
        int y = (size + textAscent) / 2 - 2;

        g2.drawString(text, x, y);
    }
}
