package components.admin;

import components.MyColor;
import components.RoundedButton;
import components.RoundedComboBox;
import util.Utility;

import javax.swing.*;
import java.awt.*;

public class Spam extends JPanel {

    public Spam() {
        Font roboto = new Font("Roboto", Font.PLAIN, 16);

        // Main page panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.LIGHT_BLUE);

        // === Filter Panel ===
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        filterPanel.setBackground(MyColor.WHITE_BG);
        filterPanel.setPreferredSize(new Dimension(1920, 60));
        filterPanel.setMaximumSize(new Dimension(1950, 70));

        FilterButton nameFilter = new FilterButton("Filter by name");
        FilterButton timeFilter = new FilterButton("Filter by time");
        FilterButton emailFilter = new FilterButton("Filter by email");
        JLabel orderby = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);

        String[] options = {"Name", "Time"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(roboto);

        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setForeground(MyColor.DARK_GRAY);
        asc_des.setFont(roboto);

        filterPanel.add(nameFilter);
        filterPanel.add(timeFilter);
        filterPanel.add(emailFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(orderby);
        filterPanel.add(comboBox);
        filterPanel.add(asc_des);

        add(filterPanel);

        // === Table ===
        String[] headers = {"Username", "Time reported", "email",""};
        String[][] data = {
                {"thaibao", "12:00:00 12/12/2025", "123@gmail.com", ""},
                {"thaibao", "12:00:00 12/12/2025", "123@gmail.com", ""},
                {"thaibao", "12:00:00 12/12/2025", "123@gmail.com", ""},
                {"thaibao", "12:00:00 12/12/2025", "123@gmail.com", ""},
                {"thaibao", "12:00:00 12/12/2025", "123@gmail.com", ""},
                {"thaibao", "12:00:00 12/12/2025", "123@gmail.com", ""},
                {"thaibao", "12:00:00 12/12/2025", "123@gmail.com", ""},
                {"thaibao", "12:00:00 12/12/2025", "123@gmail.com", ""}
        };

        CustomTable table = new CustomTable(data, headers);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);

        // === Table buttons ===
        for (int i = 0; i < data.length; i++) {
            RoundedButton lock = new RoundedButton(15);
            lock.setText("Lock account");
            lock.setBackground(MyColor.DARK_RED);
            lock.setForeground(Color.WHITE);
            lock.setFocusPainted(false);
            lock.setBorder(BorderFactory.createEmptyBorder(3, 10, 4, 10));
            lock.setFont(roboto.deriveFont(Font.PLAIN, 14f));

            JPanel lockWrapper = new JPanel(new GridBagLayout());
            lockWrapper.setBackground(Color.WHITE);
            lockWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            lockWrapper.add(lock);

            table.setCellComponent(i, 3, lockWrapper);
        }

        // Stretch content
        add(Box.createVerticalGlue());
    }
}
