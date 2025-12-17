package com.example.ui;

import com.example.dto.UserMiniDTO;
import com.example.dto.response.LoginResDTO;
import com.example.services.AuthService;
import com.example.services.WebSocketManager;
import com.example.util.Utility;
import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedPasswordField;
import com.example.components.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Login extends JFrame{
    public Login() throws IOException, FontFormatException {
        setTitle("Chat system");
        try {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            // Panel
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 40, 10, 40); // spacing around components
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.setMaximumSize(new Dimension(600, 175));

            Font font = new Font("SansSerif", Font.BOLD, 13);
            //Sign up
            JLabel login = Utility.makeText("LOGIN", font, 75f, Font.BOLD, MyColor.LIGHT_BLUE, null);
            login.setAlignmentX(0.5f);
            login.setMaximumSize(new Dimension(login.getMaximumSize().width + 40, 75));
            // or log in
            JLabel or_signup = Utility.makeText("or signup", font, 20f, Font.PLAIN, MyColor.LIGHT_BLUE, null);
            or_signup.setAlignmentX(0.5f);
            or_signup.setCursor(new Cursor(Cursor.HAND_CURSOR));
            or_signup.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Login.this.dispose();
                        new Signup();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(Login.this, "Error: " + ex.getMessage());
                    }
                }
            });

            // Username
            JLabel usr_label = Utility.makeText("Username/email:", font, 20f, Font.BOLD, MyColor.LIGHT_BLACK, null);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0;
            panel.add(usr_label, gbc);

            RoundedTextField usr_name = new RoundedTextField(20);
            usr_name.setFont(font.deriveFont(Font.PLAIN, 18f));
            usr_name.setMaximumSize(new Dimension(325, 45));
            usr_name.setPreferredSize(new Dimension(325, 45));
            usr_name.setBackground(Color.WHITE);
            usr_name.setAlignmentY(Component.TOP_ALIGNMENT);

            usr_name.setAlignmentX(0.5f);
            usr_name.setToolTipText("Username or email");
            usr_name.setForeground(Color.GRAY);

            gbc.gridx = 1;
            gbc.weightx = 0;
            panel.add(usr_name, gbc);

            // Password
            JLabel pass_label = Utility.makeText("Password:", font, 20f, Font.BOLD, MyColor.LIGHT_BLACK, null);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            panel.add(pass_label, gbc);

            RoundedPasswordField password = new RoundedPasswordField(20);
            password.setFont(font.deriveFont(Font.PLAIN, 18f));
            password.setMaximumSize(new Dimension(325, 45));
            password.setPreferredSize(new Dimension(325, 45));
            password.setBackground(Color.WHITE);
            password.setAlignmentY(Component.TOP_ALIGNMENT);

            password.setAlignmentX(0.5f);
            password.setToolTipText("Password");
            password.setForeground(Color.GRAY);

            gbc.gridx = 1;
            gbc.weightx = 1;
            panel.add(password, gbc);

            JLabel forgot_password = new JLabel("Forgot password?");
            forgot_password.setFont(font.deriveFont(Font.PLAIN, 16f));
            forgot_password.setForeground(new Color(0xa2a2a2));
            forgot_password.setAlignmentX(0.5f);
            forgot_password.setBackground(MyColor.LIGHT_BLACK);
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(forgot_password, gbc);

            // Row 3: Submit Button
            RoundedButton submit = new RoundedButton(15);
            submit.setText("SUBMIT");
            submit.setFont(font.deriveFont(Font.BOLD, 20f));
            submit.setBackground(MyColor.LIGHT_BLUE);
            submit.setForeground(Color.WHITE);
            submit.setFocusPainted(false);
            submit.setPreferredSize(new Dimension(150, 45));
            submit.setMaximumSize(new Dimension(150, 45));
            submit.setAlignmentX(0.5f);

            submit.addActionListener(e -> {
                String email = usr_name.getText();
                String pwd = new String(password.getPassword());

                try {
                    AuthService authService = new AuthService();
                    LoginResDTO user = authService.login(email, pwd);
                    JOptionPane.showMessageDialog(this, "Login successfully!");
                    this.dispose();
                    if (user.getUser().getRole().equals("user"))
                        new ChatScreen(user.getUser());
                    else
                        new AdminDashboard(user.getUser());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });

            // Add vertical glue for “justify-between” effect
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbc.weighty = 1;          // pushes rows apart
            panel.add(Box.createVerticalGlue(), gbc);

            add(Box.createVerticalGlue()); //Vertical centering
            add(login);
            add(or_signup);
            add(panel);
            add(submit);
            add(Box.createVerticalGlue()); //Vertical centering
            setLocationRelativeTo(null);
            setVisible(true);
        }
        catch(Throwable e){
            System.out.println("Error");
        }
    }
}
