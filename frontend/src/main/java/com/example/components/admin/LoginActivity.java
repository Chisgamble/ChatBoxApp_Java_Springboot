package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedComboBox;
import com.example.dto.LoginLogDTO;
import com.example.dto.UserDTO;
import com.example.services.admin.LoginLogService;
import com.example.services.admin.UserListService;
import com.example.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends MainPanel {
    private List<String> usernameFilter;
    private List<String> emailFilter;
    private String order;
    private LoginLogService loginLogService;
    private JPanel filterPanel;
    List<LoginLogDTO> logins;
    private String status;


    public LoginActivity() {
        loginLogService = new LoginLogService();
        order = "asc";
        usernameFilter = new ArrayList<>();
        emailFilter = new ArrayList<>();
        Font roboto = new Font("Roboto", Font.PLAIN, 16);

        // This panel IS the main page content
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.LIGHT_BLUE);

        filterData(); // This populates 'this.data'
        init();

    }

    @Override
    protected void buildFilterPanel() {
        // 1. Create panel only once, otherwise just clear it
        if (filterPanel == null) {
            filterPanel = new JPanel();
            filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
            filterPanel.setBackground(MyColor.WHITE_BG);
            filterPanel.setPreferredSize(new Dimension(1920, 80)); // Adjusted height
            filterPanel.setMaximumSize(new Dimension(1950, 120));
            add(filterPanel, 0); // Add at index 0 (top)
        }

        // 2. Clear old components (buttons, dropdowns)
        filterPanel.removeAll();

        // === USERNAME FILTER ===
        FilterButton usernameFilterBtn = new FilterButton("Filter by username");
        // Pass filterData (which now calls buildFilterPanel) to refresh UI
        usernameFilterBtn.addFilterAction(usernameFilter, this::filterData);
        filterPanel.add(usernameFilterBtn);

        // DYNAMIC TAGS (Username)
        for(String item : usernameFilter){
            RoundedButton b = new RoundedButton(10);
            b.setText(item);
            b.setFocusPainted(false);
            setUpRemoveFilter(b, usernameFilter, item); // Logic to remove
            filterPanel.add(b);
        }

        // === NAME FILTER ===
        FilterButton emailFilterBtn = new FilterButton("Filter by email");
        emailFilterBtn.addFilterAction(emailFilter, this::filterData);
        filterPanel.add(emailFilterBtn);

        // DYNAMIC TAGS (Name)
        for(String item : emailFilter){
            RoundedButton b = new RoundedButton(10);
            b.setText(item);
            b.setFocusPainted(false);
            setUpRemoveFilter(b, emailFilter, item);
            filterPanel.add(b);
        }

        // === STATUS DROPDOWN ===
        JLabel statusFilter = Utility.makeText("Filter by status:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(statusFilter);

        String[] statusOptions = {"All", "Success", "Fail"};
        RoundedComboBox<String> statusBox = new RoundedComboBox<>(statusOptions);
        statusBox.setFont(ROBOTO.deriveFont(16f));

        // MEMORY FIX: Set selected item based on current variable
        if(this.status != null) {
            String display = this.status.substring(0, 1).toUpperCase() + this.status.substring(1).toLowerCase();
            statusBox.setSelectedItem(display);
        }

        statusBox.addActionListener(e -> {
            String selected = (String) statusBox.getSelectedItem();
            if (selected != null) {
                this.status = selected.toLowerCase();
                filterData(); // Reloads data AND rebuilds panel
            }
        });
        filterPanel.add(statusBox);

        // === ORDER DROPDOWN ===
        String[] sortOptions = {"Most recent", "Least recent"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setFont(ROBOTO.deriveFont(16f));

        // MEMORY FIX
        if("asc".equals(this.order)) asc_des.setSelectedItem("Ascending");
        else asc_des.setSelectedItem("Descending");

        asc_des.addActionListener(e -> {
            String selected = (String) asc_des.getSelectedItem();
            if (selected != null){
                this.order = selected.equals("Most recent") ? "asc" : "desc";
                filterData();
            }
        });
        JLabel orderby = Utility.makeText("Order by time:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(orderby);
        filterPanel.add(asc_des);

        // Finalize Layout Update
        filterPanel.revalidate();
        filterPanel.repaint();
    }

    @Override
    protected void setUpTable() {
        String[] headers = {"Username", "Full name", "Email", "Time", "Status"};
        table = new CustomTable(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing for ALL cells (Action is via Right-Click)
            }
        };

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1850, 600));
        add(scroll);

        add(Box.createVerticalGlue());
    }

    private void setUpRemoveFilter(RoundedButton b, List<String> list, String item){
        b.addActionListener(e -> {
            list.remove(item);
            filterData();
            refreshTable();
        });
    }

    protected void filterData() {
        // 1. Fetch Data from Service
        logins = loginLogService.getAll(
                this.emailFilter,
                this.usernameFilter,
                this.status,
                this.order
        );

        // 2. Convert List<LoginLogDTO> to Table Data
        if (logins != null) {
            this.data = logins.stream()
                    .map(log -> List.of(
                            Utility.safeString(log.username()),
                            Utility.safeString(log.name()),
                            Utility.safeString(log.email()),

                            // Clean up Date string (e.g., remove 'T')
                            Utility.safeString(log.createdAt() != null ? log.createdAt().replace("T", " ") : ""),

                            log.isSuccess() ? "Success" : "Failed"
                    ))
                    .toList();
        } else {
            this.data = new ArrayList<>();
        }

        buildFilterPanel();
        refreshTable();
    }
}
