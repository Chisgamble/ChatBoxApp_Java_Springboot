package com.example.components.admin;

import com.example.components.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.example.components.admin.FriendCard;
import com.example.dto.FriendCardDTO;
import com.example.dto.LoginLogDTO;
import com.example.dto.UserDTO;
import com.example.dto.request.AdminCreateOrUpdateUserReqDTO;
import com.example.model.User;
import com.example.services.AuthService;
import com.example.services.FriendService;
import com.example.services.admin.LoginLogService;
import com.example.util.Utility;
import com.example.services.admin.UserListService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class UserList extends MainPanel {
    private List<String> usernameFilter;
    private List<String> nameFilter;
    private String status;
    private String sort;
    private String order;
    private String role;
    private UserListService userService;
    private LoginLogService loginLogService;
    private FriendService friendService;
    private AuthService authService;
    private JPanel filterPanel;
    private List<UserDTO> users;

    public UserList() {
        userService = new UserListService();
        loginLogService = new LoginLogService();
        authService = new AuthService();
        friendService = new FriendService();
        sort = "username";
        order = "asc";
        status = "all";
        role = "all";
        usernameFilter = new ArrayList<>();
        nameFilter = new ArrayList<>();


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        filterData(); // This populates 'this.data'

        init();
    }

    protected void setUpTable() {
        // 1. Bỏ header rỗng cuối cùng đi (chỉ còn 7 cột)
        String[] headers = {"Username", "Full name", "Status", "Address", "Date of birth", "Gender", "Email"};
        table = new CustomTable(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing for ALL cells (Action is via Right-Click)
            }
        };

        table.getTableHeader().setReorderingAllowed(false);
        addStatusDots();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1850, 640));
        add(scroll);

        table.getColumnModel().getColumn(2).setMinWidth(75);
        table.getColumnModel().getColumn(2).setMaxWidth(75);
        table.getColumnModel().getColumn(2).setWidth(75);
        table.getColumnModel().getColumn(5).setMinWidth(75);
        table.getColumnModel().getColumn(5).setMaxWidth(75);
        table.getColumnModel().getColumn(5).setWidth(75);

