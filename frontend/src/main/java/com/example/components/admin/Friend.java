package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedComboBox;
import com.example.components.RoundedLabel;
import com.example.dto.FriendListDataDTO;
import com.example.services.FriendService;
import com.example.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Friend extends MainPanel {

    // Service & Data
    private final FriendService friendService;
    private List<FriendListDataDTO> friends;

    // State / Filters
    private List<String> usernameFilter;
    private String comparatorSymbol; // "<", ">", "="
    private Integer friendCountValue;
    private String sortBy; // "username" or "accountAge"
    private String sortDir; // "asc" or "desc"

    // UI Components for State Persistence
    private RoundedComboBox<String> compBox;
    private RoundedComboBox<String> sortFieldBox;
    private RoundedComboBox<String> sortDirBox;
    private JPanel filterPanel;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            .withZone(ZoneId.systemDefault());

    public Friend() {
        this.friendService = new FriendService();

        // 1. Initialize Default State
        this.usernameFilter = new ArrayList<>();
        this.comparatorSymbol = ">"; // Default
        this.friendCountValue = null;
        this.sortBy = "username";
        this.sortDir = "asc";

        // 2. Setup Layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.LIGHT_BLUE);

        // 3. Initial Load
        filterData();
        init();
    }

    @Override
    protected void buildFilterPanel() {
        // Create panel if it doesn't exist
        if (filterPanel == null) {
            filterPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
            filterPanel.setBackground(MyColor.WHITE_BG);
            filterPanel.setPreferredSize(new Dimension(1920, 80)); // Slightly taller for tags
            filterPanel.setMaximumSize(new Dimension(1950, 100));
            add(filterPanel, 0);
        }

        // Clear to rebuild
        filterPanel.removeAll();

        Font roboto = new Font("Roboto", Font.PLAIN, 16);

        // === 1. FRIEND COUNT FILTERS ===
        RoundedLabel friendLbl = new RoundedLabel(10, 10);
        friendLbl.setText("Friends");
        filterPanel.add(friendLbl);

        // Comparator Dropdown (<, >, =)
        String[] comparatorOptions = {"<", ">", "="};
        compBox = new RoundedComboBox<>(comparatorOptions);
        compBox.setForeground(MyColor.DARK_GRAY);
        compBox.setFont(roboto);
        compBox.setSelectedItem(this.comparatorSymbol); // Restore state
        compBox.addActionListener(e -> {
            this.comparatorSymbol = (String) compBox.getSelectedItem();
            filterData();
        });
        filterPanel.add(compBox);

        // Number Input Button
        String btnText = (friendCountValue == null) ? "Enter number.." : String.valueOf(friendCountValue);
        FilterButton numberBtn = new FilterButton(btnText, 5);
        numberBtn.addActionListener(e -> {
            String input = numberBtn.getText();
            if (input != null && !input.trim().isEmpty()) {
                try {
                    this.friendCountValue = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                }
            } else {
                this.friendCountValue = null; // Clear filter on empty/cancel
            }
            filterData();
        });
        filterPanel.add(numberBtn);

        // === 2. USERNAME FILTER ===
        FilterButton nameFilterBtn = new FilterButton("Filter by name");
        nameFilterBtn.addFilterAction(usernameFilter, this::filterData);
        filterPanel.add(nameFilterBtn);

        // Render Tags (Green Bubbles) for Usernames
        for (String name : usernameFilter) {
            addTag(name, usernameFilter);
        }

        // Spacer
        filterPanel.add(Box.createHorizontalStrut(20));

        // === 3. SORTING ===
        JLabel orderLbl = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(orderLbl);

        // Sort Field (Name vs Age)
        String[] options = {"Name", "Account age"};
        sortFieldBox = new RoundedComboBox<>(options);
        sortFieldBox.setForeground(MyColor.DARK_GRAY);
        sortFieldBox.setFont(roboto);
        // Restore State & Map visual text to backend keys
        sortFieldBox.setSelectedItem("Account age".equals(this.sortBy) ? "Account age" : "Name");
        if (this.sortBy.equals("accountAge")) sortFieldBox.setSelectedItem("Account age");
        else sortFieldBox.setSelectedItem("Name");

        sortFieldBox.addActionListener(e -> {
            String selected = (String) sortFieldBox.getSelectedItem();
            this.sortBy = "Account age".equals(selected) ? "accountAge" : "username";
            filterData();
        });
        filterPanel.add(sortFieldBox);

        // Sort Direction (Asc/Desc)
        String[] sortOptions = {"Ascending", "Descending"};
        sortDirBox = new RoundedComboBox<>(sortOptions);
        sortDirBox.setForeground(MyColor.DARK_GRAY);
        sortDirBox.setFont(roboto);
        sortDirBox.setSelectedItem("desc".equals(this.sortDir) ? "Descending" : "Ascending");
        sortDirBox.addActionListener(e -> {
            String selected = (String) sortDirBox.getSelectedItem();
            this.sortDir = "Descending".equals(selected) ? "desc" : "asc";
            filterData();
        });
        filterPanel.add(sortDirBox);

        // Refresh UI
        filterPanel.revalidate();
        filterPanel.repaint();
    }

    private void addTag(String text, List<String> list) {
        RoundedButton b = new RoundedButton(10);
        b.setText(text);
        b.setFocusPainted(false);
        b.addActionListener(e -> {
            list.remove(text);
            filterData();
        });
        filterPanel.add(b);
    }

    @Override
    protected void setUpTable() {
        String[] headers = {"Username", "Friends", "Friends of friends", "Time created"};

        // Use 'data' from MainPanel or create local if MainPanel structure differs
        // Assuming MainPanel has 'protected List<List<Object>> data;' or similar
        // If not, we initialize the CustomTable directly with null safe checks.

        CustomTable table = new CustomTable(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);

        // Stretch content
        add(Box.createVerticalGlue());

        // Assign to parent field if needed for refresh logic
        this.table = table;
    }

    protected void filterData() {

        // 2. Call Service
        friends = friendService.getFriendListData(
                usernameFilter,
                sortBy,
                sortDir,
                comparatorSymbol,
                friendCountValue
        );

        // 3. Map DTO to Table Data
        if (friends != null) {
            this.data = friends.stream()
                    .map(f -> {
                        List<String> row = new ArrayList<>();
                        row.add(Utility.safeString(f.username()));
                        row.add(String.valueOf(f.friendCount()));
                        row.add(String.valueOf(f.friendOfFriendCount()));

                        // Since the service already formatted it as String (e.g., "YYYY-MM-DD HH:MM:SS"),
                        // we just use the string directly. If it needs reformatting for UI display,
                        // you can do it here, but typically you avoid re-parsing strings.
                        row.add(f.createdAt() != null ? f.createdAt() : "N/A");

                        return row;
                    })
                    .collect(Collectors.toList());
        } else {
            this.data = new ArrayList<>();
        }

        // 4. Refresh UI
        buildFilterPanel(); // Rebuilds top bar (keeps combo box states)
        refreshTable();     // MainPanel method to reload table model
    }
}