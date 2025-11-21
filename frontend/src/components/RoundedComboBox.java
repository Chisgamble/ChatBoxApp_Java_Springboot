package components;

import javax.swing.*;
import java.awt.*;

public class RoundedComboBox<E> extends JComboBox<E> {

    private final int radius = 15; // corner radius

    public RoundedComboBox(E[] items) {
        super(items);
        setOpaque(false);
        setForeground(MyColor.DARK_GRAY);
        setFont(new Font("Roboto", Font.PLAIN, 16));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrow = new JButton("▼");  // small triangle
                arrow.setForeground(MyColor.DARK_GRAY);
                arrow.setBorder(BorderFactory.createEmptyBorder());
                arrow.setFocusable(false);
                arrow.setContentAreaFilled(false); // no background
                arrow.setOpaque(false);
                arrow.setMargin(new Insets(0, 0, 0, 0));
                return arrow;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }


    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.GRAY);
        g2.drawRoundRect(1, 0, getWidth() - 2, getHeight() - 1, radius, radius);

        g2.dispose();
    }
}
