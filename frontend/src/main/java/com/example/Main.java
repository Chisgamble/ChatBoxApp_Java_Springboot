package com.example;

import com.example.dto.UserMiniDTO;
import com.example.ui.*;

import javax.swing.*;


public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            try {
//                UserMiniDTO user = new UserMiniDTO(100, "as@gmail.com", "bao", "ADMIN");
//                AdminDashboard cs = new AdminDashboard(user);
                new Login();
                // UserMiniDTO user = new UserMiniDTO(3, "ryoshu@gmail.com", "Ryoshu", "user", "R");
//                AuthService authService = new AuthService();
//                LoginResDTO user = authService.login("ryoshu@gmail.com", "OLPASMTHNWTRTBTLIU");
//                new ChatScreen(user);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });


    }
}
