package com.example.components.admin;

import com.example.components.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

import com.example.components.user.FriendCard;
import com.example.dto.UserDTO;
import com.example.model.User;
import com.example.util.Utility;
import com.example.services.admin.UserListService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserList extends MainPanel {
    private List<String> usernameFilter;
    private List<String> nameFilter;
    private List<String> statusFilter;
    private UserListService userService;

    public UserList() {
        userService = new UserListService();
        List<UserDTO> users = userService.getAll();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        //=== Data ===
        data = users.stream()
                .map(u -> List.of(
                        Utility.safeString(u.username()),
                        Utility.safeString(u.name()),
                        Utility.safeBool(u.is_active()),
                        Utility.safeString(u.address()),
                        Utility.safeString(u.dob()),
                        Utility.safeString(u.gender()),
                        Utility.safeString(u.email())
                ))
                .toList();

        filtered = new ArrayList<>(data);
        usernameFilter = new ArrayList<>();
        nameFilter = new ArrayList<>();
        statusFilter = new ArrayList<>();

        refreshTable();
    }

    protected void setUpTable() {
        // 1. Bỏ header rỗng cuối cùng đi (chỉ còn 7 cột)
        String[] headers = {"Username", "Full name", "Status", "Address", "Date of birth", "Gender", "Email"};

        table = new CustomTable(filtered, headers);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1850, 600));
        add(scroll);

        for (int i = 0; i < filtered.size(); i++) {
            String status = filtered.get(i).get(2).toLowerCase();
            Color statusColor = switch (status) {
                case "true" -> Color.GREEN;
                case "false" -> Color.RED;
                case "offline" -> Color.GRAY;
                default -> Color.BLACK;
            };

            JPanel circle = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(statusColor);
                    g.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            circle.setPreferredSize(new Dimension(12, 12));
            circle.setOpaque(false);

            JPanel statusWrap = new JPanel(new GridBagLayout());
            statusWrap.setBackground(Color.WHITE);
            statusWrap.add(circle);
            statusWrap.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            table.setCellComponent(i, 2, statusWrap);
        }

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
                // Kiểm tra xem có phải là nút kích hoạt popup (thường là chuột phải)
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

    // === Hàm tạo và hiển thị Menu (Tách ra cho gọn) ===
    private void showUserActionMenu(MouseEvent e, int index) {
        JPopupMenu popupMenu = new JPopupMenu();
        String[] options = {"Lock", "Login history", "Update", "Update password", "Refresh password", "Friend list", "Delete"};

        String currentUsername = filtered.get(index).get(0);

        for (String item : options) {
            String text = item;

            if (currentUsername.contains("\uD83D\uDD12") && item.equals("Lock")) {
                text = "Unlock";
            }

            JMenuItem menuItem = new JMenuItem(text);
            popupMenu.add(menuItem);

            // --- Xử lý sự kiện cho từng menu item ---
            if (item.equals("Lock")) {
                menuItem.addActionListener(evt -> {
                    if (filtered.get(index).get(0).contains("\uD83D\uDD12")) {
                        filtered.get(index).set(0, filtered.get(index).get(0).replace("\uD83D\uDD12", ""));
                    } else {
                        filtered.get(index).set(0, "\uD83D\uDD12" + filtered.get(index).get(0));
                    }
                    refreshTable();
                });
            }
            else if (item.equals("Friend list")) {
                menuItem.addActionListener(evt -> userFriendPopup());
            }
            else if (item.equals("Update")) {
                menuItem.addActionListener(evt -> updateUserPopup());
            }
            else if (item.equals("Login history")) {
                menuItem.addActionListener(evt -> userLoginPopup(currentUsername));
            }
            else if (item.equals("Update password")) {
                menuItem.addActionListener(evt -> updatePasswordPopup(currentUsername));
            }
            // Thêm các case khác (Refresh password, Delete) nếu cần
        }

        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    protected void  buildFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        filterPanel.setBackground(MyColor.WHITE_BG);

        RoundedButton addButton = new RoundedButton(25);
        addButton.setText("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(0x0084FF));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        addButton.addActionListener(e -> {
            addUserPopup();
        });
        filterPanel.add(addButton);

        FilterButton usernameFilterBtn = new FilterButton("Filter by groupname");
        //Username filter event
        usernameFilterBtn.addFilterAction(usernameFilter, this::setUpFiltered, this::refreshTable);
        filterPanel.add(usernameFilterBtn);

        for(String item : usernameFilter){
            RoundedButton b = new RoundedButton(10);
            b.setText(item);
            b.setFocusPainted(false);

            setUpRemoveFilter(b, usernameFilter, item);

            filterPanel.add(b);
        }
        // Name filter
        FilterButton nameFilterBtn = new FilterButton("Filter by name");
        //name filter event
        nameFilterBtn.addFilterAction(nameFilter, this::setUpFiltered, this::refreshTable);
        filterPanel.add(nameFilterBtn);

        // Name filter tags
        for(String item : nameFilter){
            RoundedButton b = new RoundedButton(10);
            b.setText(item);
            b.setFocusPainted(false);
            setUpRemoveFilter(b, nameFilter, item);

            filterPanel.add(b);
        }

        FilterButton statusFilterBtn = new FilterButton("Filter by status");
        statusFilterBtn.addFilterAction(statusFilter, this::setUpFiltered, this::refreshTable);
        filterPanel.add(statusFilterBtn);
        // Status filter tags
        for(String item : statusFilter){
            RoundedButton b = new RoundedButton(10);
            b.setText(item);
            b.setFocusPainted(false);
            setUpRemoveFilter(b, statusFilter, item);

            filterPanel.add(b);
        }

        JLabel orderby = Utility.makeText("Order by:", ROBOTO, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);

        String[] options = {"Name", "Account Age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setFont(ROBOTO.deriveFont(16f));

        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setFont(ROBOTO.deriveFont(16f));

        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(orderby);
        filterPanel.add(comboBox);
        filterPanel.add(asc_des);

        filterPanel.revalidate(); // recompute layout
        filterPanel.repaint();

        filterPanel.setPreferredSize(new Dimension(1920, filterPanel.getPreferredSize().height));
        filterPanel.setMaximumSize(new Dimension(1950, 120));

        add(filterPanel);
    }

    private void setUpFiltered(){
        filtered = new ArrayList<>();
        boolean fit = false;
        for (List<String> row : data) {
            fit = false;
            for (String filter : usernameFilter){
                if (row.get(0).toLowerCase().contains(filter.toLowerCase())) {
                    fit = true;
                    break;
                }
            }
            for(String filter : nameFilter){
                if (row.get(1).toLowerCase().contains(filter.toLowerCase())) {
                    fit = true;
                    break;
                }
            }
            for(String filter : statusFilter){
                if (row.get(2).toLowerCase().contains(filter.toLowerCase())) {
                    fit = true;
                    break;
                }
            }
            if(fit || (usernameFilter.isEmpty() && nameFilter.isEmpty() && statusFilter.isEmpty())){
                filtered.add(row);
            }
        }
    }

    private void setUpRemoveFilter(RoundedButton b, List<String> list, String item){
        b.addActionListener(e -> {
            list.remove(item);
            setUpFiltered();
            refreshTable();
        });
    }

    private void addUserPopup() {
        RoundedPanel popup = new RoundedPanel(15);
        popup.setLayout(new BorderLayout());
        popup.setBorder(new EmptyBorder(10,10,10,10));
        popup.setBackground(Color.WHITE); // make sure visible
        popup.setPreferredSize(new Dimension(500, 350));

        String[] options = {"Username", "Gender", "Address", "Email", "Date of birth", "Phone number"};
        JPanel leftWrapper = new JPanel();
        JPanel rightWrapper = new JPanel();
        leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
        rightWrapper.setLayout(new BoxLayout(rightWrapper, BoxLayout.Y_AXIS));
        leftWrapper.setOpaque(false);
        rightWrapper.setOpaque(false);

        for (String item : options) {
            JLabel text = new JLabel(item);
            text.setFont(new Font("Roboto", Font.BOLD, 14));
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 10));
            wrapper.setPreferredSize(new Dimension(150, 30));
            wrapper.setMaximumSize(new Dimension(175, 35));
            wrapper.add(text);
            wrapper.setBackground(Color.WHITE);

            RoundedTextField field = new RoundedTextField(15);
            field.setPreferredSize(new Dimension(175, 30));
            field.setMaximumSize(new Dimension(175, 35));

            leftWrapper.add(wrapper);
            leftWrapper.add(Box.createVerticalStrut(10));
            rightWrapper.add(field);
            rightWrapper.add(Box.createVerticalStrut(10));
        }

        RoundedButton save = new RoundedButton(25);
        save.setText("ADD");
        save.setForeground(MyColor.WHITE_BG);
        save.setBackground(MyColor.LIGHT_BLUE);
        RoundedButton cancel = new RoundedButton(25);
        cancel.setText("CANCEL");
        cancel.setBackground(Color.WHITE);

        JPanel bottomWrapper = new JPanel();
        bottomWrapper.setLayout(new FlowLayout(FlowLayout.TRAILING, 20, 10));
        bottomWrapper.add(save);
        bottomWrapper.add(cancel);

        popup.add(leftWrapper, BorderLayout.WEST);
        popup.add(rightWrapper, BorderLayout.EAST);
        popup.add(bottomWrapper, BorderLayout.SOUTH);


        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add User", true);
        dialog.setContentPane(popup);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void updateUserPopup() {
        RoundedPanel popup = new RoundedPanel(15);
        popup.setLayout(new BorderLayout());
        popup.setBorder(new EmptyBorder(10,10,10,10));
        popup.setBackground(Color.WHITE); // make sure visible
        popup.setPreferredSize(new Dimension(500, 350));

        String[] options = {"Username", "Gender", "Address", "Email", "Date of birth", "Phone number"};
        JPanel leftWrapper = new JPanel();
        JPanel rightWrapper = new JPanel();
        leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
        rightWrapper.setLayout(new BoxLayout(rightWrapper, BoxLayout.Y_AXIS));
        leftWrapper.setOpaque(false);
        rightWrapper.setOpaque(false);

        for (String item : options) {
            JLabel text = new JLabel(item);
            text.setFont(new Font("Roboto", Font.BOLD, 14));
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 10));
            wrapper.setPreferredSize(new Dimension(150, 30));
            wrapper.setMaximumSize(new Dimension(175, 35));
            wrapper.add(text);
            wrapper.setBackground(Color.WHITE);

            RoundedTextField field = new RoundedTextField(15);
            field.setPreferredSize(new Dimension(175, 30));
            field.setMaximumSize(new Dimension(175, 35));
            field.setText("abcabc");

            leftWrapper.add(wrapper);
            leftWrapper.add(Box.createVerticalStrut(10));
            rightWrapper.add(field);
            rightWrapper.add(Box.createVerticalStrut(10));
        }

        RoundedButton save = new RoundedButton(25);
        save.setText("UPDATE");
        save.setForeground(MyColor.WHITE_BG);
        save.setBackground(MyColor.LIGHT_BLUE);
        RoundedButton cancel = new RoundedButton(25);
        cancel.setText("CANCEL");
        cancel.setBackground(Color.WHITE);

        JPanel bottomWrapper = new JPanel();
        bottomWrapper.setLayout(new FlowLayout(FlowLayout.TRAILING, 20, 10));
        bottomWrapper.add(save);
        bottomWrapper.add(cancel);

        popup.add(leftWrapper, BorderLayout.WEST);
        popup.add(rightWrapper, BorderLayout.EAST);
        popup.add(bottomWrapper, BorderLayout.SOUTH);


        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Update user", true);
        dialog.setContentPane(popup);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void userFriendPopup() {
       JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Friend list", true);

       JPanel wrapper = new JPanel();
       wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
       for(int i = 0; i < 10; i++){
           User u = new User("BABA");
           FriendCard friend = new FriendCard( u, 400);
           wrapper.add(friend);
           wrapper.add(Box.createVerticalStrut(20));
       }
       JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setPreferredSize(new Dimension(600, 500));
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        dialog.add(scroll);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private void userLoginPopup(String username) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Login log", true);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        for(int i = 0; i < 10; i++){
            JPanel a = new JPanel();
            a.setLayout(new BorderLayout());
            a.add(new JLabel(username), BorderLayout.WEST);
            a.add(new JLabel("12/12/2025"), BorderLayout.EAST);
            wrapper.add(a);
            wrapper.setBorder(new EmptyBorder(5, 15, 5, 15));
            wrapper.add(Box.createVerticalStrut(20));
        }
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setPreferredSize(new Dimension(600, 500));
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        dialog.add(scroll);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private void updatePasswordPopup(String s) {
        RoundedPanel popup = new RoundedPanel(15);
        popup.setLayout(new BorderLayout());
        popup.setBorder(new EmptyBorder(10,10,10,10));
        popup.setBackground(Color.WHITE); // make sure visible
        popup.setPreferredSize(new Dimension(500, 200));

        String[] options = {"Old password", "New password"};
        JPanel leftWrapper = new JPanel();
        JPanel rightWrapper = new JPanel();
        leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
        rightWrapper.setLayout(new BoxLayout(rightWrapper, BoxLayout.Y_AXIS));
        leftWrapper.setOpaque(false);
        rightWrapper.setOpaque(false);

        for (String item : options) {
            JLabel text = new JLabel(item);
            text.setFont(new Font("Roboto", Font.BOLD, 14));
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 10));
            wrapper.setPreferredSize(new Dimension(150, 30));
            wrapper.setMaximumSize(new Dimension(175, 35));
            wrapper.add(text);
            wrapper.setBackground(Color.WHITE);

            RoundedPasswordField field = new RoundedPasswordField(15);
            field.setPreferredSize(new Dimension(175, 30));
            field.setMaximumSize(new Dimension(175, 35));
            field.setText("abcabc");

            leftWrapper.add(wrapper);
            leftWrapper.add(Box.createVerticalStrut(10));
            rightWrapper.add(field);
            rightWrapper.add(Box.createVerticalStrut(10));
        }

        RoundedButton update = new RoundedButton(25);
        update.setText("UPDATE");
        update.setForeground(MyColor.WHITE_BG);
        update.setBackground(MyColor.LIGHT_BLUE);
        RoundedButton cancel = new RoundedButton(25);
        cancel.setText("CANCEL");
        cancel.setBackground(Color.WHITE);

        JPanel bottomWrapper = new JPanel();
        bottomWrapper.setLayout(new FlowLayout(FlowLayout.TRAILING, 20, 10));
        bottomWrapper.add(update);
        bottomWrapper.add(cancel);

        popup.add(leftWrapper, BorderLayout.WEST);
        popup.add(rightWrapper, BorderLayout.EAST);
        popup.add(bottomWrapper, BorderLayout.SOUTH);


        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Update password", true);
        dialog.setContentPane(popup);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        cancel.addActionListener(e -> dialog.dispose());
        update.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}
