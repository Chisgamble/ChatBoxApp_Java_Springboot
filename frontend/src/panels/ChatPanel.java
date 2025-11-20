package panels;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

import components.MsgBubble;
import components.SendMsg;

public class ChatPanel extends JPanel{
    Border border = BorderFactory.createLineBorder(Color.black);
    public ChatPanel(int width, int height){
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setBorder(border);
        this.setBackground(Color.WHITE);

        JPanel chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setOpaque(false);
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addMessage(chatArea, "Hello! How are you?", false, width, "A");
        addMessage(chatArea, "I'm doing great, thanks for asking!", true, width, "A");
        addMessage(chatArea, "That's wonderful to hear. What are you working on today? Besides, I'm building a chatbox interface with bubble messages in Java Swing! It's really hard and takes lots of time. I've only making the UI and already wanna quit halfway. Wish there was another easier way to do this without grinding all the components tutorials.", false, width, "A");
        addMessage(chatArea, "I'm building a chatbox interface with bubble messages in Java Swing! It's really hard and takes lots of time. I've only making the UI and already wanna quit halfway. Wish there was another easier way to do this without grinding all the components tutorials.", true, width, "A");

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(new SendMsg(width, 50), BorderLayout.SOUTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private static void addMessage(JPanel chatPanel, String message, boolean isUser, int chatWidth, String avatarText) {
        JPanel messageWrapper = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));
        messageWrapper.setOpaque(false);

//        messageWrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        MsgBubble bubble = new MsgBubble(message, isUser, chatWidth, avatarText);

//        if (isUser) {
//            messageWrapper.add(Box.createHorizontalGlue());
//            messageWrapper.add(bubble);
//        } else {
//            messageWrapper.add(bubble);
//            messageWrapper.add(Box.createHorizontalGlue());
//        }
        messageWrapper.add(bubble);

        chatPanel.add(messageWrapper);
        chatPanel.revalidate();
        chatPanel.repaint();
    }
}
