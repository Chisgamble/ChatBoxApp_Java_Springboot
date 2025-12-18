package com.example.components.admin;

import com.example.components.ConfirmPopup; // Import the custom popup
import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedComboBox;
import com.example.services.admin.SpamReportService;
import com.example.services.admin.UserListService;
import com.example.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Spam extends MainPanel {
    private final SpamReportService spamService;
    private final UserListService userService;

    private List<String> nameFilter;
    private List<String> emailFilter;
    private LocalDate startDate;
    private LocalDate endDate;
    private String statusFilter;
    private String orderBy;
    private String orderDir;
    private JScrollPane tableScroll;

    private JPanel filterPanel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Spam(){
        this.spamService = new SpamReportService();
        this.userService = new UserListService();

        // Defaults
        this.nameFilter = new ArrayList<>();
        this.emailFilter = new ArrayList<>();
        this.orderBy = "time";
        this.orderDir = "desc";
        this.startDate = null;
        this.endDate = null;
        this.statusFilter = ""; // All

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        // Load Initial Data
        filterData();
    }

    @Override
    protected void buildFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel();
            filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
            filterPanel.setBackground(MyColor.WHITE_BG);
            filterPanel.setPreferredSize(new Dimension(1920, 80));
            filterPanel.setMaximumSize(new Dimension(1950, 120));
            add(filterPanel, 0);
        }
        filterPanel.removeAll();

        // --- NAME FILTER ---
        FilterButton nameBtn = new FilterButton("Filter by username");
        nameBtn.addFilterAction(nameFilter, this::filterData);
        filterPanel.add(nameBtn);
        for (String item : nameFilter) addTag(item, nameFilter);

        // --- EMAIL FILTER ---
        FilterButton emailBtn = new FilterButton("Filter by email");
        emailBtn.addFilterAction(emailFilter, this::filterData);
        filterPanel.add(emailBtn);
        for (String item : emailFilter) addTag(item, emailFilter);

        filterPanel.add(new JLabel(" | "));

        // --- DATE RANGE ---
        JButton startBtn = createDateButton("Start Date", startDate, true);
        JButton endBtn = createDateButton("End Date", endDate, false);
        filterPanel.add(startBtn);
        filterPanel.add(new JLabel("-"));
        filterPanel.add(endBtn);

        filterPanel.add(new JLabel(" | "));

        // --- STATUS FILTER ---
        JLabel statusLabel = Utility.makeText("Status:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(statusLabel);
        String[] statuses = {"All", "Pending", "Done"};
        RoundedComboBox<String> statusBox = new RoundedComboBox<>(statuses);
        statusBox.setFont(ROBOTO.deriveFont(16f));

        // Restore selection
        if ("pending".equalsIgnoreCase(statusFilter)) statusBox.setSelectedItem("Pending");
        else if ("done".equalsIgnoreCase(statusFilter)) statusBox.setSelectedItem("Done");
        else statusBox.setSelectedItem("All");

        statusBox.addActionListener(e -> {
            String sel = (String) statusBox.getSelectedItem();
            this.statusFilter = (sel == null || sel.equals("All")) ? "" : sel.toLowerCase();
            filterData();
        });
        filterPanel.add(statusBox);

        filterPanel.add(Box.createHorizontalStrut(20));

        // --- SORT ---
        JLabel sortLabel = Utility.makeText("Sort:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(sortLabel);

        String[] sortOptions = {"Time (Newest)", "Time (Oldest)", "Username (A-Z)", "Username (Z-A)"};
        RoundedComboBox<String> sortBox = new RoundedComboBox<>(sortOptions);
        sortBox.setFont(ROBOTO.deriveFont(16f));

        if (orderBy.equals("time") && orderDir.equals("desc")) sortBox.setSelectedIndex(0);
        else if (orderBy.equals("time") && orderDir.equals("asc")) sortBox.setSelectedIndex(1);
        else if (orderBy.equals("username") && orderDir.equals("asc")) sortBox.setSelectedIndex(2);
        else if (orderBy.equals("username") && orderDir.equals("desc")) sortBox.setSelectedIndex(3);

        sortBox.addActionListener(e -> {
            int idx = sortBox.getSelectedIndex();
            if (idx == 0) { orderBy = "time"; orderDir = "desc"; }
            else if (idx == 1) { orderBy = "time"; orderDir = "asc"; }
            else if (idx == 2) { orderBy = "username"; orderDir = "asc"; }
            else if (idx == 3) { orderBy = "username"; orderDir = "desc"; }
            filterData();
        });
        filterPanel.add(sortBox);

        filterPanel.revalidate();
        filterPanel.repaint();
    }

    @Override
    protected void setUpTable() {
        String[] headers = {"Username", "Time", "Email", "Status", "Lock Account", "ReportID", "UserID", "Locked"};

        if (this.data == null) this.data = new ArrayList<>();

        String[][] tableData = Utility.convertToString(this.data);
        table = new CustomTable(tableData, headers);

        // === CRITICAL CHANGE HERE ===
        this.tableScroll = new JScrollPane(table);

        this.tableScroll.setBorder(BorderFactory.createEmptyBorder());
        this.tableScroll.setPreferredSize(new Dimension(1850, 640));

        // 2. VISUAL SETTINGS
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);

        // 3. HIDE COLUMNS
        for (int i = 5; i <= 7; i++) {
            table.getColumnModel().getColumn(i).setMinWidth(0);
            table.getColumnModel().getColumn(i).setMaxWidth(0);
            table.getColumnModel().getColumn(i).setWidth(0);
        }

        // 4. SETUP BUTTONS & ACTIONS (With Error Protection)
        for (int i = 0; i < data.size(); i++) {
            try {
                // Extract Data
                String username = data.get(i).get(0);
                String status = data.get(i).get(3);

                // Safe Parsing
                String reportIdStr = data.get(i).get(5);
                String userIdStr = data.get(i).get(6);
                String isLockedStr = data.get(i).get(7);

                // Use '0' if parsing fails (Safety check)
                long reportId = (reportIdStr == null || reportIdStr.isEmpty()) ? 0 : Long.parseLong(reportIdStr);
                long userId = (userIdStr == null || userIdStr.isEmpty()) ? 0 : Long.parseLong(userIdStr);
                boolean isLocked = Boolean.parseBoolean(isLockedStr);

                // --- STATUS BUTTON ---
                RoundedButton statusBtn = new RoundedButton(10);
                statusBtn.setText(status.substring(0, 1).toUpperCase() + status.substring(1));
                statusBtn.setFont(ROBOTO.deriveFont(Font.BOLD, 14f));
                statusBtn.setFocusPainted(false);

                if ("pending".equalsIgnoreCase(status)) {
                    statusBtn.setBackground(new Color(255, 193, 7)); // Yellow
                    statusBtn.setForeground(Color.BLACK); // Black text is easier to read on Yellow
                    statusBtn.setToolTipText("Click to mark as DONE");

                    statusBtn.addActionListener(e -> {
                        // 1. Get Parent for Custom Popup
                        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(Spam.this);

                        // 2. Use Custom ConfirmPopup
                        if (ConfirmPopup.show(parentFrame, "mark this report as 'Done'")) {
                            try {
                                // 3. Call the service
                                // specific 'reportId' comes from the loop context above
                                spamService.updateStatus(reportId, "done");

                                // 4. Show success
                                JOptionPane.showMessageDialog(this, "Report marked as Done successfully.");

                                // 5. Refresh the table to show the new Green button
                                filterData();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(this,
                                        "Error updating status: " + ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                } else {
                    // Already Done
                    statusBtn.setBackground(MyColor.DARK_GRAY); // Green
                    statusBtn.setForeground(Color.WHITE);
                    statusBtn.setText("Done"); // Force text to be clear
                    statusBtn.setEnabled(false); // Disable button
                }

                JPanel statusWrapper = new JPanel(new GridBagLayout());
                statusWrapper.setBackground(Color.WHITE);
                statusWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
                statusWrapper.add(statusBtn);
                table.setCellComponent(i, 3, statusWrapper);

                // --- LOCK BUTTON ---
                RoundedButton lockBtn = new RoundedButton(15);
                lockBtn.setFocusPainted(false);
// Style based on state
                if (isLocked) {
                    lockBtn.setText("Unlock");
                    lockBtn.setBackground(new Color(23, 162, 184)); // Teal
                    lockBtn.setForeground(Color.WHITE);
                } else {
                    lockBtn.setText("Lock");
                    lockBtn.setBackground(MyColor.DARK_RED); // Red
                    lockBtn.setForeground(Color.WHITE);
                }

// LISTENER LOGIC
                lockBtn.addActionListener(e -> {
                    // 1. Define action name for the dialog
                    String action = isLocked ? "Unlock" : "Lock";

                    // 2. Get Parent for Custom Popup
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(Spam.this);

                    // 3. Custom Confirmation Popup
                    // Text becomes: "Are you sure that you want to unlock user [username] ?"
                    if (ConfirmPopup.show(parentFrame, action.toLowerCase() + " user " + username)) {
                        try {
                            // 4. Call Service based on current state
                            if (isLocked) {
                                userService.unlockUser(userId);
                            } else {
                                userService.lockUser(userId);
                            }

                            // 5. Show Success
                            JOptionPane.showMessageDialog(this, "User " + action + "ed successfully.");

                            // 6. Refresh to update UI
                            filterData();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this,
                                    "Error updating user: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                JPanel lockWrapper = new JPanel(new GridBagLayout());
                lockWrapper.setBackground(Color.WHITE);
                lockWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
                lockWrapper.add(lockBtn);
                table.setCellComponent(i, 4, lockWrapper);

            } catch (Exception e) {
                System.out.println("Error rendering row " + i + ": " + e.getMessage());
                // The loop continues, so other rows still render!
            }
        }
    }

    protected void filterData() {
        // 1. Prepare Arguments
        // Convert List<String> to single String for backend compatibility
        String uFilter = nameFilter.isEmpty() ? null : nameFilter.get(0);
        String eFilter = emailFilter.isEmpty() ? null : emailFilter.get(0);

        // 2. Fetch Data
        List<List<String>> rawData = spamService.getAll(eFilter, uFilter, startDate, endDate, statusFilter, orderBy, orderDir);

        this.data = new ArrayList<>();
        if (rawData != null) {
            for (List<String> row : rawData) {
                List<String> uiRow = new ArrayList<>();
                // Map to UI Table Structure:
                // [0:Username, 1:Time, 2:Email, 3:Status, 4:LockPlaceholder, 5:RepID, 6:UserID, 7:IsLocked]

                uiRow.add(row.get(0)); // Username
                uiRow.add(row.get(1)); // Time
                uiRow.add(row.get(2)); // Email
                uiRow.add(row.get(7)); // Status (e.g., "pending")
                uiRow.add("");         // Placeholder for Lock Button

                // Hidden
                uiRow.add(row.get(4)); // ReportID
                uiRow.add(row.get(5)); // UserID
                uiRow.add(row.get(6)); // IsLocked

                this.data.add(uiRow);
            }
        }

        buildFilterPanel();
        refreshTable();
    }

    // --- HELPERS ---
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

    private JButton createDateButton(String label, LocalDate currentDate, boolean isStart) {
        String btnText = (currentDate == null) ? label : currentDate.format(dateFormatter);
        RoundedButton btn = new RoundedButton(10);
        btn.setText(btnText);
        btn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter date (yyyy-MM-dd):",
                    currentDate != null ? currentDate.toString() : "");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    LocalDate d = LocalDate.parse(input.trim(), dateFormatter);
                    if (isStart) this.startDate = d; else this.endDate = d;
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Format");
                }
            } else {
                if (isStart) this.startDate = null; else this.endDate = null;
            }
            filterData();
        });
        return btn;
    }
    @Override
    protected void refreshTable() {
        // 1. Remove the OLD table if it exists
        if (this.tableScroll != null) {
            remove(this.tableScroll);
        }

        // 2. Build the NEW table (updates this.tableScroll variable)
        setUpTable();

        // 3. Add the NEW table to the screen
        if (this.tableScroll != null) {
            add(this.tableScroll);
        }

        // 4. Tell Swing to redraw
        revalidate();
        repaint();
    }

}