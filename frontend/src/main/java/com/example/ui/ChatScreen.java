package com.example.ui;
import javax.swing.*;
import java.awt.*;

import java.util.List;

import com.example.dto.*;
import com.example.dto.response.GroupUserResDTO;
import com.example.dto.response.InboxUserResDTO;
import com.example.listener.*;
import com.example.panels.ChatPanel;
import com.example.panels.ChatUtilPanel;
import com.example.panels.UserUtilPanel;
import com.example.services.*;
import lombok.Getter;

@Getter
public class ChatScreen extends JFrame
        implements CreateGroupListener,
        LogoutListener,
        FriendCardListener,
        GroupMemberActionListener,
        GroupListener {

    private final UserMiniDTO user;
    List<InboxDTO> inboxes;
    private List<FriendCardDTO> friends;

    UserService userService = new UserService();
    FriendService friendService = new FriendService();
    InboxService inboxService = new InboxService();
    GroupService groupService = new GroupService();

    ChatPanel chatPanel;
    ChatUtilPanel chatUtilPanel;
    UserUtilPanel userUtilPanel;

    ChatContext currentChat;

    public ChatScreen(UserMiniDTO user){
        this.user = user;
        try {
            WebSocketManager.getInstance().connect();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Failed to connect websocket: " + ex.getMessage());
            System.out.println("[ERROR] Failed to connect websocket::  " + ex.getMessage());
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.getContentPane().setBackground(Color.WHITE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width / 9;
        int height = screen.height;
        try {  
            friends = friendService.getAll(user.getId());
            InboxUserResDTO initialInbox = null;
            FriendCardDTO initialFriend = null;
            UserMiniDTO friend = null;
            ChatContext ctx = new ChatContext();

            ctx.setGroup(false);

            if (!friends.isEmpty()){
                initialFriend = friends.get(0);
                initialInbox = inboxService.getInboxWithMessages(initialFriend.getInboxId());
                friend = initialInbox.getFriend();
                ctx.setInboxId(initialFriend.getInboxId());
                ctx.setTargetUser(initialFriend);
            }

            ctx.setThisUser(user);
            this.currentChat = ctx;
            System.out.println(currentChat.getThisUser().getUsername());

            chatPanel = new ChatPanel(this,width * 5, height);
            chatUtilPanel = new ChatUtilPanel(this, width * 2, height, friend);
            userUtilPanel = new UserUtilPanel(this,width * 2, height, user, friends);
            this.add(chatPanel, BorderLayout.CENTER);
            this.add(chatUtilPanel, BorderLayout.EAST);
            this.add(userUtilPanel, BorderLayout.WEST);

            this.setVisible(true);

            if (initialInbox != null) {
                final InboxUserResDTO inbox = initialInbox;
                SwingUtilities.invokeLater(() -> {
                    chatPanel.showMessages(inbox.getMsgs(), user.getId());
                    userUtilPanel.selectFriendFromContext();
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load inbox: " + ex.getMessage());
            System.out.println("[ERROR] Inbox in ChatScreen failed:  " + ex.getMessage());
        }
    }

    public ChatContext getContext(){
        return currentChat;
    }

    public void openInbox(FriendCardDTO friend) {
        try {
            InboxUserResDTO inbox =
                    inboxService.getInboxWithMessages(friend.getInboxId());

            currentChat.setGroup(false);
            currentChat.setInboxId(friend.getInboxId());
            currentChat.setTargetUser(friend);

            chatPanel.showMessages(inbox.getMsgs(), user.getId());
            chatUtilPanel.setInboxMessages(inbox.getMsgs());
            chatUtilPanel.showUser(inbox.getFriend());

            WebSocketManager.getInstance()
                .subscribeInbox(friend.getInboxId(), msg -> {
                    SwingUtilities.invokeLater(() -> {
                        chatPanel.appendMessage(
                            msg.getContent(),
                                msg.getSenderId() == currentChat.getThisUser().getId(),
                            msg.getSenderName());
                    });
                });
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Unable to load inbox: " + ex.getMessage());
        }
    }

    public void reloadCurrentInbox() {
        if (currentChat == null || currentChat.isGroup()) return;

        InboxUserResDTO inbox =
                inboxService.getInboxWithMessages(currentChat.getInboxId());

        chatPanel.showMessages(inbox.getMsgs(), user.getId());
        chatUtilPanel.setInboxMessages(inbox.getMsgs());
    }

    public void reloadCurrentGroupChat() {
        if (currentChat == null || !currentChat.isGroup()) return;

        GroupUserResDTO res =
                groupService.getInfoAndMsgs(currentChat.getGroupId());

        chatPanel.showGroupMessages(res.getMsgs(), user.getId());
        chatUtilPanel.setGroupMessages(res.getMsgs());
    }

    public void openGroupChat(GroupCardDTO group) {

        GroupUserResDTO res =
                groupService.getInfoAndMsgs(group.getId());

        currentChat.setGroup(true);
        currentChat.setGroupId(group.getId());
        currentChat.setTargetGroup(group);
        currentChat.setUserInGroup(res.getUserInGroup());

        List<GroupMemberDTO> members = groupService.getAllMembers(group.getId());
        chatUtilPanel.setAllMembers(members);

        chatPanel.showGroupMessages(res.getMsgs(), user.getId());
        chatUtilPanel.setGroupMessages(res.getMsgs());
        chatUtilPanel.showGroup(group, res.getUserInGroup().getRole());

        WebSocketManager.getInstance()
            .subscribeGroup(group.getId(), msg -> {
                SwingUtilities.invokeLater(() -> {
                    chatPanel.appendMessage(
                        msg.getContent(),
                        msg.getSenderId() == currentChat.getThisUser().getId(),
                        msg.getSenderName());
                });
            });
    }


    @Override
    public void onGroupCreated(String groupName, List<Long> memberIds) {
        try{
            GroupService groupService = new GroupService();
            GroupCardDTO res = groupService.createGroup(groupName, memberIds);
            List<GroupMemberDTO> mems = groupService.getAllMembers(res.getId());

            currentChat.setGroup(true);
            currentChat.setGroupId(res.getId());
            currentChat.setTargetGroup(res);

            chatPanel.clearChat();
            chatUtilPanel.setAllMembers(mems);
            chatUtilPanel.showGroup(res, "admin");
            userUtilPanel.onMenuOptionSelected("Groups");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
    }

    public void reloadGroups() {
        System.out.println("[ChatScreen] refreshing groups...");
        // later: call backend → update UI list
    }

    @Override
    public void onLogout(){
        try{
            JOptionPane.showMessageDialog(this, "Logout successfully!");
            WebSocketManager.getInstance().disconnect();
            AuthService authService = new AuthService();
            authService.logout();
            this.dispose();
            new Login();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    @Override
    public void onFriendSelected(FriendCardDTO friend) {
        openInbox(friend);
    }

    public void handleConfirmedOption(String option) {

        ChatContext ctx = this.currentChat;

        try {
            switch (option) {

                case "Unfriend" -> {
                    Long targetUserId = ctx.getTargetUser().getFriendId();
                    friendService.deleteFriend(targetUserId);

                    SwingUtilities.invokeLater(() -> {
                        userUtilPanel.removeFriend(targetUserId);
                        chatPanel.clearChat();
                        chatUtilPanel.clear();
                    });
                }

                case "Delete All Chat History" -> {
                    if (ctx.isGroup()) {
                        groupService.deleteAllMessages(ctx.getGroupId());
                    } else {
                        inboxService.deleteAllMessages(ctx.getInboxId());
                    }
                    chatPanel.clearChat();
                }

                case "Block" -> {
                    userService.blockUser(ctx.getTargetUser().getFriendId());
                    userUtilPanel.removeFriend(ctx.getTargetUser().getFriendId());
                    chatPanel.clearChat();
                }

                case "Leave Group" -> {
                    groupService.leaveGroup(ctx.getGroupId());
                    userUtilPanel.removeGroup(ctx.getGroupId());
                    chatPanel.clearChat();
                    chatUtilPanel.clear();
                }

                case "Delete Group" -> {
                    try{
                        groupService.deleteGroup(ctx.getGroupId());
                        userUtilPanel.removeGroup(ctx.getGroupId());
                        chatUtilPanel.clear();
                        chatPanel.clearChat();
                    }catch (Exception e){
                        JOptionPane.showMessageDialog(this, "Failed to delete group\n" + e.getMessage());
                    }
                }

                case "Report Spam" -> {
                    userService.reportSpam(ctx.getTargetUser().getFriendId());
                    JOptionPane.showMessageDialog(this, "Reported successfully");
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Action failed: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void onRemoveMember(Long groupId, Long userId) {
        try {
            groupService.removeMember(groupId, userId);

            SwingUtilities.invokeLater(() -> {
                chatUtilPanel.removeMember(userId);
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Remove member failed: " + e.getMessage());
        }
    }

    @Override
    public void onPromoteMember(Long groupId, Long userId) {
        try {
            groupService.promoteMember(groupId, userId);

            SwingUtilities.invokeLater(() -> {
                chatUtilPanel.updateMemberRole(userId, "admin");
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Promote member failed: " + e.getMessage());
        }
    }

    @Override
    public void onAddMembers(Long groupId, List<Long> userIds) {
        try {
            groupService.addMembers(groupId, userIds, currentChat.getThisUser().getId());

            List<GroupMemberDTO> members =
                    groupService.getAllMembers(groupId);

            chatUtilPanel.setAllMembers(members);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    @Override
    public void onGroupNameChanged(String newName) {
        try {
            GroupCardDTO updated =
                    groupService.changeGroupName(currentChat.getGroupId(), newName);

            currentChat.getTargetGroup().setGroupname(updated.getGroupname());

            // update UI đồng bộ
            chatUtilPanel.showGroup(updated, currentChat.getUserInGroup().getRole());
            userUtilPanel.updateGroupName(updated.getId(), updated.getGroupname());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to change group name");
        }
    }


}
