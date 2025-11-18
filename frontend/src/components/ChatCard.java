package components;

import javax.swing.*;
import java.awt.*;

public class ChatCard extends JPanel {

    public ChatCard(String senderInitials, String message) {
        setLayout(new BorderLayout(10, 0));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Avatar avatar = new Avatar(senderInitials);

        JLabel msgLabel = new JLabel("<html><body style='width:200px;'>" + message + "</body></html>");
        msgLabel.setOpaque(true);
        msgLabel.setBackground(Color.WHITE);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(avatar, BorderLayout.WEST);
        add(msgLabel, BorderLayout.CENTER);
    }
}
