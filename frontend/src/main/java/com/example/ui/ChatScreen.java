package com.example.ui;
import javax.swing.*;
import java.awt.*;

import java.util.List;

import com.example.dto.*;
import com.example.dto.request.CreateGroupReqDTO;
import com.example.dto.response.GroupUserResDTO;
import com.example.dto.response.InboxUserResDTO;
import com.example.dto.response.LoginResDTO;
import com.example.listener.CreateGroupListener;
import com.example.listener.FriendCardListener;
import com.example.listener.LogoutListener;
import com.example.model.Group;
import com.example.panels.ChatPanel;
import com.example.panels.ChatUtilPanel;
import com.example.panels.UserUtilPanel;
import com.example.services.*;
import lombok.Getter;

@Getter
public class ChatScreen extends JFrame implements CreateGroupListener, LogoutListener, FriendCardListener {
    UserMiniDTO user;
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
            FriendCardDTO initialFriend = friends.get(0);
            InboxUserResDTO initialInbox = inboxService.getInboxWithMessages(initialFriend.getInboxId());

            ChatContext ctx = new ChatContext();
            ctx.setGroup(false);
            ctx.setAdmin(false);
            ctx.setInboxId(initialFriend.getInboxId());
            ctx.setTargetUser(initialFriend);
            ctx.setThisUser(user);

            this.currentChat = ctx;

            chatPanel = new ChatPanel(width * 5, height);
            chatUtilPanel = new ChatUtilPanel(this, width * 2, height, initialInbox.getFriend());
            userUtilPanel = new UserUtilPanel(this,width * 2, height, user, friends);
            this.add(chatPanel, BorderLayout.CENTER);
            this.add(chatUtilPanel, BorderLayout.EAST);
            this.add(userUtilPanel, BorderLayout.WEST);

            this.setVisible(true);

            SwingUtilities.invokeLater(() -> {
                chatPanel.showMessages(initialInbox.getMsgs(), user.getId());
                userUtilPanel.selectFriendFromContext();
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load inbox: " + ex.getMessage());
            System.out.println("[ERROR]  " + ex.getMessage());
        }
    }

    public ChatContext getContext(){
        return currentChat;
    }

    public void openInbox(FriendCardDTO friend) {
        try {
            InboxUserResDTO inbox =
                    inboxService.getInboxWithMessages(friend.getInboxId());

            ChatContext ctx = new ChatContext();
            ctx.setGroup(false);
            ctx.setInboxId(friend.getInboxId());
            ctx.setTargetUser(friend);

            this.currentChat = ctx;

            chatPanel.showMessages(inbox.getMsgs(), user.getId());
            chatUtilPanel.setInboxMessages(inbox.getMsgs());
            chatUtilPanel.showUser(inbox.getFriend());
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


    public void openGroupChat(GroupCardDTO group) {

        GroupUserResDTO res =
                groupService.getInfoAndMsgs(group.getId());

        ChatContext ctx = new ChatContext();
        ctx.setGroup(true);
        ctx.setGroupId(group.getId());
        ctx.setTargetGroup(group);

        this.currentChat = ctx;

        List<GroupMemberDTO> members = groupService.getAllMembers(group.getId());
        chatUtilPanel.setAllMembers(members);

        chatPanel.showGroupMessages(res, user.getId());
        chatUtilPanel.setGroupMessages(res.getMsgs());
        chatUtilPanel.showGroup(group, res.getUserInGroup().getRole());
    }


    @Override
    public void onGroupCreated(String groupName, List<Long> memberIds) {
        try{
            GroupService groupService = new GroupService();
            GroupCardDTO res = groupService.createGroup(groupName, memberIds);
            List<GroupMemberDTO> mems = groupService.getAllMembers(res.getId());

            ChatContext ctx = new ChatContext();
            ctx.setGroup(true);
            ctx.setGroupId(res.getId());
            ctx.setTargetGroup(res);

            this.currentChat = ctx;

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
            this.dispose(); // Close chat window
            new Login();    // Open Login window
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
                }

                case "Delete Group" -> {
                    try{
                        groupService.deleteGroup(ctx.getGroupId());
                        userUtilPanel.removeGroup(ctx.getGroupId());
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


}
