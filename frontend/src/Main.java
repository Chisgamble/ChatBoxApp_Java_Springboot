import components.admin.AdminDashboard;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
                AdminDashboard cs = new AdminDashboard();
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
