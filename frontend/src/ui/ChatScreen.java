package ui;
import javax.swing.JFrame;
import java.awt.*;

import panels.ChatPanel;
import panels.ChatUtilPanel;
import panels.UserUtilPanel;

public class ChatScreen extends JFrame {

    public ChatScreen(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width / 9;
        int height = screen.height;

        this.add(new ChatPanel(width * 5, height), BorderLayout.CENTER);
        this.add(new ChatUtilPanel(width * 2, height), BorderLayout.EAST);
        this.add(new UserUtilPanel(width * 2, height), BorderLayout.WEST);

        this.setVisible(true);
    }

}
