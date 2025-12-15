package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedTextField;
import com.example.dto.YearlyGraphDTO;
import com.example.services.admin.LoginLogService;
import com.example.services.admin.UserListService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphPage extends JPanel {

    private final UserListService userService;
    private final LoginLogService loginLogService;

    private final ModernLineChart newUserChart;
    private final ModernLineChart activeUserChart;

    private final RoundedTextField yearInputNewUsers;
    private final RoundedTextField yearInputActiveUsers;

    public GraphPage() {
        this.userService = new UserListService();
        this.loginLogService = new LoginLogService();

        // 1. Main Layout
        setLayout(new BorderLayout());
        setBackground(MyColor.WHITE_BG);

        // 2. Content Panel (Vertical Stack)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(MyColor.WHITE_BG);

        // ==========================================
        // SECTION 1: NEW USERS GRAPH
        // ==========================================

        // 1A. Control Panel
        JPanel topPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel1.setBackground(MyColor.WHITE_BG);
        topPanel1.setMaximumSize(new Dimension(2000, 60));
        topPanel1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label1 = new JLabel("New Users Year: ");
        label1.setFont(new Font("Roboto", Font.BOLD, 14));

        yearInputNewUsers = new RoundedTextField(10);
        yearInputNewUsers.setText(String.valueOf(Year.now().getValue()));
        yearInputNewUsers.setPreferredSize(new Dimension(80, 35));

        RoundedButton loadBtn1 = new RoundedButton(10);
        loadBtn1.setText("Load");
        loadBtn1.addActionListener(e -> loadNewUserData()); // <--- Listener 1

        topPanel1.add(label1);
        topPanel1.add(yearInputNewUsers);
        topPanel1.add(loadBtn1);

        // 1B. Chart
        newUserChart = new ModernLineChart("New Users per Month", "New Users");
        newUserChart.setPreferredSize(new Dimension(1000, 450));
        newUserChart.setMaximumSize(new Dimension(1000, 450));
        newUserChart.setBackground(Color.WHITE);
        newUserChart.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(topPanel1);
        contentPanel.add(newUserChart);

        // Spacer
        contentPanel.add(Box.createVerticalStrut(40));

        // ==========================================
        // SECTION 2: ACTIVE USERS GRAPH
        // ==========================================

        // 2A. Control Panel
        JPanel topPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel2.setBackground(MyColor.WHITE_BG);
        topPanel2.setMaximumSize(new Dimension(2000, 60));
        topPanel2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label2 = new JLabel("Active Users Year: ");
        label2.setFont(new Font("Roboto", Font.BOLD, 14));

        yearInputActiveUsers = new RoundedTextField(10);
        yearInputActiveUsers.setText(String.valueOf(Year.now().getValue()));
        yearInputActiveUsers.setPreferredSize(new Dimension(80, 35));

        RoundedButton loadBtn2 = new RoundedButton(10);
        loadBtn2.setText("Load");
        loadBtn2.addActionListener(e -> loadActiveUserData()); // <--- Listener 2

        topPanel2.add(label2);
        topPanel2.add(yearInputActiveUsers);
        topPanel2.add(loadBtn2);

        // 2B. Chart
        activeUserChart = new ModernLineChart("Active Users per Month", "Active Users");
        activeUserChart.setPreferredSize(new Dimension(1000, 450));
        activeUserChart.setMaximumSize(new Dimension(1000, 450));
        activeUserChart.setBackground(Color.WHITE);
        activeUserChart.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(topPanel2);
        contentPanel.add(activeUserChart);

        // Push everything up
        contentPanel.add(Box.createVerticalGlue());

        // 3. Scroll Pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- Initial Load ---
        loadNewUserData();
        loadActiveUserData();
    }

    // --- LOGIC 1: Load New Users ---
    private void loadNewUserData() {
        int year = parseYear(yearInputNewUsers);
        if (year == -1) return;

        YearlyGraphDTO dto = userService.getNewUserGraph(year);
        updateChart(newUserChart, dto);
    }

    // --- LOGIC 2: Load Active Users ---
    private void loadActiveUserData() {
        int year = parseYear(yearInputActiveUsers);
        if (year == -1) return;

        YearlyGraphDTO dto = loginLogService.getActiveUserGraph(year);
        updateChart(activeUserChart, dto);
    }

    // --- HELPER METHODS ---

    private int parseYear(RoundedTextField field) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Year");
            return -1;
        }
    }

    private void updateChart(ModernLineChart chart, YearlyGraphDTO dto) {
        if (dto != null && dto.data() != null) {
            Map<LocalDate, Integer> chartData = new HashMap<>();
            List<Long> counts = dto.data();

            for (int i = 0; i < counts.size(); i++) {
                LocalDate date = LocalDate.of(dto.year(), i + 1, 1);
                chartData.put(date, counts.get(i).intValue());
            }
            chart.updateData(chartData);
        } else {
            chart.updateData(new HashMap<>()); // Clear if empty
        }
    }
}