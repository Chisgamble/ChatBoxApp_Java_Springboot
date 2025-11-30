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
                AdminDashboard cs = new AdminDashboard();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
        URL url = new URL("http://localhost:8080/api/auth/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = "email=" + email + "&password=" + password;
        conn.getOutputStream().write(params.getBytes());

        String setCookie = conn.getHeaderField("Set-Cookie");
// Save this for next requests

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        String response = reader.lines().collect(Collectors.joining("\n"));

        System.out.println("Response: " + response);
        System.out.println("Session Cookie: " + setCookie);
        conn.setRequestProperty("Cookie", setCookie);

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
