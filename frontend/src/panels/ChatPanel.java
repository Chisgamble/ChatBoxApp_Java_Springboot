package panels;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import components.SendMsg;

public class ChatPanel extends JPanel{
    Border border = BorderFactory.createLineBorder(Color.black);
    public ChatPanel(int width, int height){
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setBorder(border);
        this.setBackground(Color.RED);

        this.add(new SendMsg(width, 50), BorderLayout.SOUTH);
    }
}
