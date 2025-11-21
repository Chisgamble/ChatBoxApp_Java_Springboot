package components.admin;

import javax.swing.*;

import components.MyColor;
import util.Utility;

import java.awt.*;

public class AdminDashboard extends JPanel {
    public AdminDashboard(){
        Font font = new Font( "Roboto", Font.PLAIN, 16);
        setLayout(new FlowLayout(FlowLayout.LEADING, 20, 12));
        JLabel text = Utility.makeText("Admin Dashboard", font, 26f, Font.BOLD, MyColor.LIGHT_BLACK, null);
        setPreferredSize(new Dimension(1920, 67));
        setMaximumSize(new Dimension(2000, 100));
        setBackground(MyColor.WHITE_BG);
        add(text);
        setBorder(BorderFactory.createLineBorder(MyColor.LIGHT_BLACK));
        setAlignmentY(TOP_ALIGNMENT);
    }
}
