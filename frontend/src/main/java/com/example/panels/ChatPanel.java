package com.example.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

import com.example.dto.BaseMsgDTO;
import com.example.dto.GroupMsgDTO;
import com.example.dto.InboxMsgDTO;
import com.example.dto.request.SendGroupMsgReqDTO;
import com.example.dto.request.SendInboxMsgReqDTO;
import com.example.services.InboxService;
import com.example.services.LLMService;
import com.example.services.WebSocketManager;
import com.example.ui.ChatScreen;
import com.example.util.Utility;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.example.components.MyColor;
import com.example.components.user.MsgBubble;
import com.example.components.RoundedButton;
import com.example.components.RoundedTextArea;

import java.util.List;

public class ChatPanel extends JPanel {
    private final Border border = BorderFactory.createLineBorder(Color.black);
    private final JScrollPane scrollPane;
    private final JPanel chatArea;
    private final InboxService inboxService = new InboxService();
    private final LLMService llmService = new LLMService();

    private List<? extends BaseMsgDTO> currentMessages;
    private final JPanel suggestionPanel;
    private final JLabel suggestionLabel;
    private final ChatScreen mainFrame;

    public ChatPanel(ChatScreen mainFrame, int width, int height) {
        this.mainFrame = mainFrame;
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setBorder(border);
        this.setOpaque(false);

        //Chat Area
        chatArea = new JPanel();
        chatArea.setOpaque(false);
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.scrollPane = new JScrollPane(chatArea);
        this.scrollPane.setOpaque(false);
        this.scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        this.scrollPane.getViewport().setOpaque(false);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //Input Area
        JPanel inputArea = new JPanel(new BorderLayout(10, 0));
        inputArea.setOpaque(false);
        inputArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        RoundedTextArea inputField = new RoundedTextArea(20, 20);
        inputField.setLineWrap(true);
        inputField.setWrapStyleWord(true);

        JButton llmBtn = new JButton(new FlatSVGIcon("assets/robot-solid-full.svg", 20, 20));
        llmBtn.setContentAreaFilled(false);
        llmBtn.setBorder(null);
        llmBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        RoundedButton sendButton = new RoundedButton(20);
        sendButton.setFocusPainted(false);
        sendButton.setText("Send");
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(MyColor.LIGHT_BLUE);

        inputArea.add(llmBtn, BorderLayout.WEST);
        inputArea.add(inputField, BorderLayout.CENTER); // Chiếm diện tích còn lại
        inputArea.add(sendButton, BorderLayout.EAST);

        //Suggestion Panel
        suggestionPanel = new JPanel(new BorderLayout(10, 0));
        suggestionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        suggestionPanel.setBackground(new Color(240, 248, 255));
        suggestionPanel.setVisible(false);

        suggestionLabel = new JLabel();
        suggestionLabel.setForeground(Color.DARK_GRAY);

        RoundedButton replaceBtn = new RoundedButton(15);
        replaceBtn.setText("Replace");
        replaceBtn.setBackground(MyColor.LIGHT_BLUE);
        replaceBtn.setForeground(Color.WHITE);

        suggestionPanel.add(suggestionLabel, BorderLayout.CENTER);
        suggestionPanel.add(replaceBtn, BorderLayout.EAST);

        //South Container
        JPanel southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
        southContainer.setOpaque(false);
        southContainer.add(suggestionPanel);
        southContainer.add(inputArea);

        //Logics & Listeners
        llmBtn.addActionListener(e -> {
            String currentInput = inputField.getText().trim();
            List<String> recentMsgs = getLast10Messages();
            Long inboxId = mainFrame.getCurrentChat().isGroup()
                    ? mainFrame.getCurrentChat().getGroupId()
                    : mainFrame.getCurrentChat().getInboxId();

            new SwingWorker<String, Void>() {
                @Override protected String doInBackground() {
                    return llmService.getLLMSuggestion(inboxId, currentInput, recentMsgs);
                }
                @Override protected void done() {
                    try {
                        String suggestion = get();
                        suggestionLabel.setText(suggestion);
                        suggestionPanel.setVisible(true);
                        revalidate();
                    } catch (Exception ex) { ex.printStackTrace(); }
                }
            }.execute();
        });

        replaceBtn.addActionListener(e -> {
            inputField.setText(suggestionLabel.getText());
            suggestionPanel.setVisible(false);
        });

        sendButton.addActionListener(e -> {
            String message = inputField.getText().trim();
            if (message.isEmpty()) return;
            inputField.setText("");

            if (mainFrame.getCurrentChat().isGroup()) {
                SendGroupMsgReqDTO req = new SendGroupMsgReqDTO(mainFrame.getCurrentChat().getGroupId(), message);
                WebSocketManager.getInstance().send("/app/chat/group/send", req);
            } else {
                SendInboxMsgReqDTO req = new SendInboxMsgReqDTO(
                        mainFrame.getCurrentChat().getInboxId(),
                        mainFrame.getCurrentChat().getTargetUser().getFriendId(),
                        message);
                WebSocketManager.getInstance().send("/app/chat/inbox/send", req);
            }
        });

        inputField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                inputField.autoResize(30, 150); // Giới hạn chiều cao max 150px
                inputArea.revalidate();
                southContainer.revalidate();
                scrollToBottom(scrollPane);
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        this.add(this.scrollPane, BorderLayout.CENTER);
        this.add(southContainer, BorderLayout.SOUTH);
    }

    private void addMessage(JPanel chatPanel, String message, boolean isUser, int chatWidth, String avatarText) {
        JPanel messageWrapper = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));
        messageWrapper.setOpaque(false);
        MsgBubble bubble = new MsgBubble(message, isUser, chatWidth, avatarText);
        messageWrapper.add(bubble);
        messageWrapper.setMaximumSize(new Dimension(chatWidth, messageWrapper.getPreferredSize().height));
        chatPanel.add(messageWrapper);
    }

    private void scrollToBottom(JScrollPane scrollPane) {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));
    }

    public void showMessages(List<InboxMsgDTO> msgs, Long myId) {
        this.currentMessages = msgs;
        chatArea.removeAll();
        for (InboxMsgDTO msg : msgs) {
            boolean isMe = msg.getSenderId().equals(myId);
            addMessage(chatArea, msg.getContent(), isMe, getWidth(), Utility.getInitials(msg.getSenderName()));
        }
        updateUIState();
    }

    public void showGroupMessages(List<GroupMsgDTO> msgs, Long myId) {
        this.currentMessages = msgs;
        chatArea.removeAll();
        for (GroupMsgDTO msg : msgs) {
            boolean isMe = msg.getSenderId().equals(myId);
            addMessage(chatArea, msg.getContent(), isMe, getWidth(), Utility.getInitials(msg.getSenderUsername()));
        }
        updateUIState();
    }

    public void appendMessage(String content, boolean isMe, String senderName) {
        SwingUtilities.invokeLater(() -> {
            addMessage(chatArea, content, isMe, getWidth(), Utility.getInitials(senderName));
            updateUIState();
        });
    }

    private void updateUIState() {
        chatArea.revalidate();
        chatArea.repaint();
        scrollToBottom(scrollPane);
    }

    private List<String> getLast10Messages() {
        if (currentMessages == null || currentMessages.isEmpty()) return List.of();
        return currentMessages.stream()
                .skip(Math.max(0, currentMessages.size() - 10))
                .map(BaseMsgDTO::getContent)
                .toList();
    }

    public void clearChat() {
        chatArea.removeAll();
        chatArea.revalidate();
        chatArea.repaint();
    }
}