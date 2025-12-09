package com.example.components.user;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedDialog;
import com.example.listener.CreateGroupListener;
import com.example.model.User;
import com.example.model.Group;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupPopup {

    public static void show(JFrame parent,
                            List<User> allUsers,
                            CreateGroupListener callback) {

        RoundedDialog dialog = new RoundedDialog(parent, "Create Group", 400, 500);
//        dialog.setSize(400, 500);
//        dialog.setLocationRelativeTo(parent);
//        dialog.setLayout(new BorderLayout());
//        dialog.setUndecorated(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        // Top: Input group name
        JTextField nameField = new JTextField();
        nameField.setOpaque(false);
        nameField.setBackground(MyColor.LIGHT_PURPLE);
        nameField.setMaximumSize(new Dimension(400, 30));
        nameField.setFont(nameField.getFont().deriveFont(16f));
        LineBorder lineBorder = new LineBorder(Color.BLACK, 1);
        nameField.setBorder(BorderFactory.createTitledBorder(lineBorder, "Group Name"));

        // Member list (scrollable)
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(listPanel);

        List<User> selectedUsers = new ArrayList<>();
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

        RoundedButton createBtn = new RoundedButton(20);
        createBtn.setText("Confirm");
        createBtn.setForeground(Color.WHITE);
        createBtn.setBackground(MyColor.LIGHT_BLUE);
        createBtn.setFocusPainted(false);

        createBtn.addActionListener(e -> {
            String groupName = nameField.getText().trim();
            if (groupName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Group name cannot be empty.");
                return;
            }

//            TODO: Call backend to create group
            Group group = new Group(groupName, selectedUsers);

            if (callback != null)
                callback.onGroupCreated(group);

            dialog.dispose();
        });

        buttonRow.add(Box.createVerticalStrut(10));
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(cancelBtn);
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(createBtn);
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(Box.createVerticalStrut(90));

        mainPanel.add(nameField);
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
        //TODO: auto select current user that create group with
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
