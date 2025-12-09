package com.example.ui;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.model.User;

import javax.swing.*;
import java.awt.*;

public class ProfilePopup extends JDialog {
    User user;
    JPanel content = new JPanel();
    JLabel title = new JLabel();
    JFrame parent;

    public ProfilePopup(JFrame parent) {
        super(parent, "Profile", true); // true = modal
        this.parent = parent;

        user = new User();
        showProfile();
        this.setLocationRelativeTo(parent);
    }

    private void addField(GridBagConstraints gbc, int row, String labelText, JComponent input) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        content.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        content.add(input, gbc);
    }

    private void showProfile(){
        this.setTitle("User profile");
        this.setSize(400,410);
        this.setResizable(false);

        this.getContentPane().removeAll();
        content.removeAll();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        title = new JLabel("Profile",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(16f));
        title.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));

        gbc.insets = new Insets(10, 10, 10, 10); // spacing between rows
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        addField(gbc, row++, "Username:", new JTextField(user.getName(), 20));
        addField(gbc, row++, "Name:", new JTextField(user.getName(), 20));

        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setSelectedItem(user.getGender());
        genderCombo.setBackground(Color.WHITE);        // the closed combo

        ((JComponent)genderCombo.getRenderer()).setBackground(Color.WHITE);

        addField(gbc, row++, "Gender:", genderCombo);
        addField(gbc, row++, "BirthDay:", new JTextField(user.getBirthDay(), 20));
        addField(gbc, row++, "Address:", new JTextField(user.getAddress(), 20));
        addField(gbc, row++, "Email", new JTextField(user.getEmail(), 20) );

        RoundedButton changePass = new RoundedButton(10);
        changePass.setBackground(MyColor.LIGHT_BLUE);
        changePass.setForeground(Color.WHITE);
        changePass.setText("Change Password");
        changePass.setFocusPainted(false);
        changePass.addActionListener(e -> showChangePasswordForm());
        addField(gbc, row++, "Password", changePass);

        RoundedButton confirm = new RoundedButton(20);
        confirm.setText("Confirm");
        confirm.setBackground(MyColor.LIGHT_BLUE);
        confirm.setForeground(Color.WHITE);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST; // align right
        gbc.insets = new Insets(10, 10, 10, 30);
        gbc.fill = GridBagConstraints.NONE;   // don't stretch
        content.add(confirm, gbc);

        this.add(title, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private void showChangePasswordForm() {
        //Change title
        setTitle("Change Password");
        setSize(400, 260);

        title = new JLabel("Change Password",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(16f));
        title.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));

        //Remove old content
        this.getContentPane().removeAll();
        content.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        //Add new password fields
        addField(gbc, row++, "Old Password:", new JPasswordField(20));
        addField(gbc, row++, "New Password:", new JPasswordField(20));
        addField(gbc, row++, "Confirm New Password:", new JPasswordField(20));

        //Add Submit button (bottom-right)
        RoundedButton back = new RoundedButton(20);
        back.setText("Back");
        back.addActionListener(e -> showProfile());
        back.setBackground(MyColor.DARK_GRAY);
        back.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 30, 10, 10);
        content.add(back, gbc);

        RoundedButton submit = new RoundedButton(20);
        submit.setText("Submit");
        submit.setBackground(MyColor.LIGHT_BLUE);
        submit.setForeground(Color.WHITE);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST; // align right
        gbc.insets = new Insets(10, 10, 10, 30);
//        gbc.fill = GridBagConstraints.NONE;   // don't stretch
        content.add(submit, gbc);

        // 5. Refresh UI
        this.add(title, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }


}
