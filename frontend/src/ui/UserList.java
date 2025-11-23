package ui;

import components.MyColor;
import components.RoundedButton;
import components.RoundedComboBox;
import components.admin.*;

import util.Utility;
import javax.swing.*;
import java.awt.*;

public class UserList extends JPanel {

    public UserList() {
        Font roboto = new Font("Roboto", Font.PLAIN, 16);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.LIGHT_BLUE);

        // === Filter Bar ===
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        filterPanel.setBackground(MyColor.WHITE_BG);
        filterPanel.setPreferredSize(new Dimension(1920, 60));
        filterPanel.setMaximumSize(new Dimension(1950, 70));

        FilterButton usernameFilter = new FilterButton("Filter by username");
        FilterButton nameFilter = new FilterButton("Filter by name");
        FilterButton statusFilter = new FilterButton("Filter by status");

        JLabel orderby = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);

        String[] options = {"Name", "Account Age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setFont(roboto.deriveFont(16f));

        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setFont(roboto.deriveFont(16f));

        filterPanel.add(usernameFilter);
        filterPanel.add(nameFilter);
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(orderby);
        filterPanel.add(comboBox);
        filterPanel.add(asc_des);

        add(filterPanel);

        // === Table ===

        String[] headers = {"Username","Full name","Status","Address","Date of birth","Gender","Email",""};
        String[][] data = {
                {"thaibao","Nguyễn Thái Bảo","Active","Ho Chi Minh City","2003-05-20","Male","bao@gmail.com",""},
                {"linhngoc","Phạm Ngọc Linh","Banned","Da Nang","2002-11-02","Female","linh@gmail.com",""},
                {"minhduc","Trần Minh Đức","Active","Hanoi","2001-08-12","Male","duc@gmail.com",""},
        };

        CustomTable table = new CustomTable(data, headers);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);

        // === Status + Action column customization ===
        for (int i = 0; i < data.length; i++) {

            // button column
            RoundedButton action = new RoundedButton(15);
            action.setText("...");
            action.setBorder(BorderFactory.createEmptyBorder(3, 10, 4, 10));
            action.setFont(roboto.deriveFont(Font.BOLD, 26f));
            action.setBackground(new Color(255,255,255,1));
            action.setFocusPainted(false);

            JPanel btnWrap = new JPanel(new GridBagLayout());
            btnWrap.add(action);
            btnWrap.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            table.setCellComponent(i, 7, btnWrap);

            // status color
            String status = data[i][2].toLowerCase();
            Color statusColor = switch (status) {
                case "active" -> Color.GREEN;
                case "banned" -> Color.RED;
                case "offline" -> Color.GRAY;
                default -> Color.BLACK;
            };

            // circle
            JPanel circle = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(statusColor);
                    g.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            circle.setPreferredSize(new Dimension(12, 12));
            circle.setOpaque(false);

            JPanel statusWrap = new JPanel(new GridBagLayout());
            statusWrap.setBackground(Color.WHITE);
            statusWrap.add(circle);
            statusWrap.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            table.setCellComponent(i, 2, statusWrap);
        }

        // Allow scroll + table to stretch
        add(Box.createVerticalGlue());
    }
}
