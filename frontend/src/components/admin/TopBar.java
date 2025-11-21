package components.admin;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

import components.MyColor;
import util.Utility;

import java.awt.*;

public class TopBar extends JPanel {
    public TopBar(){
        Font font = new Font( "Roboto", Font.PLAIN, 16);
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        JLabel Categories = new JLabel("Categories");
        Categories.setFont(new Font("Roboto", Font.BOLD, 18));
        Categories.setForeground(Color.WHITE);
        Categories.setAlignmentY(0f);
        JPanel catPanel = new JPanel();
        catPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        catPanel.setBackground(new Color(25, 55, 109));
        catPanel.setPreferredSize(new Dimension(180, 60));
        catPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        catPanel.add(Categories);

        JLabel text = Utility.makeText("Admin Dashboard", font, 26f, Font.BOLD, MyColor.LIGHT_BLACK, null);
        JPanel tPanel = new JPanel();
        tPanel.add(text);
        tPanel.setBackground(MyColor.WHITE_BG);
        tPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        setMinimumSize(new Dimension(2000, 60));
        setPreferredSize(new Dimension(2020, 60));
        setMaximumSize(new Dimension(2100, 100));
        setBackground(MyColor.WHITE_BG);
        add(catPanel);
        add(tPanel);
        setBorder(BorderFactory.createLineBorder(MyColor.LIGHT_BLACK));
        setAlignmentY(TOP_ALIGNMENT);
    }
}
