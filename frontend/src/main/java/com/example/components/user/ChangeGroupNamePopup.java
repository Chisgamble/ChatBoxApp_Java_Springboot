package com.example.components.user;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedDialog;
import com.example.components.RoundedTextField;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

public class ChangeGroupNamePopup{
    static RoundedTextField input = new RoundedTextField(10,10);

    public static boolean show(JFrame parent)  {
        RoundedDialog dialog = new RoundedDialog(parent, "Confirm", 350, 200);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextArea label = new JTextArea("Enter new group name");
        label.setFont(label.getFont().deriveFont(20f));
        label.setEditable(false);
        label.setOpaque(false);
        label.setFocusable(false);
        label.setLineWrap(true);        // wrap long lines
        label.setWrapStyleWord(true);   // wrap at word boundaries
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        input.setBackground(Color.WHITE);
        input.setForeground(Color.GRAY);
        input.setText("New name");
        input.setMaximumSize(new Dimension(250, 30));
        input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (input.getText().equals("New name")) {
                    input.setText("");
                    input.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (input.getText().isEmpty()) {
                    input.setForeground(Color.GRAY);
                    input.setText("New name");
                }
            }
        });

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

        panel.add(label);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(input);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(buttonRow);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);

        return confirmed[0];
    }

}
