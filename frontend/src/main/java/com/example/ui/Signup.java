package com.example.ui;

import com.example.dto.response.RegisterResDTO;
import com.example.services.AuthService;
import com.example.util.Utility;
import com.example.components.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class Signup extends JFrame{
    public Signup() throws IOException, FontFormatException {
        setTitle("Chat system");
        try{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            Font font = new Font("SansSerif", Font.BOLD, 13);

            // Panel
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 40, 10, 40); // spacing around components
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.setMaximumSize(new Dimension(600, 250));

            //Sign up
            JLabel sign_up = Utility.makeText("SIGN UP", font, 75f, Font.BOLD, MyColor.LIGHT_BLUE, null);
            sign_up.setAlignmentX(0.5f);

            // or log in
            JLabel or_login = Utility.makeText("or login", font, 20f, Font.PLAIN, MyColor.LIGHT_BLUE, null);
            or_login.setAlignmentX(0.5f);
            or_login.setCursor(new Cursor(Cursor.HAND_CURSOR));
            or_login.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Signup.this.dispose();
                        new Login();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(Signup.this, "Error: " + ex.getMessage());
                    }
                }
            });

            // Email
            JLabel usr_label = Utility.makeText("Email:", font, 20f, Font.BOLD, MyColor.LIGHT_BLACK, null);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0;
            panel.add(usr_label, gbc);

            RoundedTextField usr_name = new RoundedTextField(20);
            usr_name.setFont(font.deriveFont(Font.PLAIN, 18f));
            usr_name.setMaximumSize(new Dimension(325, 45));
            usr_name.setPreferredSize(new Dimension(325, 45));
            usr_name.setBackground(Color.WHITE);

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

            password.setAlignmentX(0.5f);
            password.setToolTipText("Password");
            password.setForeground(Color.GRAY);

            gbc.gridx = 1;
            gbc.weightx = 1;
            panel.add(password, gbc);

            // Re-enter password
            JLabel reenter_label = Utility.makeText("Re-enter password:", font, 20f, Font.BOLD, MyColor.LIGHT_BLACK, null);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 0;
            panel.add(reenter_label, gbc);

            RoundedPasswordField reenter = new RoundedPasswordField(20);
            reenter.setFont(font.deriveFont(Font.PLAIN, 18f));
            reenter.setMaximumSize(new Dimension(325, 45));
            reenter.setPreferredSize(new Dimension(325, 45));
            reenter.setBackground(Color.WHITE);

            reenter.setAlignmentX(0.5f);
            reenter.setToolTipText("Re enter");
            reenter.setForeground(Color.GRAY);

            gbc.gridx = 1;
            gbc.weightx = 1;
            panel.add(reenter, gbc);


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
                    RegisterResDTO user = authService.register("Name1", email, pwd);
                    JOptionPane.showMessageDialog(this, "Registered successfully!");
                    this.dispose(); // Close Signup window
                    new Login();    // Open Login window
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
            add(sign_up);
            add(or_login);
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
