package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedComboBox;

import com.example.components.RoundedPanel;
import com.example.util.Utility;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
                Arrays.asList("thaibao", "12:00:00 12/12/2025", ""),
                Arrays.asList("real", "12:00:00 12/12/2025", ""),
                Arrays.asList("Bún riêu cua", "12:00:00 12/12/2025", ""),
                Arrays.asList("Group vật lý", "12:00:00 12/12/2025", ""),
                Arrays.asList("12A12", "12:00:00 12/12/2025", ""),
                Arrays.asList("Group Toán", "12:00:00 12/12/2025", ""),
                Arrays.asList("New group", "12:00:00 12/12/2025", ""),
                Arrays.asList("1234", "12:00:00 12/12/2025", "")
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
        String[] headers = {"Chat group name", "Time created", ""};

        table = new CustomTable(filtered, headers);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);

        // === Table buttons ===
//        for (int i = 0; i < data.size(); i++) {
//            // View Participants button
//            RoundedButton participants = new RoundedButton(15);
//            participants.setText("View Participants");
//            participants.setBackground(MyColor.LIGHT_BLUE);
//            participants.setForeground(Color.WHITE);
//            participants.setFocusPainted(false);
//            participants.setBorder(BorderFactory.createEmptyBorder(3, 10, 4, 10));
//            participants.setFont(ROBOTO.deriveFont(Font.PLAIN, 14f));
//
//            final int a = i;
//            participants.addActionListener(e -> openParticipantsDialog(data.get(a)));
//
//            JPanel participantsWrapper = new JPanel(new GridBagLayout());
//            participantsWrapper.setBackground(Color.WHITE);
//            participantsWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
//            participantsWrapper.add(participants);
//            table.setCellComponent(i, 2, participantsWrapper);
//        }

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

    private void openParticipantsDialog(List<String> groupData) {
        // Tạo dialog modal
        JDialog dialog = new JDialog((Frame) null, "Participants", true);
        dialog.setSize(450, 600);
        dialog.setLayout(new BorderLayout());

        // Custom panel bên trong popup
        JPanel customPanel = new JPanel();
        customPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.Y_AXIS));
        customPanel.setBackground(Color.WHITE);

        // Ví dụ thêm dữ liệu
        JPanel wrapper = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1;
        g.anchor = GridBagConstraints.WEST; // canh trái

        JLabel groupName = new JLabel(groupData.get(0));
        groupName.setFont(ROBOTO.deriveFont(Font.BOLD, 16));
        wrapper.add(groupName, g);

        g.gridx = 1;
        g.weightx = 0;
        g.anchor = GridBagConstraints.EAST; // canh phải

        JLabel timeCreated = new JLabel(groupData.get(1));
        timeCreated.setFont(ROBOTO.deriveFont(Font.BOLD, 16));
        wrapper.add(timeCreated, g);

        wrapper.setPreferredSize(new Dimension(360, 60));
        wrapper.setMaximumSize(new Dimension(365, 65));
        wrapper.setBackground(Color.WHITE);
        customPanel.add(wrapper);

        for (String member : groupData) {
            JLabel name = new JLabel("Giao Thai Bao");
            name.setFont(new Font("Roboto", Font.PLAIN, 14));
            name.setAlignmentY(0.5f);
            JLabel role = new JLabel("Role: user");
            role.setFont(new Font("Roboto", Font.PLAIN, 14));
            role.setAlignmentY(0.5f);


            RoundedPanel labelWrapper = new RoundedPanel();
            labelWrapper.setLayout(new BorderLayout());
            labelWrapper.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            labelWrapper.setPreferredSize(new Dimension(360, 32));
            labelWrapper.setMaximumSize(new Dimension(365, 35));
            labelWrapper.setBackground(new Color(0,0,0, 15));

            labelWrapper.add(name, BorderLayout.WEST);
            labelWrapper.add(role, BorderLayout.EAST);
            customPanel.add(labelWrapper);
            customPanel.add(Box.createVerticalStrut(5));
        }

        // Cho scroll nếu nhiều người
        JScrollPane scrollPane = new JScrollPane(customPanel);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Đặt dialog ở giữa màn hình
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);
    }

}
