package com.example.components.user;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedDialog;
import com.example.listener.CreateGroupListener;
import com.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddMemberPopup {
    static List<User> selectedUsers = new ArrayList<>();
    public static void show(JFrame parent,
                            List<User> allUsers,
                            CreateGroupListener callback) {

        RoundedDialog dialog = new RoundedDialog(parent, "Create Group", 400, 500);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        // Member list (scrollable)
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(listPanel);

        refreshMemberList(listPanel, allUsers, selectedUsers, "");

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setMaximumSize(new Dimension( 300, 40));
        SearchBar searchBar = new SearchBar(20, 10, 300, 35, phrase -> {
            refreshMemberList(listPanel, allUsers, selectedUsers, phrase);
        });
        searchPanel.add(searchBar);

        // Bottom: buttons
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setOpaque(false);

        RoundedButton cancelBtn = new RoundedButton(20);
        cancelBtn.setText("Cancel");
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(MyColor.DARK_GRAY);
        cancelBtn.setFocusPainted(false);

        cancelBtn.addActionListener(e -> dialog.dispose());

        RoundedButton addBtn = new RoundedButton(20);
        addBtn.setText("Add");
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(MyColor.LIGHT_BLUE);
        addBtn.setFocusPainted(false);

        addBtn.addActionListener(e -> {

//            TODO: Call backend to add member
            System.out.print("added member");

            dialog.dispose();
        });

        buttonRow.add(Box.createVerticalStrut(10));
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(cancelBtn);
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(addBtn);
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(Box.createVerticalStrut(90));

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(searchPanel);

        scrollPane.setPreferredSize(new Dimension(380, 300));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(scrollPane);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonRow, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static void refreshMemberList(
            JPanel listPanel,
            List<User> allUsers,
            List<User> selectedUsers,
            String phrase
    ) {
        listPanel.removeAll();

        for (User u : allUsers) {
            if (!selectedUsers.contains(u) &&
                    !phrase.isEmpty() && !phrase.equals("Search")
                    && !u.getName().toLowerCase().contains(phrase.toLowerCase()))
                continue;

            AddMemberCard card = new AddMemberCard(u, 300);

            if (selectedUsers.contains(u)) {
                card.setBackground(MyColor.LIGHT_PURPLE.darker());
                card.setSelected(true);
            }

            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (selectedUsers.contains(u))
                        selectedUsers.remove(u);
                    else
                        selectedUsers.add(u);
                }
            });

            listPanel.add(card);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}
