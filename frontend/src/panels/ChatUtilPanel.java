package panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class ChatUtilPanel extends JPanel{
    Border border = BorderFactory.createLineBorder(Color.black);
    public ChatUtilPanel(int width, int height){
        this.setPreferredSize(new Dimension(width, height));
        this.setBorder(border);
        this.setBackground(Color.BLUE);
    }
}
