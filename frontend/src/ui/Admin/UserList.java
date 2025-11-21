package ui.Admin;

import components.MyColor;
import components.RoundedButton;
import components.admin.*;

import util.Utility;
import javax.swing.*;
import java.awt.*;

public class UserList extends JFrame {
    public UserList(){
        Font roboto = new Font("Roboto", Font.PLAIN, 16);
        setLayout(new BoxLayout( getContentPane(), BoxLayout.X_AXIS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Chat system");
        getContentPane().setBackground(MyColor.LIGHT_BLACK);
        setSize(100,100);

        Sidebar sidebar = new Sidebar();
        AdminDashboard ad = new AdminDashboard();
        JPanel filterPanel = new JPanel();
        FilterButton usernameFilter = new FilterButton("Filter by username");
        FilterButton nameFilter = new FilterButton("Filter by name");
        FilterButton statusFilter = new FilterButton("Filter by status");
        JLabel orderby = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);

        String[] customHeader = {"Username", "Full name", "Status", "Address", "Date of birth", "Gender", "Email", ""};
        String[][] data = {
                {"thaibao", "Nguyễn Thái Bảo", "Active", "Ho Chi Minh City", "2003-05-20", "Male", "bao@gmail.com", ""},
                {"linhngoc", "Phạm Ngọc Linh", "Banned", "Da Nang", "2002-11-02", "Female", "linh@gmail.com", ""},
                {"minhduc", "Trần Minh Đức", "Active", "Hanoi", "2001-08-12", "Male", "duc@gmail.com", ""},
                {"thaibao", "Nguyễn Thái Bảo", "Active", "Ho Chi Minh City", "2003-05-20", "Male", "bao@gmail.com", ""},
                {"linhngoc", "Phạm Ngọc Linh", "Banned", "Da Nang", "2002-11-02", "Female", "linh@gmail.com", ""},
                {"minhduc", "Trần Minh Đức", "Active", "Hanoi", "2001-08-12", "Male", "duc@gmail.com", ""},
                {"thaibao", "Nguyễn Thái Bảo", "Active", "Ho Chi Minh City", "2003-05-20", "Male", "bao@gmail.com", ""},
                {"linhngoc", "Phạm Ngọc Linh", "Banned", "Da Nang", "2002-11-02", "Female", "linh@gmail.com", ""},
                {"minhduc", "Trần Minh Đức", "Active", "Hanoi", "2001-08-12", "Male", "duc@gmail.com", ""},
                {"thaibao", "Nguyễn Thái Bảo", "Active", "Ho Chi Minh City", "2003-05-20", "Male", "bao@gmail.com", ""},
                {"linhngoc", "Phạm Ngọc Linh", "Banned", "Da Nang", "2002-11-02", "Female", "linh@gmail.com", ""},
                {"minhduc", "Trần Minh Đức", "Active", "Hanoi", "2001-08-12", "Male", "duc@gmail.com", ""},
                {"thaibao", "Nguyễn Thái Bảo", "Active", "Ho Chi Minh City", "2003-05-20", "Male", "bao@gmail.com", ""},
                {"linhngoc", "Phạm Ngọc Linh", "Banned", "Da Nang", "2002-11-02", "Female", "linh@gmail.com", ""},
                {"minhduc", "Trần Minh Đức", "Active", "Hanoi", "2001-08-12", "Male", "duc@gmail.com", ""},
                {"thaibao", "Nguyễn Thái Bảo", "Active", "Ho Chi Minh City", "2003-05-20", "Male", "bao@gmail.com", ""},
                {"linhngoc", "Phạm Ngọc Linh", "Banned", "Da Nang", "2002-11-02", "Female", "linh@gmail.com", ""},
                {"minhduc", "Trần Minh Đức", "Active", "Hanoi", "2001-08-12", "Male", "duc@gmail.com", ""},
                {"thaibao", "Nguyễn Thái Bảo", "Active", "Ho Chi Minh City", "2003-05-20", "Male", "bao@gmail.com", ""},
                {"linhngoc", "Phạm Ngọc Linh", "Banned", "Da Nang", "2002-11-02", "Female", "linh@gmail.com", ""},
                {"minhduc", "Trần Minh Đức", "Active", "Hanoi", "2001-08-12", "Male", "duc@gmail.com", ""}
        };
        CustomTable table = new CustomTable(data, customHeader);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        for (int i = 0; i < data.length; i++){
            RoundedButton action = new RoundedButton(15);
            action.setText("...");
            action.setBackground(Color.WHITE);
            action.setPreferredSize(new Dimension(60, 30));
            action.setMaximumSize(new Dimension(80, 80));
            action.setFont(roboto.deriveFont(Font.BOLD, 26f));

            JPanel wrapper = new JPanel(new GridBagLayout()); // center button
            wrapper.setBackground(Color.WHITE);
            wrapper.add(action);
            table.setCellComponent(i, 7, wrapper);
        }

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

        filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        filterPanel.setBackground(MyColor.WHITE_BG);
        filterPanel.setPreferredSize(new Dimension(1920, 60));
        filterPanel.setMaximumSize(new Dimension(1950, 70));
        filterPanel.add(usernameFilter);
        filterPanel.add(nameFilter);
        filterPanel.add(statusFilter);
        filterPanel.add(orderby);
        filterPanel.add(comboBox);
        filterPanel.add(asc_des);

        right.add(ad);
        right.add(filterPanel);
        right.add(scroll);
        right.add(Box.createVerticalGlue());

        add(sidebar);
        add(right);
        setVisible(true);
    }
}
