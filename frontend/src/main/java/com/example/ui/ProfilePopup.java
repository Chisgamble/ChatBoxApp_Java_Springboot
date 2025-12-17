package com.example.ui;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.dto.UserDTO;
import com.example.dto.UserMiniDTO;
import com.example.dto.request.AdminCreateOrUpdateUserReqDTO;
import com.example.dto.request.ChangePasswordReqDTO;
import com.example.dto.response.GeneralResDTO;
import com.example.listener.GroupListener;
import com.example.listener.ProfileListener;
import com.example.model.User;
import com.example.services.AuthService;
import com.example.services.UserService;

import javax.swing.*;
import java.awt.*;

public class ProfilePopup extends JDialog {
    UserDTO user;
    JPanel content = new JPanel();
    JLabel title = new JLabel();
    JFrame parent;

    static final AuthService authService = new AuthService();
    private final UserService userService = new UserService();

    private ProfileListener updateListener;

    private JTextField txtUsername, txtName, txtBirthday, txtAddress, txtEmail;
    private JComboBox<String> comboGender;

    public ProfilePopup(JFrame parent, Long userId, ProfileListener listener) {
        super(parent, "Profile", true); // true = modal
        this.parent = parent;
        this.user = userService.getInfo(userId);
        System.out.println("Test user:" + user.toString());
        this.updateListener = listener;

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
        txtUsername = new JTextField(user.username(), 20);
        txtName = new JTextField(user.name(), 20);
        txtAddress = new JTextField(user.address(), 20);
        txtBirthday = new JTextField(user.dob(), 20); // user.dob() trả về String
        txtEmail = new JTextField(user.email(), 20);

        comboGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        comboGender.setSelectedItem(user.gender());
        comboGender.setBackground(Color.WHITE);
        ((JComponent)comboGender.getRenderer()).setBackground(Color.WHITE);

        addField(gbc, row++, "Username:", txtUsername);
        addField(gbc, row++, "Name:", txtName);
        addField(gbc, row++, "Gender:", comboGender);
        addField(gbc, row++, "BirthDay:", txtBirthday);
        addField(gbc, row++, "Address:", txtAddress);
        addField(gbc, row++, "Email:", txtEmail);

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

        confirm.addActionListener(e -> handleUpdateProfile());

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
        JPasswordField oldPassField = new JPasswordField( 20);
        JPasswordField newPassField = new JPasswordField( 20);
        JPasswordField confirmPassField = new JPasswordField( 20);
        addField(gbc, row++, "Old Password", oldPassField);
        addField(gbc, row++, "New Password:", newPassField);
        addField(gbc, row++, "Confirm New Password:", confirmPassField);

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

        submit.addActionListener(e -> {
            String oldPass = new String(oldPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.");
                return;
            }

            // Disable button during loading
            submit.setEnabled(false);

            // Run API call in background
            SwingWorker<GeneralResDTO, Void> worker = new SwingWorker<>() {
                @Override
                protected GeneralResDTO doInBackground() throws Exception {
                    return authService.changePassword(oldPass, newPass);
                }

                @Override
                protected void done() {
                    submit.setEnabled(true); // re-enable
                    try {
                        GeneralResDTO res = get();
                        JOptionPane.showMessageDialog(
                            ProfilePopup.this,
                            res.message() != null ? res.message() : "Password changed!"
                        );
                        showProfile();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                            ProfilePopup.this,
                            "Error: " + ex.getMessage(),
                            "Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            worker.execute();
        });


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

    private void handleUpdateProfile() {
        AdminCreateOrUpdateUserReqDTO req = new AdminCreateOrUpdateUserReqDTO(
                txtUsername.getText().trim(),
                txtName.getText().trim(),
                null,
                (String) comboGender.getSelectedItem(),
                txtAddress.getText().trim(),
                txtEmail.getText().trim(),
                txtBirthday.getText().trim(),
                null
        );

        SwingWorker<UserDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected UserDTO doInBackground() throws Exception {
                return userService.updateProfile(user.id(), req);
            }

            @Override
            protected void done() {
                try {
                    UserDTO updatedUser = get();
                    JOptionPane.showMessageDialog(ProfilePopup.this, "Profile updated!");

                    // GỌI LISTENER TẠI ĐÂY
                    if (updateListener != null) {
                        updateListener.onProfileUpdated(updatedUser);
                    }

                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProfilePopup.this, "Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

}
