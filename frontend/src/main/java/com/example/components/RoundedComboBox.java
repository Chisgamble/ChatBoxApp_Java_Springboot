package com.example.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class RoundedComboBox<E> extends JComboBox<E> {

    private final int radius = 15; // corner radius

    public RoundedComboBox(E[] items) {
        super(items);
        setFocusable(false);
        setOpaque(false);
        setForeground(MyColor.DARK_GRAY);
        setFont(new Font("Roboto", Font.PLAIN, 16));

        // Padding for the text inside the main box
        setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrow = new JButton("▼");
                arrow.setForeground(MyColor.DARK_GRAY);
                arrow.setBorder(BorderFactory.createEmptyBorder());
                arrow.setFocusable(false);
                arrow.setContentAreaFilled(false);
                arrow.setOpaque(false);
                arrow.setMargin(new Insets(0, 0, 0, 0));
                return arrow;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected void configurePopup() {
                        super.configurePopup();

                        // 1. Remove ScrollPane Borders
                        if (scroller != null) {
                            scroller.setBorder(BorderFactory.createEmptyBorder());
                            scroller.setViewportBorder(BorderFactory.createEmptyBorder());
                            scroller.setOpaque(false);
                            scroller.getViewport().setOpaque(false);
                        }

                        // 2. MAKE THE LIST TRANSPARENT (Crucial Fix)
                        // This removes the "boxy" white background behind the text items
                        list.setOpaque(false);
                        list.setBackground(new Color(0, 0, 0, 0)); // Fully transparent
                        list.setSelectionBackground(new Color(0x0084FF)); // Highlight color
                        list.setSelectionForeground(Color.WHITE);

                        // 3. Add padding inside the popup so text doesn't hit rounded corners
                        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    }

                    // 4. Paint the rounded background on the POPUP CONTAINER only
                    @Override
                    public void paint(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Draw White Rounded Background
                        g2.setColor(Color.WHITE);
                        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                        // Draw the items (transparent list sits on top of this white shape)
                        super.paint(g2);

                        // Draw Gray Border
                        g2.setColor(Color.GRAY);
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                        g2.dispose();
                    }
                };

                popup.setOpaque(false);
                return popup;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

        g2.dispose();
    }
}