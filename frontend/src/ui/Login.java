package ui;

import util.Utility;
import components.MyColor;
import components.RoundedButton;
import components.RoundedPasswordField;
import components.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;


public class Login extends JFrame{
    public Login() throws IOException, FontFormatException {
        setTitle("Chat system");
        try{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            Font baloo = Utility.getFont("../public/font/Baloo2-VariableFont_wght.ttf");

            // Panel
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 40, 10, 40); // spacing around components
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.setMaximumSize(new Dimension(600, 175));

            //Sign up
            JLabel login = Utility.makeText("LOGIN", baloo, 75f, Font.BOLD, MyColor.LIGHT_BLUE, null);
            login.setAlignmentX(0.5f);
            login.setMaximumSize(new Dimension(login.getMaximumSize().width, 75));
            // or log in
            JLabel or_signup = Utility.makeText("or signup", baloo, 20f, Font.PLAIN, MyColor.LIGHT_BLUE, null);
            or_signup.setAlignmentX(0.5f);

            // Username
            JLabel usr_label = Utility.makeText("Username/email:", baloo, 20f, Font.BOLD, MyColor.LIGHT_BLACK, null);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0;
            panel.add(usr_label, gbc);

            RoundedTextField usr_name = new RoundedTextField(20);
            usr_name.setFont(baloo.deriveFont(Font.PLAIN, 18f));
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
            JLabel pass_label = Utility.makeText("Password:", baloo, 20f, Font.BOLD, MyColor.LIGHT_BLACK, null);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0;
            panel.add(pass_label, gbc);

            RoundedPasswordField password = new RoundedPasswordField(20);
            password.setFont(baloo.deriveFont(Font.PLAIN, 18f));
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
            forgot_password.setFont(baloo.deriveFont(Font.PLAIN, 16f));
            forgot_password.setForeground(new Color(0xa2a2a2));
            forgot_password.setAlignmentX(0.5f);
            forgot_password.setBackground(MyColor.LIGHT_BLACK);
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(forgot_password,gbc);

            // Row 3: Submit Button
            RoundedButton submit = new RoundedButton(15);
            submit.setText("SUBMIT");
            submit.setFont(baloo.deriveFont(Font.BOLD, 20f));
            submit.setBackground(MyColor.LIGHT_BLUE);
            submit.setForeground(Color.WHITE);
            submit.setFocusPainted(false);
            submit.setPreferredSize(new Dimension(150, 45));
            submit.setMaximumSize(new Dimension(150, 45));
            submit.setAlignmentX(0.5f);

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
        catch (IOException | FontFormatException e) {
            System.out.println("io font error");
            throw new RuntimeException(e);
        }
        catch(Throwable e){
            System.out.println("Error");
        }


    }
}
