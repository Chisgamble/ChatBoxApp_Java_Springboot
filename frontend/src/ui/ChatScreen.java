package ui;
import javax.swing.*;
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
        this.getContentPane().setBackground(Color.WHITE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width / 9;
        int height = screen.height;

        this.add(new ChatPanel(width * 5, height), BorderLayout.CENTER);
        this.add(new ChatUtilPanel(width * 2, height, false, false), BorderLayout.EAST);
        this.add(new UserUtilPanel(width * 2, height), BorderLayout.WEST);

        this.setVisible(true);
    }

    public void showProfilePopup() {
        // Create dim background using glass pane
        JPanel dim = new JPanel();
        dim.setBackground(new Color(0, 0, 0, 120));
        setGlassPane(dim);
        dim.setVisible(true);

        // Use your existing ProfilePopup class
        ProfilePopup popup = new ProfilePopup(this);

        // When popup closes → remove dim effect
        popup.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                dim.setVisible(false);
            }
        });

        popup.setVisible(true);
    }

}
