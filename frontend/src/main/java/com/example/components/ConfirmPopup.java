package com.example.components;

import java.awt.*;
import javax.swing.*;

public class ConfirmPopup {

    public static boolean show(JFrame parent, String action) {
        RoundedDialog dialog = new RoundedDialog(parent, "Confirm", 350, 200);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(10, 10));

        JTextArea label = new JTextArea("Are you sure that you want to " + action + " ?");
        label.setFont(label.getFont().deriveFont(16f));
        label.setEditable(false);
        label.setOpaque(false);
        label.setFocusable(false);
        label.setLineWrap(true);        // wrap long lines
        label.setWrapStyleWord(true);   // wrap at word boundaries
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setOpaque(false);

        RoundedButton cancelBtn = new RoundedButton(20);
        cancelBtn.setText("Cancel");
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(MyColor.DARK_GRAY);
        cancelBtn.setFocusPainted(false);

        RoundedButton confirmBtn = new RoundedButton(20);
        confirmBtn.setText("Confirm");
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBackground(MyColor.LIGHT_BLUE);
        cancelBtn.setFocusPainted(false);

        final boolean[] confirmed = {false};

        cancelBtn.addActionListener(e -> {
            confirmed[0] = false;
            dialog.dispose();
        });

        confirmBtn.addActionListener(e -> {
            confirmed[0] = true;
            dialog.dispose();
        });

        buttonRow.add(Box.createVerticalStrut(10));
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(cancelBtn);
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(confirmBtn);
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(Box.createVerticalStrut(90));

        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonRow, BorderLayout.SOUTH);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);

        return confirmed[0];
    }
}
