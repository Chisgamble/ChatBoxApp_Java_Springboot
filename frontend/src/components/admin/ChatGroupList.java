package components.admin;

import components.MyColor;
import components.RoundedButton;
import components.RoundedComboBox;

import util.Utility;
import javax.swing.*;
import java.awt.*;

public class ChatGroupList extends JPanel {

    public ChatGroupList() {
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

        FilterButton nameFilter = new FilterButton("Filter by name");
        FilterButton timeFilter = new FilterButton("Filter by time");
        JLabel orderby = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);

        String[] options = {"Name", "Group Age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(roboto);

        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setForeground(MyColor.DARK_GRAY);
        asc_des.setFont(roboto);

        filterPanel.add(nameFilter);
        filterPanel.add(timeFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(orderby);
        filterPanel.add(comboBox);
        filterPanel.add(asc_des);

        add(filterPanel);

        // === Table ===
        String[] headers = {"Chat group name", "Time created", "", ""};
        String[][] data = {
                {"thaibao", "12:00:00 12/12/2025", "", ""},
                {"thaibao", "12:00:00 12/12/2025", "", ""},
                {"thaibao", "12:00:00 12/12/2025", "", ""},
                {"thaibao", "12:00:00 12/12/2025", "", ""},
                {"thaibao", "12:00:00 12/12/2025", "", ""},
                {"thaibao", "12:00:00 12/12/2025", "", ""},
                {"thaibao", "12:00:00 12/12/2025", "", ""},
                {"thaibao", "12:00:00 12/12/2025", "", ""}
        };

        CustomTable table = new CustomTable(data, headers);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);

        // === Table buttons ===
        for (int i = 0; i < data.length; i++) {
            // View Participants button
            RoundedButton participants = new RoundedButton(15);
            participants.setText("View Participants");
            participants.setBackground(MyColor.LIGHT_BLUE);
            participants.setForeground(Color.WHITE);
            participants.setFocusPainted(false);
            participants.setBorder(BorderFactory.createEmptyBorder(3, 10, 4, 10));
            participants.setFont(roboto.deriveFont(Font.PLAIN, 14f));

            JPanel participantsWrapper = new JPanel(new GridBagLayout());
            participantsWrapper.setBackground(Color.WHITE);
            participantsWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            participantsWrapper.add(participants);
            table.setCellComponent(i, 2, participantsWrapper);

            // View Admin button
            RoundedButton admin = new RoundedButton(15);
            admin.setText("View Admin");
            admin.setBackground(MyColor.DARK_RED);
            admin.setForeground(Color.WHITE);
            admin.setFocusPainted(false);
            admin.setBorder(BorderFactory.createEmptyBorder(3, 10, 4, 10));
            admin.setFont(roboto.deriveFont(Font.PLAIN, 14f));

            JPanel adminWrapper = new JPanel(new GridBagLayout());
            adminWrapper.setBackground(Color.WHITE);
            adminWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            adminWrapper.add(admin);
            table.setCellComponent(i, 3, adminWrapper);
        }

        // Stretch content to fill
        add(Box.createVerticalGlue());
    }
}
