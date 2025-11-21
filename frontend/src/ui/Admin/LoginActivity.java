package ui.Admin;

import components.MyColor;
import components.RoundedButton;
import components.admin.*;
import util.Utility;

import javax.swing.*;
import java.awt.*;

public class LoginActivity extends JFrame {
    public LoginActivity(){
        Font roboto = new Font("Roboto", Font.PLAIN, 16);
        setLayout(new BoxLayout( getContentPane(), BoxLayout.X_AXIS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Chat system");
        getContentPane().setBackground(MyColor.LIGHT_BLACK);
        setSize(100,100);

        Sidebar sidebar = new Sidebar();
        AdminDashboard ad = new AdminDashboard();

        String[] customHeader = {"Username", "Full name", "Time"};
        String[][] data = {
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"}
        };
        CustomTable table = new CustomTable(data, customHeader);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        String[] options = {"Name", "Account Age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(new Font("Roboto", Font.PLAIN, 16));


        String[] optionsToSort = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(optionsToSort);
        asc_des.setForeground(MyColor.DARK_GRAY);
        asc_des.setFont(new Font("Roboto", Font.PLAIN, 16));

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(MyColor.LIGHT_BLUE);
        right.setPreferredSize(new Dimension(100, getHeight()));
        scroll.setPreferredSize(new Dimension(1920, 800));


        right.add(ad);
        right.add(scroll);
        right.add(Box.createVerticalGlue());

        add(sidebar);
        add(right);
    }
}
