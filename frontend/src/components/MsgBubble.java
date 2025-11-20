package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

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
            bubbleColor = new Color(230, 230, 230); // Gray for other
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

//        RoundedTextArea textArea = new RoundedTextArea(20, PADDING);
//        textArea.setText(message);
//        textArea.setWrapStyleWord(true);
//        textArea.setLineWrap(true);
//        textArea.setBackground(bubbleColor);
//        textArea.setEditable(false);
//        textArea.setFocusable(false);
//        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
//        textArea.setForeground(textColor);

        // Calculate preferred size
//        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
//        int textWidth = Math.min(fm.stringWidth(message), maxWidth);
//        textArea.setSize(textWidth + 1, Short.MAX_VALUE);
//        int textHeight = textArea.getPreferredSize().height;
//
//        System.out.println(textWidth + " " + textHeight + " " + textArea.getPreferredSize().width);
//        textArea.setPreferredSize(new Dimension(Math.min(textWidth, maxWidth), textHeight));
//        System.out.println(fm.stringWidth(message) + " " + textArea.getPreferredSize().width + " " + maxWidth);
//        System.out.println();

//        textArea.setSize(maxWidth, Short.MAX_VALUE);  // important
//        Dimension pref = textArea.getPreferredSize();
//        textArea.setPreferredSize(pref);

        JLabel textArea = new JLabel("<html>" + message + "</html>");
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setOpaque(true);
        textArea.setBackground(bubbleColor);
        textArea.setForeground(textColor);
        textArea.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));


        this.setMaximumSize(new Dimension(maxWidth, textArea.getPreferredSize().height));
        this.add(textArea, BorderLayout.CENTER);

        // Set alignment based on sender
        this.setAlignmentX(isUser ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
    }
}