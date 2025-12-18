package com.example.components.user;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedDialog;
import com.example.components.RoundedTextField;
import com.example.services.AuthService;
import com.example.dto.response.GeneralResDTO;
import javax.swing.*;
import java.awt.*;

public class ResetPasswordPopup {
    public static void show(JFrame parent) {
        RoundedDialog dialog = new RoundedDialog(parent, "Reset Password", 400, 250);
        AuthService authService = new AuthService();

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JLabel instructionLabel = new JLabel("Enter your email to receive a new password:");
        instructionLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // TextField nhập Email
        RoundedTextField emailField = new RoundedTextField(20, 10);
        emailField.setMaximumSize(new Dimension(350, 40));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(emailField);

        // --- Phần nút bấm (South) ---
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setOpaque(false);

        RoundedButton cancelBtn = new RoundedButton(20);
        cancelBtn.setText("Cancel");
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(MyColor.DARK_GRAY);

        RoundedButton sendBtn = new RoundedButton(20);
        sendBtn.setText("Send New Password");
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setBackground(MyColor.LIGHT_BLUE);

        // Xử lý sự kiện
        cancelBtn.addActionListener(e -> dialog.dispose());

        sendBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter your email.");
                return;
            }

            try {
                // Gọi service
                GeneralResDTO response = authService.resetPassword(email);

                JOptionPane.showMessageDialog(parent,
                        "Success: " + response.message(),
                        "Notification",
                        JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error: " + ex.getMessage(),
                        "Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(cancelBtn);
        buttonRow.add(Box.createHorizontalStrut(20));
        buttonRow.add(sendBtn);
        buttonRow.add(Box.createHorizontalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonRow, BorderLayout.SOUTH);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }
}
