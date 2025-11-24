package components.admin;

import components.MyColor;
import components.RoundedButton;
import components.RoundedComboBox;

import util.Utility;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ChatGroupList extends MainPanel {
    private List<String> nameFilter;

    public ChatGroupList() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        // === Table ===
        data = Arrays.asList(
                Arrays.asList("thaibao", "12:00:00 12/12/2025", "", ""),
                Arrays.asList("thaibao", "12:00:00 12/12/2025", "", ""),
                Arrays.asList("thaibao", "12:00:00 12/12/2025", "", ""),
                Arrays.asList("thaibao", "12:00:00 12/12/2025", "", ""),
                Arrays.asList("thaibao", "12:00:00 12/12/2025", "", ""),
                Arrays.asList("thaibao", "12:00:00 12/12/2025", "", ""),
                Arrays.asList("thaibao", "12:00:00 12/12/2025", "", ""),
                Arrays.asList("thaibao", "12:00:00 12/12/2025", "", "")
        );
        filtered = new ArrayList<>(data);
        nameFilter = new ArrayList<>();

        refreshTable();
    }

    @Override
    protected void buildFilterPanel() {
        // === Filter Panel ===
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        filterPanel.setBackground(MyColor.WHITE_BG);
        filterPanel.setPreferredSize(new Dimension(1920, 60));
        filterPanel.setMaximumSize(new Dimension(1950, 70));

        // Name Filter
        FilterButton nameFilterBtn = new FilterButton("Filter by name");
        filterPanel.add(nameFilterBtn);
        nameFilterBtn.addFilterAction(nameFilter, this::setUpFiltered, this::refreshTable);
        // Name Filter Tags
        for(String item : nameFilter){
            RoundedButton b = new RoundedButton(10);
            b.setText(item);
            b.setFocusPainted(false);
            b.addActionListener(e -> {
                nameFilter.remove(item);
                setUpFiltered();
                refreshTable();
            });

            filterPanel.add(b);
        }

        FilterButton timeFilter = new FilterButton("Filter by time");
        JLabel orderby = Utility.makeText("Order by:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);

        String[] options = {"Name", "Group Age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(ROBOTO);

        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setForeground(MyColor.DARK_GRAY);
        asc_des.setFont(ROBOTO);

        filterPanel.add(timeFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(orderby);
        filterPanel.add(comboBox);
        filterPanel.add(asc_des);

        add(filterPanel);
    }

    @Override
    protected void setUpTable() {
    String[] headers = {"Chat group name", "Time created", "", ""};

        table = new CustomTable(filtered, headers);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);

        // === Table buttons ===
        for (int i = 0; i < data.size(); i++) {
            // View Participants button
            RoundedButton participants = new RoundedButton(15);
            participants.setText("View Participants");
            participants.setBackground(MyColor.LIGHT_BLUE);
            participants.setForeground(Color.WHITE);
            participants.setFocusPainted(false);
            participants.setBorder(BorderFactory.createEmptyBorder(3, 10, 4, 10));
            participants.setFont(ROBOTO.deriveFont(Font.PLAIN, 14f));

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
            admin.setFont(ROBOTO.deriveFont(Font.PLAIN, 14f));

            JPanel adminWrapper = new JPanel(new GridBagLayout());
            adminWrapper.setBackground(Color.WHITE);
            adminWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            adminWrapper.add(admin);
            table.setCellComponent(i, 3, adminWrapper);
        }

        // Stretch content to fill
        add(Box.createVerticalGlue());
    }

    private void setUpFiltered(){
        filtered = new ArrayList<>();
        boolean fit = false;
        for (List<String> row : data) {
            fit = false;
            for (String filter : nameFilter){
                if (row.get(0).toLowerCase().contains(filter.toLowerCase())) {
                    fit = true;
                    break;
                }
            }
            if(fit || nameFilter.isEmpty()){
                filtered.add(row);
            }
        }
    }
}
