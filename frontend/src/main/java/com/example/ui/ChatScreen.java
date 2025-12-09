package com.example.ui;
import javax.swing.*;
import java.awt.*;

import java.util.List;

import com.example.dto.FriendCardDTO;
import com.example.dto.InboxDTO;
import com.example.dto.response.LoginResDTO;
import com.example.listener.CreateGroupListener;
import com.example.listener.LogoutListener;
import com.example.model.Group;
import com.example.panels.ChatPanel;
import com.example.panels.ChatUtilPanel;
import com.example.panels.UserUtilPanel;
import com.example.services.FriendService;

public class ChatScreen extends JFrame implements CreateGroupListener, LogoutListener {
    LoginResDTO user;
    List<InboxDTO> inboxes;
    List<FriendCardDTO> friends;
    FriendService friendService = new FriendService();

    public ChatScreen(LoginResDTO user){
        this.user = user;
        friends = friendService.getAll(user.id());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.getContentPane().setBackground(Color.WHITE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width / 9;
        int height = screen.height;

        this.add(new ChatPanel(width * 5, height), BorderLayout.CENTER);
        this.add(new ChatUtilPanel(this, width * 2, height, true, false), BorderLayout.EAST);
        this.add(new UserUtilPanel(this,width * 2, height, inboxes, friends), BorderLayout.WEST);

        this.setVisible(true);
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
}
