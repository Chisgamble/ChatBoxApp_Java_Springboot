package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedComboBox;
import com.example.components.RoundedButton;
import com.example.components.RoundedTextField; // Assuming this class exists
import com.example.dto.ActivityDTO;
import com.example.services.ActivityLogService;
import com.example.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Active extends MainPanel {

    private final ActivityLogService service;
    private List<ActivityDTO> activityData;
    private JPanel filterPanel;
    private JScrollPane tableScroll;

    // Filter State
    private List<String> nameFilter;
    private String activityType; // "open", "with-one", "with-group"
    private String comparison;   // "<", ">", "="
    private String count;        // The number to compare against
    private String sortBy;       // "username" or "age"
    private String sortDir;      // "asc" or "desc"

    public Active() {
        this.service = new ActivityLogService();

        // Defaults
        this.nameFilter = new ArrayList<>();
        this.activityType = "Open";
        this.comparison = "=";
        this.count = null;
        this.sortBy = "username";
        this.sortDir = "asc";

        // Layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        // Initial Load and UI Setup
        filterData();
    }

    @Override
    protected void buildFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel();
            filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
            filterPanel.setBackground(MyColor.WHITE_BG);
            filterPanel.setPreferredSize(new Dimension(1920, 60));
            filterPanel.setMaximumSize(new Dimension(1950, 70));
            add(filterPanel, 0);
        }
        filterPanel.removeAll();
        Font roboto = ROBOTO.deriveFont(16f);

        // --- ACTIVITY COUNT FILTER (Type, Comparator, Count) ---

        // 1. Activity Type Dropdown
        String[] activityOptions = {"---", "Open", "Chat with Person", "Chat with Group"};
        RoundedComboBox<String> actBox = new RoundedComboBox<>(activityOptions);
        actBox.setFont(roboto);

        // VISUAL FIX: Restore activity type selection
        String currentActivityLabel = switch (this.activityType) {
            case "open" -> "Open";
            case "with-one" -> "Chat with Person";
            case "with-group" -> "Chat with Group";
            default -> "---";
        };
        actBox.setSelectedItem(currentActivityLabel);
        actBox.addActionListener(e -> {
            String selected = (String) actBox.getSelectedItem();
            this.activityType = switch (selected) {
                case "Open" -> "open";
                case "Chat with Person" -> "with-one";
                case "Chat with Group" -> "with-group";
                default -> ""; // Sets to null if "---" is selected
            };
            filterData();
        });
        filterPanel.add(actBox);

        // 2. Comparator Dropdown
        String[] comparatorOptions = {"---", "=", ">", "<"}; // Added "---" for clearing filter
        RoundedComboBox<String> compBox = new RoundedComboBox<>(comparatorOptions);
        compBox.setFont(roboto);

        compBox.setSelectedItem(this.comparison == null ? "---" : this.comparison);

        compBox.addActionListener(e -> {
            String selected = (String) compBox.getSelectedItem();
            this.comparison = selected.equals("---") ? null : selected;
            filterData();
        });
        filterPanel.add(compBox);

        // 3. Count Input (RoundedTextField)
        RoundedTextField countField = new RoundedTextField(5);
        countField.setPreferredSize(new Dimension(100, 35));
        countField.setFont(roboto.deriveFont(14f));
        countField.setText(this.count == null ? "" : this.count);

        ActionListener countListener = e -> {
            String input = countField.getText().trim();

            if (input.isEmpty()) {
                this.count = null;
            } else {
                try {
                    Integer.parseInt(input);
                    this.count = input;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Input must be a valid number.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    // Revert text field to current filter state
                    countField.setText(this.count == null ? "" : this.count);
                    return;
                }
            }
            filterData();
        };

        countField.addActionListener(countListener);
        filterPanel.add(countField);

        filterPanel.add(new JLabel(" | "));

        // --- USERNAME FILTER ---
        FilterButton nameFilterBtn = new FilterButton("Filter by username");
        nameFilterBtn.addFilterAction(nameFilter, this::filterData);
        filterPanel.add(nameFilterBtn);

        // Display Name Filter Tags
        for(String item : nameFilter){
            if (item == null || item.isEmpty()) continue;
            RoundedButton b = new RoundedButton(10);
            b.setText(item);
            b.setForeground(Color.BLACK);
            b.setFocusPainted(false);
            b.addActionListener(e -> {
                nameFilter.remove(item);
                filterData();
            });
            filterPanel.add(b);
        }

        filterPanel.add(new JLabel(" | "));

        // --- SORT OPTIONS ---
        JLabel orderby = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(orderby);

        // Sort Field (Name vs Age)
        String[] options = {"Username", "Account Age"};
        RoundedComboBox<String> sortFieldBox = new RoundedComboBox<>(options);
        sortFieldBox.setFont(roboto);

        // VISUAL FIX: Restore sort field selection
        sortFieldBox.setSelectedItem("age".equals(this.sortBy) ? "Account Age" : "Username");

        sortFieldBox.addActionListener(e -> {
            String selected = (String) sortFieldBox.getSelectedItem();
            this.sortBy = "Account Age".equals(selected) ? "age" : "username";
            filterData();
        });
        filterPanel.add(sortFieldBox);

        // Sort Direction (Asc/Desc)
        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> sortDirBox = new RoundedComboBox<>(sortOptions);
        sortDirBox.setFont(roboto);

        // VISUAL FIX: Restore sort direction selection
        sortDirBox.setSelectedItem("desc".equals(this.sortDir) ? "Descending" : "Ascending");

        sortDirBox.addActionListener(e -> {
            String selected = (String) sortDirBox.getSelectedItem();
            this.sortDir = "Descending".equals(selected) ? "desc" : "asc";
            filterData();
        });
        filterPanel.add(sortDirBox);

        filterPanel.revalidate();
        filterPanel.repaint();
    }

    @Override
    protected void setUpTable() {
        // Headers match the DTO/Service output
        String[] headers = {"Username", "Open (Count)", "Chat with Person (Count)", "Chat with Group (Count)", "Account Created"};

        // 1. Convert List<List<String>> data to String[][] for CustomTable
        if (this.data == null) this.data = new ArrayList<>();
        String[][] tableData = Utility.convertToString(this.data);
        table = new CustomTable(tableData, headers);

        // 2. Wrap the table in the scroll pane
        this.tableScroll = new JScrollPane(table);
        this.tableScroll.setBorder(BorderFactory.createEmptyBorder());
        this.tableScroll.setPreferredSize(new Dimension(1920, 800));

        table.getTableHeader().setReorderingAllowed(false);

        add(Box.createVerticalGlue());
    }

    protected void filterData() {
        activityData = service.getAllUserActivity(
                this.nameFilter,
                this.activityType,
                this.comparison,
                this.count,
                this.sortBy,
                this.sortDir
        );

        // 3. Map DTO (ActivityDTO) to Table Data (List<List<String>>)
        if (activityData != null) {
            this.data = activityData.stream()
                    .map(dto -> {
                        List<String> row = new ArrayList<>();
                        row.add(Utility.safeString(dto.username()));
                        row.add(String.valueOf(dto.openCount()));
                        row.add(String.valueOf(dto.chatOneCount()));
                        row.add(String.valueOf(dto.chatGroupCount()));
                        row.add(Utility.safeString(dto.createdAt()));
                        return row;
                    })
                    .collect(Collectors.toList());
        } else {
            this.data = new ArrayList<>();
        }

        // 4. Refresh UI
        buildFilterPanel();
        refreshTable();
    }

    @Override
    protected void refreshTable() {
        if (this.tableScroll != null) {
            remove(this.tableScroll);
        }

        setUpTable();

        if (this.tableScroll != null) {
            add(this.tableScroll);
        }

        revalidate();
        repaint();
    }
}