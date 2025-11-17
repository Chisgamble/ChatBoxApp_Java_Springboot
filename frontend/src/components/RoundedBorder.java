package components;
import java.awt.*;
import javax.swing.border.Border;

public class RoundedBorder implements Border {
    private final int radius;
    private final Color color;
    private final int thickness;

    public RoundedBorder(Color color, int thickness, int radius) {
        this.color = color;
        this.thickness = thickness;
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRoundRect(
                x + thickness / 2,
                y + thickness / 2,
                width - thickness,
                height - thickness,
                radius,
                radius
        );
    }

    // @Override
    // protected void paintComponent(Graphics g) {

    //     Graphics2D g2 = (Graphics2D) g;
    //     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    //     // Clip the component to a rounded rectangle
    //     g2.setClip(new java.awt.geom.RoundRectangle2D.Float(
    //         0, 0, getWidth(), getHeight(), radius, radius
    //     ));

    //     // Fill the rounded rectangle with the panel's background color
    //     g2.setColor(getBackground()); 
    //     g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
    // }
}
