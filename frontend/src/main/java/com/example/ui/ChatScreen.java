package com.example.ui;
import javax.swing.*;
import java.awt.*;

import java.util.List;

import com.example.dto.FriendCardDTO;
import com.example.dto.GroupCardDTO;
import com.example.dto.InboxDTO;
import com.example.dto.UserMiniDTO;
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
import com.example.services.AuthService;
import com.example.services.FriendService;
import com.example.services.GroupService;
import com.example.services.InboxService;

public class ChatScreen extends JFrame implements CreateGroupListener, LogoutListener, FriendCardListener {
    UserMiniDTO user;
    List<InboxDTO> inboxes;
    List<FriendCardDTO> friends;
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
            List<FriendCardDTO> friends = friendService.getAll(user.getId());
            FriendCardDTO initialFriend = friends.get(0);
            InboxUserResDTO initialInbox = inboxService.getInboxWithMessages(initialFriend.getInboxId());

            ChatContext ctx = new ChatContext();
            ctx.setGroup(false);
            ctx.setAdmin(false);
            ctx.setInboxId(initialFriend.getInboxId());
            ctx.setTargetUser(initialInbox.getFriend());

            this.currentChat = ctx;


            chatPanel = new ChatPanel(width * 5, height);
            chatUtilPanel = new ChatUtilPanel(this, width * 2, height, initialInbox.getFriend());
            userUtilPanel = new UserUtilPanel(this,width * 2, height, user, friends);
            this.add(chatPanel, BorderLayout.CENTER);
            this.add(chatUtilPanel, BorderLayout.EAST);
            this.add(userUtilPanel, BorderLayout.WEST);

            this.setVisible(true);

            SwingUtilities.invokeLater(() -> {
                chatPanel.showMessages(initialInbox, user.getId());
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
            ctx.setTargetUser(inbox.getFriend());

            this.currentChat = ctx;

            chatPanel.showMessages(inbox, user.getId());
            chatUtilPanel.showUser(inbox.getFriend());
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Unable to load inbox: " + ex.getMessage());
        }
    }

    public void openGroupChat(GroupCardDTO group) {

        GroupUserResDTO res =
                groupService.getInfoAndMsgs(group.getId());

        ChatContext ctx = new ChatContext();
        ctx.setGroup(true);
        ctx.setGroupId(group.getId());
//        ctx.setTargetGroup(res.getUserInGroup());

        this.currentChat = ctx;

        chatPanel.showGroupMessages(res, user.getId());
        chatUtilPanel.showGroup(group, res.getUserInGroup());
    }


    @Override
    public void onGroupCreated(Group group) {
        System.out.println("New group created: " + group.getName());

        reloadGroups(); // Temporary
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

}
