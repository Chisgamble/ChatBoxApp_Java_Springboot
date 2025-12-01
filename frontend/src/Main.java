import dto.response.LoginResDTO;
import services.AuthService;
import ui.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            try {
//                AdminDashboard cs = new AdminDashboard();
//                new Signup();
                AuthService authService = new AuthService();
                LoginResDTO user = authService.login("ryoshu@gmail.com", "OLPASMTHNWTRTBTLIU");
                new ChatScreen(user);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });

//        new Login();
//        JFrame fr = new JFrame();
//        fr.setLayout(new FlowLayout());
//        fr.setSize(new Dimension(500,500));
//        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        MsgBubble msg = new MsgBubble("Hello! How are you?", true, 900);
//        fr.add(msg);

//        RoundedTextArea rta = new RoundedTextArea(30, 20);
//        rta.setBackground(Color.GREEN);
//        rta.setPreferredSize(new Dimension(300,300));
//        fr.add(rta);

//        fr.setVisible(true);
    }
}
