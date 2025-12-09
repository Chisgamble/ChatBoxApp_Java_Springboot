package com.example.components.user;

import com.example.components.Avatar;
import com.example.components.RoundedLabel;

import javax.swing.*;
import java.awt.*;

public class MsgBubble extends JPanel {
    private Color bubbleColor;
    private Color textColor;
    private static final int PADDING = 10;
    private static final int AVATAR_GAP = 8;

    public MsgBubble(String message, boolean isUser, int chatPanelWidth) {
        this(message, isUser, chatPanelWidth, null);
    }

    public MsgBubble(String message, boolean isUser, int chatPanelWidth, String avatarText) {
        // Set colors based on sender
        if (isUser) {
            bubbleColor = new Color(0, 132, 255); // Blue for user
            textColor = Color.WHITE;
        } else {
            bubbleColor = Color.WHITE; // Gray for other
            textColor = Color.BLACK;
        }

        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // Add avatar for non-user messages
        if (!isUser && avatarText != null) {
            Avatar avatar = new Avatar(avatarText);
            this.add(avatar);
            this.add(Box.createHorizontalStrut(AVATAR_GAP));
        }

        // Calculate max width (half of chat panel minus avatar space if present)
        int maxWidth = chatPanelWidth / 2;
        if (!isUser && avatarText != null) {
            maxWidth -= (40 + AVATAR_GAP); // Subtract avatar width and gap
        }

        RoundedLabel textArea = new RoundedLabel(20,10);

        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
        if (fm.stringWidth(message) < maxWidth){
            textArea.setText("<html>" + message + "</html>");
        }else
            textArea.setText("<html><body style='width: " + maxWidth + "px'>" + message + "</body></html>");

        textArea.setOpaque(false);
        textArea.setBackground(bubbleColor);
        textArea.setForeground(textColor);
//        textArea.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        this.setMaximumSize(new Dimension(maxWidth, textArea.getPreferredSize().height));
        this.add(textArea);

        // Set alignment based on sender
        this.setAlignmentX(isUser ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
    }
}