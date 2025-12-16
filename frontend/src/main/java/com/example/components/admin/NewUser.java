package com.example.components.admin;

import com.example.components.admin.CustomTable;
import com.example.components.admin.MainPanel;
import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedComboBox;
import com.example.dto.NewUserDTO;
import com.example.services.admin.UserListService; // Or wherever UserService is
import com.example.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class NewUser extends MainPanel {
    private List<String> usernameFilter;
    private List<String> emailFilter;
    private LocalDate startDate;
    private LocalDate endDate;
    private String order; // "asc" or "desc"
    private final UserListService userService;
    private List<NewUserDTO> users;
    private JPanel filterPanel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public NewUser() {
        userService = new UserListService();

        // Defaults
        order = "desc"; // Most recent
        usernameFilter = new ArrayList<>();
        emailFilter = new ArrayList<>();
        startDate = null;
        endDate = null;

        // Layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        // Initial Load
        filterData();
        init();
    }

    @Override
    protected void buildFilterPanel() {
        // 1. Create panel only once
        if (filterPanel == null) {
            filterPanel = new JPanel();
            filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
            filterPanel.setBackground(MyColor.WHITE_BG);
            filterPanel.setPreferredSize(new Dimension(1920, 80));
            filterPanel.setMaximumSize(new Dimension(1950, 120));
            add(filterPanel, 0); // Add to top
        }

        // 2. Clear old components to rebuild
        filterPanel.removeAll();

        // === USERNAME FILTER ===
        FilterButton usernameBtn = new FilterButton("Filter by username");
        usernameBtn.addFilterAction(usernameFilter, this::filterData);
        filterPanel.add(usernameBtn);

        for (String item : usernameFilter) {
            addTag(item, usernameFilter);
        }

        // === EMAIL FILTER ===
        FilterButton emailBtn = new FilterButton("Filter by email");
        emailBtn.addFilterAction(emailFilter, this::filterData);
        filterPanel.add(emailBtn);

        for (String item : emailFilter) {
            addTag(item, emailFilter);
        }

        // === DATE FILTERS (Start & End) ===
        filterPanel.add(new JLabel(" | "));

        // Simple Input Dialog Buttons for Dates
        JButton startBtn = createDateButton("Start Date", startDate, true);
        JButton endBtn = createDateButton("End Date", endDate, false);

        filterPanel.add(startBtn);
        filterPanel.add(new JLabel("-"));
        filterPanel.add(endBtn);

        // === ORDER DROPDOWN ===
        filterPanel.add(Box.createHorizontalStrut(20));
        JLabel orderLabel = Utility.makeText("Order by Time:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(orderLabel);

        String[] sortOptions = {"Most recent", "Least recent"};
        RoundedComboBox<String> sortBox = new RoundedComboBox<>(sortOptions);
        sortBox.setFont(ROBOTO.deriveFont(16f));

        // Restore selection state
        if ("asc".equals(this.order)) sortBox.setSelectedItem("Least recent");
        else sortBox.setSelectedItem("Most recent");

        sortBox.addActionListener(e -> {
            String selected = (String) sortBox.getSelectedItem();
            if (selected != null) {
                this.order = selected.equals("Least recent") ? "asc" : "desc";
                filterData();
            }
        });
        filterPanel.add(sortBox);

        // Revalidate UI
        filterPanel.revalidate();
        filterPanel.repaint();
    }

    // Helper to add removeable tags (Green Bubbles)
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

    // Helper to create Date Buttons with Popup Input
    private JButton createDateButton(String label, LocalDate currentDate, boolean isStart) {
        String btnText = (currentDate == null) ? label : currentDate.format(dateFormatter);
        RoundedButton btn = new RoundedButton(10);
        btn.setText(btnText);
        btn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter date (yyyy-MM-dd):",
                    currentDate != null ? currentDate.toString() : "");

            if (input != null && !input.trim().isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(input.trim(), dateFormatter);
                    if (isStart) this.startDate = date;
                    else this.endDate = date;
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Format. Use yyyy-MM-dd");
                }
            } else {
                // Clear filter if input is cleared
                if (isStart) this.startDate = null;
                else this.endDate = null;
            }
            filterData(); // Refresh data
        });
        return btn;
    }

    @Override
    protected void setUpTable() {
        String[] headers = {"Username", "Email", "Time Created"};

        table = new CustomTable(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1850, 640));
        add(scroll);

        add(Box.createVerticalGlue());
    }

    protected void filterData() {
        // 1. Prepare Arguments
        String userQuery = usernameFilter.isEmpty() ? null : String.join("|", usernameFilter);
        String emailQuery = emailFilter.isEmpty() ? null : String.join("|", emailFilter);

        // 2. Call Service (You need to add this method to UserListService)
        users = userService.getNewUsers(userQuery, emailQuery, startDate, endDate, order);

        // 3. Map to Table Data
        if (users != null) {
            this.data = users.stream()
                    .map(u -> List.of(
                            Utility.safeString(u.username()),
                            Utility.safeString(u.email()),
                            u.createdAt() != null
                                    ? u.createdAt().replace("T", " ").split("\\.")[0]
                                    : ""
                    ))
                    .toList();
        } else {
            this.data = new ArrayList<>();
        }

        // 4. Refresh UI
        buildFilterPanel();
        refreshTable();
    }
}