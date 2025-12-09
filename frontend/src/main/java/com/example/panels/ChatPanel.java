package com.example.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.example.components.MyColor;
import com.example.components.user.MsgBubble;
import com.example.components.RoundedButton;
import com.example.components.RoundedTextArea;

public class ChatPanel extends JPanel{
    Border border = BorderFactory.createLineBorder(Color.black);
    JScrollPane scrollPane;

    public ChatPanel(int width, int height){
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setBorder(border);
        this.setOpaque(false);

        JPanel chatArea = new JPanel();
        chatArea.setOpaque(false);
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.scrollPane = new JScrollPane(chatArea);
        this.scrollPane.setOpaque(false);
        this.scrollPane.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        this.scrollPane.getViewport().setOpaque(false);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        addMessage(chatArea, "Hello! How are you?", false, width, "A");
        addMessage(chatArea, "I'm doing great, thanks for asking!", true, width, "A");
        addMessage(chatArea, "That's wonderful to hear. What are you working on today? Besides, I'm building a chatbox interface with bubble messages in Java Swing! It's really hard and takes lots of time. I've only making the UI and already wanna quit halfway. Wish there was another easier way to do this without grinding all the components tutorials.", false, width, "A");
        addMessage(chatArea, "I'm building a chatbox interface with bubble messages in Java Swing! It's really hard and takes lots of time. I've only making the UI and already wanna quit halfway. Wish there was another easier way to do this without grinding all the components tutorials.", true, width, "A");

        for (int i = 0; i < 15; i++){
            addMessage(chatArea, "Hello! How are you?", i%2 == 0, width, "A");
        }

        JPanel inputArea = new JPanel(new FlowLayout( FlowLayout.LEFT, 10,5));
        inputArea.setOpaque(false);
        inputArea.setPreferredSize(new Dimension(width - 10, 50 ));

        JButton LLM = new JButton(new FlatSVGIcon("assets/robot-solid-full.svg", 20,20));
        LLM.setContentAreaFilled(false);
        LLM.setBorder(null);

        RoundedButton sendButton = new RoundedButton(20);
        sendButton.setFocusPainted(false);
        sendButton.setFocusable(false);
        sendButton.setText("send");
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(MyColor.LIGHT_BLUE);

        RoundedTextArea inputField = new RoundedTextArea(20, 10);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText().trim();
                if (!message.isEmpty()) {
                    addMessage(chatArea, message, true, width, "A");  // Add the message to the chat
                    inputField.setText("");  // Clear the input field
                }
            }
        });

        Dimension buttonSize = sendButton.getPreferredSize();

        inputField.setPreferredSize(new Dimension(width - buttonSize.width - LLM.getPreferredSize().width - 40, 30));
//        textArea.setMinimumSize(new Dimension(0, 5));
//        textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));

        inputArea.add(LLM);
        inputArea.add(inputField);
        inputArea.add(sendButton);

        scrollToBottom(this.scrollPane);
        this.add(inputArea, BorderLayout.SOUTH);
        this.add(this.scrollPane, BorderLayout.CENTER);
    }

    private void addMessage(JPanel chatPanel, String message, boolean isUser, int chatWidth, String avatarText) {
        JPanel messageWrapper = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));
        messageWrapper.setOpaque(false);

        MsgBubble bubble = new MsgBubble(message, isUser, chatWidth, avatarText);

        messageWrapper.add(bubble);
        messageWrapper.validate();
        messageWrapper.setMaximumSize(new Dimension(chatWidth, messageWrapper.getPreferredSize().height));

        chatPanel.add(messageWrapper);
        chatPanel.revalidate();
        scrollToBottom(this.scrollPane);
        chatPanel.repaint();
    }

    private void scrollToBottom(JScrollPane scrollPane) {
        // Get the vertical scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();

        // Set the value of the scrollbar to its maximum to scroll to the bottom
        SwingUtilities.invokeLater(() -> {
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }

}
