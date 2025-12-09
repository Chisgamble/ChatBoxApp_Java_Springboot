package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedComboBox;
import com.example.util.Utility;

import javax.swing.*;
import java.awt.*;

public class NewUser extends JPanel {

    public NewUser() {
        Font roboto = new Font("Roboto", Font.PLAIN, 16);

        // This panel IS the main page content
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.LIGHT_BLUE);

        // === Filter Panel ===
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        filterPanel.setBackground(MyColor.WHITE_BG);
        filterPanel.setPreferredSize(new Dimension(1920, 60));
        filterPanel.setMaximumSize(new Dimension(1950, 70));

        FilterButton startTime = new FilterButton("Start time", 10);
        FilterButton endTime = new FilterButton("End time", 10);
        FilterButton nameFilter = new FilterButton("Filter by name");
        JLabel orderby = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);

        String[] options = {"Name", "Group Age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(roboto);

        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setForeground(MyColor.DARK_GRAY);
        asc_des.setFont(roboto);

        filterPanel.add(startTime);
        filterPanel.add(new JLabel("-"));
        filterPanel.add(endTime);
        filterPanel.add(nameFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(orderby);
        filterPanel.add(comboBox);
        filterPanel.add(asc_des);

        add(filterPanel);

        // === Table ===
        String[] headers = {"Chat group name", "Time created"};
        String[][] data = {
                {"thaibao", "12:00:00 12/12/2025"},
                {"thaibao", "12:00:00 12/12/2025"},
                {"thaibao", "12:00:00 12/12/2025"},
                {"thaibao", "12:00:00 12/12/2025"},
                {"thaibao", "12:00:00 12/12/2025"},
                {"thaibao", "12:00:00 12/12/2025"},
                {"thaibao", "12:00:00 12/12/2025"},
                {"thaibao", "12:00:00 12/12/2025"}
        };

        CustomTable table = new CustomTable(data, headers);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);

        // Stretch content to fill
        add(Box.createVerticalGlue());
    }
}
