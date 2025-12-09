package com.example.ui;

import com.example.components.admin.*;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public AdminDashboard() {
        // Sidebar and TopBar
        Sidebar sidebar = new Sidebar();
        TopBar topbar = new TopBar();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // === CardLayout container ===
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // === Add pages to card panel ===
        UserList userListPage = new UserList();
        ChatGroupList chatGroupListPage = new ChatGroupList();
        LoginActivity loginActivityPage = new LoginActivity();
        Spam spamPage = new Spam();
        Active activePage = new Active();
        Friend friendPage = new Friend();
        NewUser newUserPage = new NewUser();
        GraphPage graphPage = new GraphPage();
        setTitle("Chat system");

        cardPanel.add(userListPage, "UserList");
        cardPanel.add(chatGroupListPage, "ChatGroupList");
        cardPanel.add(loginActivityPage, "LoginActivity");
        cardPanel.add(spamPage, "Spam");
        cardPanel.add(activePage, "Active");
        cardPanel.add(friendPage, "Friend");
        cardPanel.add(newUserPage, "NewUser");
        cardPanel.add(graphPage, "GraphPage");

        // === Add components to frame ===
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(topbar, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);

        // === Sidebar button listener ===
        sidebar.setButtonListener(buttonName -> {
            // buttonName should match your card names above
            cardLayout.show(cardPanel, buttonName);
            cardPanel.revalidate();
            cardPanel.repaint();
        });
        pack();
        setVisible(true);
    }
}
