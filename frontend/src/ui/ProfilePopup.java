package ui;

import javax.swing.*;
import java.awt.*;

public class ProfilePopup extends JDialog {

    public ProfilePopup(JFrame parent) {
        super(parent, "Profile", true); // true = modal
        this.setSize(400, 300);
        this.setLocationRelativeTo(parent); // center on parent
        this.setLayout(new BorderLayout());

        JLabel label = new JLabel("User Profile Screen", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(16f));
        this.add(label, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        this.add(closeButton, BorderLayout.SOUTH);
    }
}
