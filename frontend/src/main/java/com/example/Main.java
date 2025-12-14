package com.example;

import com.example.dto.UserMiniDTO;
import com.example.ui.*;

import javax.swing.*;


public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            try {
                UserMiniDTO user = new UserMiniDTO(12, "giao@gmail.com", "userme", "admin");
                AdminDashboard cs = new AdminDashboard(user);
//                new Signup();
//                AuthService authService = new AuthService();
//                LoginResDTO user = authService.login("ryoshu@gmail.com", "OLPASMTHNWTRTBTLIU");
//                new ChatScreen(user);
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
