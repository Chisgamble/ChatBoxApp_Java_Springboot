package components;

import javax.swing.*;
import java.awt.*;

public class ChatCard extends JPanel {

    public ChatCard(String senderInitials, String name, String message, int width, int height) {
        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, 60));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Avatar avatar = new Avatar(senderInitials);
        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        westWrapper.setOpaque(false); // optional, to be transparent
        westWrapper.add(avatar);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16f));

        JLabel msgLabel = new JLabel(message);
        msgLabel.setForeground(Color.GRAY);
        msgLabel.setOpaque(false);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(nameLabel, BorderLayout.NORTH);
        centerContainer.add(msgLabel, BorderLayout.SOUTH);

        this.add(westWrapper, BorderLayout.WEST);
        this.add(centerContainer, BorderLayout.CENTER);

    }
}
