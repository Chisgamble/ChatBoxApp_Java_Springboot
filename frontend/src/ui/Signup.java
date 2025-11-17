package ui;

import util.Utility;
import components.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;


public class Signup {
    public static void main(String[] arg) throws IOException, FontFormatException {

        JFrame frame = new JFrame();
        frame.setTitle("Chat system");
        try{
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

            Font baloo = Utility.getFont("../public/font/Baloo2-VariableFont_wght.ttf");

            // Panel
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 40, 10, 40); // spacing around components
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.setMaximumSize(new Dimension(600, 250));

            //Sign up
            JLabel sign_up = Utility.makeText("SIGN UP", baloo, 75f, Font.BOLD, MyColor.LIGHT_BLUE, null);
            sign_up.setAlignmentX(0.5f);

            // or log in
            JLabel or_login = Utility.makeText("or login", baloo, 20f, Font.PLAIN, MyColor.LIGHT_BLUE, null);
            or_login.setAlignmentX(0.5f);

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

            password.setAlignmentX(0.5f);
            password.setToolTipText("Password");
            password.setForeground(Color.GRAY);

            gbc.gridx = 1;
            gbc.weightx = 1;
            panel.add(password, gbc);

            // Re-enter password
            JLabel reenter_label = Utility.makeText("Re-enter password:", baloo, 20f, Font.BOLD, MyColor.LIGHT_BLACK, null);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 0;
            panel.add(reenter_label, gbc);

            RoundedPasswordField reenter = new RoundedPasswordField(20);
            reenter.setFont(baloo.deriveFont(Font.PLAIN, 18f));
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


            frame.add(Box.createVerticalGlue()); //Vertical centering
            frame.add(sign_up);
            frame.add(or_login);
            frame.add(panel);
            frame.add(submit);
            frame.add(Box.createVerticalGlue()); //Vertical centering
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
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