//      Chuot phai
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handleContextMenu(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleContextMenu(e);
            }

            private void handleContextMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    // Lấy vị trí dòng dựa trên toạ độ chuột
                    int row = table.rowAtPoint(e.getPoint());

                    // Nếu click vào một dòng hợp lệ
                    if (row >= 0 && row < table.getRowCount()) {
                        // Tự động bôi đen (select) dòng đó
                        table.setRowSelectionInterval(row, row);

                        // Hiển thị menu
                        showUserActionMenu(e, row);
                    }
                }
            }
        });

        add(Box.createVerticalGlue());
    }

    private void showUserActionMenu(MouseEvent e, int index) {
        JPopupMenu popupMenu = new JPopupMenu();
        String[] options = {"Lock", "Login history", "Update", "Update password", "Refresh password", "Friend list", "Delete"};

        String currentUsername = data.get(index).get(0);
        String currentEmail = data.get(index).get(6); // Get Email from column 6

        // 1. Find the UserDTO to get ID and is_locked status
        UserDTO targetUser = users.stream()
                .filter(u -> u.email().equals(currentEmail))
                .findFirst()
                .orElse(null);

        if (targetUser == null) return; // Safety check

        for (String item : options) {
            String text = item;

            // 2. Determine Text based on UserDTO status
            if (item.equals("Lock")) {
                if (targetUser.is_locked()) {
                    text = "Unlock";
                } else {
                    text = "Lock";
                }
            }

            JMenuItem menuItem = new JMenuItem(text);
            popupMenu.add(menuItem);

            // --- Xử lý sự kiện cho từng menu item ---
            if (item.equals("Lock")) {
                menuItem.addActionListener(evt -> {
                    try {
                        if (targetUser.is_locked()) {
                            // Logic: UNLOCK
                            int confirm = JOptionPane.showConfirmDialog(null,
                                    "Are you sure you want to UNLOCK this user?",
                                    "Confirm Unlock", JOptionPane.YES_NO_OPTION);

                            if (confirm == JOptionPane.YES_OPTION) {
                                userService.unlockUser(targetUser.id());
                                JOptionPane.showMessageDialog(null, "User unlocked successfully.");
                                filterData(); // Refresh from Server
                            }
                        } else {
                            // Logic: LOCK
                            int confirm = JOptionPane.showConfirmDialog(null,
                                    "Are you sure you want to LOCK this user?\nThey will not be able to log in.",
                                    "Confirm Lock", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                            if (confirm == JOptionPane.YES_OPTION) {
                                userService.lockUser(targetUser.id());
                                JOptionPane.showMessageDialog(null, "User locked successfully.");
                                filterData(); // Refresh from Server
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                });
            }
            else if (item.equals("Friend list")) {
                menuItem.addActionListener(evt -> userFriendPopup(currentEmail));
            }
            else if (item.equals("Update")) {
                menuItem.addActionListener(evt -> updateUserPopup());
            }
            else if (item.equals("Login history")) {
                menuItem.addActionListener(evt -> userLoginPopup(currentUsername, currentEmail));
            }
            else if (item.equals("Update password")) {
                menuItem.addActionListener(evt -> updatePasswordPopup(currentUsername));
            }
            else if (item.equals("Delete")) {
                menuItem.setForeground(Color.RED);

                menuItem.addActionListener(evt -> {
                    // 1. CONFIRMATION DIALOG
                    int confirm = JOptionPane.showConfirmDialog(
                            null, // Use 'this' (UserList panel) as parent, not null
                            "Are you sure you want to PERMANENTLY delete this user?\n"
                                    + "User: " + currentUsername + "\n"
                                    + "This action cannot be undone.",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE // Red 'X' icon
                    );

                    // 2. CALL SERVICE & REFRESH
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            userService.deleteUser(targetUser.id());

                            JOptionPane.showMessageDialog(null, "User deleted successfully.");

                            // 3. REFRESH TABLE
                            filterData();

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Could not delete user. Error: " + ex.getMessage(),
                                    "Delete Failed",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }

            else if (item.equals("Refresh password")) {
                menuItem.addActionListener(evt -> {
                    // 1. CONFIRMATION DIALOG
                    int confirm = JOptionPane.showConfirmDialog(
                            null, // Parent component
                            "Are you sure you want to reset the password for user: " + currentUsername + "?\n"
                                    + "A randomly generated password will be sent to " + currentEmail + ".",
                            "Confirm Password Reset",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    // 2. CALL SERVICE
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            // Assuming userService has the resetPassword method you defined earlier
                            authService.resetPassword(currentEmail);

                            JOptionPane.showMessageDialog(null,
                                    "Password reset successfully.\nThe new password has been emailed to the user.");

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Failed to reset password. Error: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
            // Thêm các case khác (Refresh password, Delete) nếu cần
        }

        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    protected void filterData() {
        users = userService.getAll(
                this.usernameFilter,
                this.nameFilter,
                this.status,
                this.role,
                this.sort,
                this.order
        );

        // 2. Convert List<UserDTO> to the table data format (List<List<Object>>)
        // This matches the exact logic from your constructor
        if (users != null) {
            this.data = users.stream()
                    .filter(u -> u.username() != null && u.name() != null)
                    .map(u -> List.of(
                            Utility.safeString(u.is_locked() ? "🔒" +u.username()  : u.username()),
                            Utility.safeString(u.name()),
                            Utility.safeBool(u.is_active()),
                            Utility.safeString(u.address()),
                            Utility.safeString(u.dob()),
                            Utility.safeString(u.gender()),
                            Utility.safeString(u.email())
                    ))
                    .toList(); // or .collect(Collectors.toList()) if on older Java
        } else {
            this.data = new ArrayList<>();
        }
        buildFilterPanel();
        refreshTable();
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

        // === ADD BUTTON ===
        RoundedButton addButton = new RoundedButton(25);
        addButton.setText("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(0x0084FF));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        addButton.addActionListener(e -> addUserPopup());
        filterPanel.add(addButton);

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
        FilterButton nameFilterBtn = new FilterButton("Filter by name");
        nameFilterBtn.addFilterAction(nameFilter, this::filterData);
        filterPanel.add(nameFilterBtn);

        // DYNAMIC TAGS (Name)
        for(String item : nameFilter){
            RoundedButton b = new RoundedButton(10);
            b.setText(item);
            b.setFocusPainted(false);
            setUpRemoveFilter(b, nameFilter, item);
            filterPanel.add(b);
        }

        // === STATUS DROPDOWN ===
        JLabel statusFilter = Utility.makeText("Filter by status:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(statusFilter);

        String[] statusOptions = {"All", "Active", "Offline"};
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

        filterPanel.add(new JLabel(" | "));

        JLabel roleFilter = Utility.makeText("Filter by role:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(roleFilter);

        String[] roleOptions = {"All", "Admin", "User"};
        RoundedComboBox<String> roleBox = new RoundedComboBox<>(roleOptions);
        roleBox.setFont(ROBOTO.deriveFont(16f));

        // MEMORY FIX: Set selected item based on current variable
        if(this.status != null) {
            String display = this.status.substring(0, 1).toUpperCase() + this.status.substring(1).toLowerCase();
            roleBox.setSelectedItem(display);
        }

        roleBox.addActionListener(e -> {
            String selected = (String) roleBox.getSelectedItem();
            if (selected != null) {
                this.role = selected.toLowerCase();
                filterData(); // Reloads data AND rebuilds panel
            }
        });
        filterPanel.add(roleBox);
        filterPanel.add(new JLabel(" | "));
        // === SORT DROPDOWN ===
        JLabel orderby = Utility.makeText("Order by:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(orderby);

        String[] options = {"Name", "Account Age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setFont(ROBOTO.deriveFont(16f));

        // MEMORY FIX
        if("username".equals(this.sort)) comboBox.setSelectedItem("Name");
        else comboBox.setSelectedItem("Account Age");

        comboBox.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            if (selected != null){
                this.sort = selected.equals("Name") ? "username" : "age";
                filterData();
            }
        });
        filterPanel.add(comboBox);

        // === ORDER DROPDOWN ===
        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setFont(ROBOTO.deriveFont(16f));

        // MEMORY FIX
        if("asc".equals(this.order)) asc_des.setSelectedItem("Ascending");
        else asc_des.setSelectedItem("Descending");

        asc_des.addActionListener(e -> {
            String selected = (String) asc_des.getSelectedItem();
            if (selected != null){
                this.order = selected.equals("Ascending") ? "asc" : "desc";
                filterData();
            }
        });
        filterPanel.add(asc_des);

        // Finalize Layout Update
        filterPanel.revalidate();
        filterPanel.repaint();
    }

    private void setUpRemoveFilter(RoundedButton b, List<String> list, String item){
        b.addActionListener(e -> {
            list.remove(item);
            filterData();
            refreshTable();
            addStatusDots();
        });
    }

    private void addStatusDots() {
        if (table == null) return;

        // 1. Find the View Index of the "Status" column dynamically
        int statusColumnIndex = -1;
        for (int i = 0; i < table.getColumnCount(); i++) {
            // Check the header name
            if ("Status".equalsIgnoreCase(table.getColumnName(i))) {
                statusColumnIndex = i;
                break;
            }
        }
        if (statusColumnIndex == -1) return;

        for (int i = 0; i < data.size(); i++) {
            // Safety check for rows
            if (i >= table.getRowCount()) break;

            String status = data.get(i).get(2).toLowerCase();

            Color statusColor = switch (status) {
                case "true", "active" -> Color.GREEN;
                case "false", "offline" -> Color.RED;
                default -> Color.BLACK;
            };

            // Create the circle component
            JPanel circle = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(statusColor);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            circle.setPreferredSize(new Dimension(12, 12));
            circle.setOpaque(false);

            JPanel statusWrap = new JPanel(new GridBagLayout());
            statusWrap.setBackground(Color.WHITE);
            statusWrap.add(circle);
            statusWrap.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            table.setCellComponent(i, statusColumnIndex, statusWrap);
        }
    }

    @Override
    protected void afterRefresh(){
        addStatusDots();
    }

    private void addUserPopup() {
        RoundedPanel popup = new RoundedPanel(15);
        popup.setLayout(new BorderLayout());
        popup.setBorder(new EmptyBorder(10, 10, 10, 10));
        popup.setBackground(Color.WHITE);
        popup.setPreferredSize(new Dimension(500, 500));

        // Store all inputs as JComponent (works for both TextField and ComboBox)
        Map<String, JComponent> inputs = new HashMap<>();

        String[] keys = {"Username", "Name", "Gender", "Address", "Email", "Date of birth", "Role", "Password"};

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        for (String key : keys) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false);
            row.setPreferredSize(new Dimension(480, 45));
            row.setMaximumSize(new Dimension(480, 50));
            row.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

            JLabel label = new JLabel(key.replace(" (YYYY-MM-DD)", ""));
            label.setFont(new Font("Roboto", Font.BOLD, 14));
            label.setPreferredSize(new Dimension(150, 45));

            JComponent inputField;

            // 1. Create Dropdowns for Gender & Role
            if (key.equals("Gender")) {
                String[] options = {"Male", "Female", "Other"};
                inputField = new RoundedComboBox<>(options);
                inputField.setBackground(Color.WHITE);
            }
            else if (key.equals("Date of birth")){
                RoundedTextField field = new RoundedTextField(15);
                field.addFocusListener(new java.awt.event.FocusAdapter() {
                    @Override
                    public void focusGained(java.awt.event.FocusEvent e) {
                        // User clicked: Clear placeholder
                        if (field.getText().equals("YYYY-MM-DD")) {
                            field.setText("");
                            field.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(java.awt.event.FocusEvent e) {
                        // User left: If empty, put placeholder back
                        if (field.getText().trim().isEmpty()) {
                            field.setText("YYYY-MM-DD");
                            field.setForeground(Color.GRAY);
                        }
                    }
                });
                inputField = field;
            }
            else if (key.equals("Role")) {
                String[] options = {"User", "Admin"};
                inputField = new RoundedComboBox<>(options);
                inputField.setBackground(Color.WHITE);
            }
            // 2. Create TextFields for everything else
            else {
                RoundedTextField field = new RoundedTextField(15);
                inputField = field;
            }

            inputField.setPreferredSize(new Dimension(150, 45));
            inputField.setMaximumSize(new Dimension(175, 45));
            inputs.put(key, inputField); // Save to map

            row.add(label, BorderLayout.WEST);
            row.add(inputField, BorderLayout.EAST);
            centerWrapper.add(row);
        }

        RoundedButton save = new RoundedButton(25);
        save.setText("ADD");
        save.setForeground(Color.WHITE);
        save.setBackground(new Color(0x0084FF));

        RoundedButton cancel = new RoundedButton(25);
        cancel.setText("CANCEL");
        cancel.setBackground(Color.WHITE);

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.TRAILING, 20, 10));
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(save);
        bottomWrapper.add(cancel);

        popup.add(centerWrapper, BorderLayout.CENTER);
        popup.add(bottomWrapper, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add User", true);
        dialog.setContentPane(popup);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        cancel.addActionListener(e -> dialog.dispose());

        // --- SAVE LOGIC ---
        save.addActionListener(e -> {
            try {
                // 1. Extract Values manually (Casting based on knowledge of the key)
                String email = ((JTextField) inputs.get("Email")).getText().trim();
                String dob = ((JTextField) inputs.get("Date of birth")).getText().trim();

                // For ComboBox, we cast to JComboBox to get selected item
                String gender = (String) ((JComboBox<?>) inputs.get("Gender")).getSelectedItem();
                String role = (String) ((JComboBox<?>) inputs.get("Role")).getSelectedItem();

                String username = ((JTextField) inputs.get("Username")).getText().trim();
                String password = ((JTextField) inputs.get("Password")).getText().trim();
                String name = ((JTextField) inputs.get("Name")).getText().trim();
                String address = ((JTextField) inputs.get("Address")).getText().trim();

                // 2. Validation
                if (!email.contains("@")) {
                    throw new Exception("Email must contain '@'");
                }
                if (!dob.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    throw new Exception("Invalid Date! Use YYYY-MM-DD (e.g., 2024-01-30)");
                }

                // 3. Create DTO & Call Service
                // Note: role.toLowerCase() ensures "User" becomes "user" for backend
                AdminCreateOrUpdateUserReqDTO req = new AdminCreateOrUpdateUserReqDTO(
                        username, name, password, gender, address, email, dob, role.toLowerCase()
                );

                userService.createUser(req);

                filterData();
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "User created successfully!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void updateUserPopup() {
        int rowIndex = table.getSelectedRow();
        if (rowIndex == -1) return;

        List<String> rowData = data.get(rowIndex);
        System.out.print(rowData);

        RoundedPanel popup = new RoundedPanel(15);
        popup.setLayout(new BorderLayout());
        popup.setBorder(new EmptyBorder(10, 10, 10, 10));
        popup.setBackground(Color.WHITE);
        popup.setPreferredSize(new Dimension(500, 500)); // Taller for Combos

        Map<String, JComponent> inputs = new HashMap<>();

        String[] keys = {"Username", "Name", "Gender", "Address", "Email", "Date of birth", "Role"};

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        for (String key : keys) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false);
            row.setPreferredSize(new Dimension(480, 40));
            row.setMaximumSize(new Dimension(480, 50));
            row.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

            JLabel label = new JLabel(key);
            label.setFont(new Font("Roboto", Font.BOLD, 14));
            label.setPreferredSize(new Dimension(150, 40));

            JComponent inputField;

            // --- BUILD COMPONENT & PRE-FILL DATA ---
            if (key.equals("Gender")) {
                String[] options = {"Male", "Female", "Other"};
                RoundedComboBox<String> box = new RoundedComboBox<>(options);
                box.setSelectedItem(rowData.get(5));
                inputField = box;
            }
            else if (key.equals("Role")) {
                String[] options = {"User", "Admin"};
                RoundedComboBox<String> box = new RoundedComboBox<>(options);
                // NOTE: Role is not in your current table data.
                // Defaulting to "User" or you need to fetch it separately.
                box.setSelectedItem("User");
                inputField = box;
            }
            else {
                // Text Fields
                RoundedTextField field = new RoundedTextField(15);

                // Map keys to table indices manually to ensure correct data
                String value = switch (key) {
                    case "Username" -> rowData.get(0);
                    case "Name"     -> rowData.get(1);
                    case "Address"  -> rowData.get(3);
                    case "Email"    -> rowData.get(6);
                    case "Date of birth" -> rowData.get(4);
                    default -> "";
                };

                field.setText(value);

                // Optional: Make Username read-only
//                if (key.equals("Username")) {
//                    field.setEditable(false);
//                    field.setBackground(new Color(245, 245, 245));
//                }

                inputField = field;
            }

            inputField.setPreferredSize(new Dimension(200, 40));
            inputs.put(key, inputField); // Store for retrieval

            row.add(label, BorderLayout.WEST);
            row.add(inputField, BorderLayout.CENTER);
            centerWrapper.add(row);
        }

        RoundedButton updateBtn = new RoundedButton(25);
        updateBtn.setText("UPDATE");
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(new Color(0x0084FF));

        RoundedButton cancel = new RoundedButton(25);
        cancel.setText("CANCEL");
        cancel.setBackground(Color.WHITE);

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.TRAILING, 20, 10));
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(updateBtn);
        bottomWrapper.add(cancel);

        popup.add(centerWrapper, BorderLayout.CENTER);
        popup.add(bottomWrapper, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Update User", true);
        dialog.setContentPane(popup);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        cancel.addActionListener(e -> dialog.dispose());

        // --- UPDATE ACTION ---
        updateBtn.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    dialog,
                    "Are you sure you want to update this user's information?",
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                try {
                    // Extract Values
                    String username = ((JTextField) inputs.get("Username")).getText();
                    String name     = ((JTextField) inputs.get("Name")).getText();
                    String address  = ((JTextField) inputs.get("Address")).getText();
                    String email    = ((JTextField) inputs.get("Email")).getText();
                    String dob      = ((JTextField) inputs.get("Date of birth")).getText();

                    // Cast JComponent to JComboBox to get selection
                    String gender   = (String) ((JComboBox<?>) inputs.get("Gender")).getSelectedItem();
                    String role     = (String) ((JComboBox<?>) inputs.get("Role")).getSelectedItem();

                    // Create DTO
                    // Note: password is null because we are not updating it here
                    // Note: role.toLowerCase() to match backend expectation ("User" -> "user")
                    AdminCreateOrUpdateUserReqDTO req = new AdminCreateOrUpdateUserReqDTO(
                            username,
                            name,
                            null, // password
                            gender,
                            address,
                            email,
                            dob,
                            role != null ? role.toLowerCase() : "user"
                    );


                    Long userId = users.stream()
                            .filter(u -> u.email().equals(rowData.get(6))) // Match email
                            .findFirst()
                            .map(UserDTO::id)                           // Extract ID
                            .orElseThrow(() -> new Exception("Could not find user ID for email: " + rowData.get(6)));
                    // ============================================================

                    userService.updateUser(userId, req);

                    filterData();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(null, "User updated successfully!");

                } catch (Exception ex) {
                    ex.printStackTrace(); // Helpful for debugging
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.setVisible(true);
    }

    private void userFriendPopup(String currentEmail) {
        // 1. Resolve User ID from the current list based on Email
        // We do this BEFORE the SwingWorker to ensure we have the ID ready.
        Long targetUserId;
        try {
            targetUserId = users.stream()
                    .filter(u -> u.email().equals(currentEmail))
                    .findFirst()
                    .map(UserDTO::id)
                    .orElseThrow(() -> new RuntimeException("User not found in list"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: Could not identify user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Setup the Dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Friend List", true);
        dialog.setSize(400, 550);
        dialog.setLayout(new BorderLayout());

        // 3. Container for the list
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);

        // 4. Scroll Pane
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        dialog.add(scroll, BorderLayout.CENTER);

        // 5. Loading Indicator
        JLabel loadingLabel = new JLabel("Loading friends...", SwingConstants.CENTER);
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(Box.createVerticalStrut(20));
        wrapper.add(loadingLabel);

        // 6. Fetch Data (Background Thread) using the resolved targetUserId
        new SwingWorker<List<FriendCardDTO>, Void>() {
            @Override
            protected List<FriendCardDTO> doInBackground() {
                return friendService.getAllById(targetUserId);
            }

            @Override
            protected void done() {
                wrapper.removeAll(); // Clear loading

                try {
                    List<FriendCardDTO> friends = get();

                    if (friends == null || friends.isEmpty()) {
                        wrapper.add(Box.createVerticalStrut(50));
                        JLabel empty = new JLabel("No friends found.");
                        empty.setAlignmentX(Component.CENTER_ALIGNMENT);
                        wrapper.add(empty);
                    } else {
                        for (FriendCardDTO dto : friends) {
                            FriendCard card = new FriendCard(dto);
                            card.setAlignmentX(Component.LEFT_ALIGNMENT);
                            wrapper.add(card);

                            JSeparator sep = new JSeparator();
                            sep.setMaximumSize(new Dimension(Short.MAX_VALUE, 1));
                            sep.setForeground(new Color(240, 240, 240));
                            wrapper.add(sep);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    wrapper.add(Box.createVerticalStrut(20));
                    JLabel error = new JLabel("Failed to load friends.");
                    error.setForeground(Color.RED);
                    error.setAlignmentX(Component.CENTER_ALIGNMENT);
                    wrapper.add(error);
                }

                wrapper.revalidate();
                wrapper.repaint();
            }
        }.execute();

        // 7. Show Dialog
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    private void userLoginPopup(String username, String email) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Login History: " + username, true);

        List<LoginLogDTO> logs = loginLogService.getByEmail(email);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);
        // Apply border to the wrapper once, not inside the loop
        wrapper.setBorder(new EmptyBorder(10, 15, 10, 15));

        if (logs == null || logs.isEmpty()) {
            JLabel noLogs = new JLabel("No login history found.");
            noLogs.setAlignmentX(Component.CENTER_ALIGNMENT);
            wrapper.add(noLogs);
        } else {
            for (LoginLogDTO log : logs) {
                JPanel row = new JPanel(new BorderLayout());
                row.setMaximumSize(new Dimension(550, 30));
                row.setBackground(Color.WHITE);
                // Add a subtle separator line
                row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

                // --- Left: Username + Status Color ---
                JLabel leftLabel = new JLabel(username);
                leftLabel.setFont(new Font("Roboto", Font.BOLD, 14));

                // Visual indicator: Green for Success, Red for Fail
                if (log.isSuccess()) {
                    leftLabel.setForeground(new Color(34, 139, 34)); // Green
                    leftLabel.setText("✔ " + username);
                } else {
                    leftLabel.setForeground(Color.RED);
                    leftLabel.setText("✘ " + username);
                }

                // --- Right: Time ---
                // Backend sends ISO string (e.g. 2025-12-12T10:15:30)
                // Replace 'T' with space for readability
                String timeStr = log.createdAt() != null
                        ? log.createdAt().replace("T", " ")
                        : "Unknown";

                // Optional: trim milliseconds if present
                if (timeStr.contains(".")) {
                    timeStr = timeStr.substring(0, timeStr.indexOf("."));
                }

                JLabel rightLabel = new JLabel(timeStr);
                rightLabel.setFont(new Font("Roboto", Font.PLAIN, 13));
                rightLabel.setForeground(Color.GRAY);

                row.add(leftLabel, BorderLayout.WEST);
                row.add(rightLabel, BorderLayout.EAST);

                wrapper.add(row);
                // Space between rows
                wrapper.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setPreferredSize(new Dimension(600, 500));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        dialog.add(scroll);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    private void updatePasswordPopup(String currentUsername) {
        RoundedPanel popup = new RoundedPanel(15);
        popup.setLayout(new BorderLayout());
        popup.setBorder(new EmptyBorder(20, 20, 20, 20));
        popup.setBackground(Color.WHITE);
        popup.setPreferredSize(new Dimension(500, 280));

        // Store inputs to retrieve later
        Map<String, JPasswordField> inputs = new HashMap<>();
        String[] options = {"Old password", "New password"};

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        // Font for the "Eye" icon (using Unicode or plain text)
        Font iconFont = new Font("SansSerif", Font.BOLD, 16);

        FlatSVGIcon eyeOpenIcon = new FlatSVGIcon("assets/solar--eye-bold.svg", 20, 20);
        FlatSVGIcon eyeCrossedIcon = new FlatSVGIcon("assets/solar--eye-closed-broken.svg", 20, 20);
        for (String item : options) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(480, 50));
            row.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

            // 1. Label
            JLabel label = new JLabel(item);
            label.setFont(new Font("Roboto", Font.BOLD, 14));
            label.setPreferredSize(new Dimension(130, 40));

            // 2. Field
            RoundedPasswordField field = new RoundedPasswordField(15);
            field.setPreferredSize(new Dimension(200, 40));
            field.setEchoChar('•');
            inputs.put(item, field);

            // 3. Eye Button (Toggle Visibility)
            JButton eyeBtn = new JButton();
            eyeBtn.setIcon(eyeCrossedIcon);

            eyeBtn.setPreferredSize(new Dimension(40, 40));
            eyeBtn.setFocusPainted(false);
            eyeBtn.setBorderPainted(false);
            eyeBtn.setContentAreaFilled(false);
            eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Eye Button Logic
            eyeBtn.addActionListener(e -> {
                if (field.getEchoChar() != (char) 0) {
                    // SHOW PASSWORD
                    field.setEchoChar((char) 0);
                    eyeBtn.setIcon(eyeOpenIcon); // Show "crossed out" eye
                } else {
                    // HIDE PASSWORD
                    field.setEchoChar('•');
                    eyeBtn.setIcon(eyeCrossedIcon); // Show normal eye
                }
            });

            // 4. Wrapper to hold Field + Eye Button
            JPanel fieldWrapper = new JPanel(new BorderLayout(5, 0));
            fieldWrapper.setOpaque(false);
            fieldWrapper.add(field, BorderLayout.CENTER);
            fieldWrapper.add(eyeBtn, BorderLayout.EAST);

            row.add(label, BorderLayout.WEST);
            row.add(fieldWrapper, BorderLayout.CENTER);
            centerWrapper.add(row);
        }

        // === BUTTONS ===
        RoundedButton updateBtn = new RoundedButton(25);
        updateBtn.setText("UPDATE");
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(new Color(0x0084FF));
        updateBtn.setPreferredSize(new Dimension(100, 35));

        RoundedButton cancel = new RoundedButton(25);
        cancel.setText("CANCEL");
        cancel.setBackground(Color.WHITE);
        cancel.setPreferredSize(new Dimension(100, 35));

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 0));
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(updateBtn);
        bottomWrapper.add(cancel);

        popup.add(centerWrapper, BorderLayout.CENTER);
        popup.add(bottomWrapper, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Update Password", true);
        dialog.setContentPane(popup);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        cancel.addActionListener(e -> dialog.dispose());

        // === UPDATE ACTION ===
        updateBtn.addActionListener(e -> {
            try {
                String oldPass = new String(inputs.get("Old password").getPassword());
                String newPass = new String(inputs.get("New password").getPassword());

                // 1. Validation
                if (oldPass.isEmpty() || newPass.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Both fields are required.");
                    return;
                }

                // 2. Call Service (using the new ChangePassword flow)
                authService.changePassword(oldPass, newPass);

                JOptionPane.showMessageDialog(dialog, "Password updated successfully!");
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
}
