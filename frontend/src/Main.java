import components.MsgBubble;
import components.RoundedTextArea;
import ui.ChatScreen;
import ui.Login;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args){
//        ChatScreen cs = new ChatScreen();

//        new Login();
        JFrame fr = new JFrame();
        fr.setLayout(new FlowLayout());
        fr.setSize(new Dimension(500,500));
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MsgBubble msg = new MsgBubble("Hello! How are you?", true, 900);
        fr.add(msg);

//        RoundedTextArea rta = new RoundedTextArea(30, 20);
//        rta.setBackground(Color.GREEN);
//        rta.setPreferredSize(new Dimension(300,300));
//        fr.add(rta);

        fr.setVisible(true);
    }
}
