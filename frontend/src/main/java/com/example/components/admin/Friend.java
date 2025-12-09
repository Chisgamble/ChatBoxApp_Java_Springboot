package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedComboBox;
import com.example.components.RoundedLabel;
import com.example.util.Utility;

import javax.swing.*;
import java.awt.*;

public class Friend extends JPanel {

    public Friend() {
        Font roboto = new Font("Roboto", Font.PLAIN, 16);

        // Main page panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.LIGHT_BLUE);

        // === Filter Panel ===
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        filterPanel.setBackground(MyColor.WHITE_BG);
        filterPanel.setPreferredSize(new Dimension(1920, 60));
        filterPanel.setMaximumSize(new Dimension(1950, 70));

        // ComboBoxes & filters
        String[] comparatorOptions = {"<", ">", "="};
        RoundedComboBox<String> compBox = new RoundedComboBox<>(comparatorOptions);
        compBox.setForeground(MyColor.DARK_GRAY);
        compBox.setFont(roboto);

        RoundedLabel friend = new RoundedLabel(10, 10);
        friend.setText("Friends");

        FilterButton number = new FilterButton("Enter number..", 5);
        FilterButton nameFilter = new FilterButton("Filter by name");
        JLabel orderby = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);

        String[] options = {"Name", "Account age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(roboto);

        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setForeground(MyColor.DARK_GRAY);
        asc_des.setFont(roboto);

        // Add filter controls
        filterPanel.add(friend);
        filterPanel.add(compBox);
        filterPanel.add(number);
        filterPanel.add(nameFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(orderby);
        filterPanel.add(comboBox);
        filterPanel.add(asc_des);

        add(filterPanel);

        // === Table ===
        String[] headers = {"Username", "Friend", "Friends of friends", "Time created"};
        String[][] data = {
                {"thaibao", "100", "2002", "12", "20/12/2005"},
                {"thaibao", "100", "2002", "12", "20/12/2005"},
                {"thaibao", "100", "2002", "12", "20/12/2005"},
                {"thaibao", "100", "2002", "12", "20/12/2005"},
                {"thaibao", "100", "2002", "12", "20/12/2005"},
                {"thaibao", "100", "2002", "12", "20/12/2005"}
        };

        CustomTable table = new CustomTable(data, headers);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);

        // Stretch content
        add(Box.createVerticalGlue());
    }
}
