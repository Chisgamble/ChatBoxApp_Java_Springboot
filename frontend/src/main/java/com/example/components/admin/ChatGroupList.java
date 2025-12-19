package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedButton;
import com.example.components.RoundedComboBox;
import com.example.components.RoundedPanel;
import com.example.dto.GroupDataDTO;
import com.example.dto.GroupMemberDTO;
import com.example.services.GroupService;
import com.example.util.Utility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatGroupList extends MainPanel {

    // Dependencies and Data State
    private final GroupService groupService;
    private List<GroupDataDTO> groups;
    private JPanel filterPanel;
    private JScrollPane tableScroll; // Added for manual management (like Spam.java)

    // Filter State
    private List<String> nameFilter;
    private LocalDate startDate;
    private LocalDate endDate;
    private String sortBy;    // "name" or "time"
    private String sortDir;   // "asc" or "desc"

    // Formatter for UI display and input parsing (YYYY-MM-DD)
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Formatter used internally to send full timestamp strings to the backend API
    private final DateTimeFormatter apiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ChatGroupList() {
        this.groupService = new GroupService();

        // Defaults
        this.nameFilter = new ArrayList<>();
        this.sortBy = "name";
        this.sortDir = "asc";
        this.startDate = null;
        this.endDate = null;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        filterData();
    }

    @Override
    protected void buildFilterPanel() {
        // ... (Filter panel logic remains the same) ...
        if (filterPanel == null) {
            filterPanel = new JPanel();
            filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
            filterPanel.setBackground(MyColor.WHITE_BG);
            filterPanel.setPreferredSize(new Dimension(1920, 80));
            filterPanel.setMaximumSize(new Dimension(1950, 120));
            add(filterPanel, 0);
        }

        filterPanel.removeAll();
        Font roboto = ROBOTO.deriveFont(16f);

        // === NAME FILTER ===
        FilterButton nameFilterBtn = new FilterButton("Filter by name");
        nameFilterBtn.addFilterAction(nameFilter, this::filterData);
        filterPanel.add(nameFilterBtn);


        // Display Name Filter Tags
        for(String item : nameFilter){
            addTag(item, nameFilter);
        }

        filterPanel.add(new JLabel(" | "));

        // Separator
        filterPanel.add(Box.createHorizontalStrut(20));

        // === DATE FILTERS (Start & End) ===
        JButton startBtn = createDateButton("Start Date", startDate, true);
        JButton endBtn = createDateButton("End Date", endDate, false);

        filterPanel.add(startBtn);
        filterPanel.add(new JLabel("-"));
        filterPanel.add(endBtn);

        // Separator
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel(" | "));

        // === ORDER DROPDOWN ===
        JLabel orderby = Utility.makeText("Order by:", roboto, 16f, Font.PLAIN, MyColor.DARK_GRAY, null);
        filterPanel.add(orderby);

        // Sort Field (Name vs Time)
        String[] options = {"Group Name", "Group Age"};
        RoundedComboBox<String> sortFieldBox = new RoundedComboBox<>(options);
        sortFieldBox.setFont(roboto);

        sortFieldBox.setSelectedItem("time".equals(this.sortBy) ? "Group Age" : "Group Name");
        sortFieldBox.addActionListener(e -> {
            String selected = (String) sortFieldBox.getSelectedItem();
            this.sortBy = "Group Age".equals(selected) ? "time" : "name";
            filterData();
        });
        filterPanel.add(sortFieldBox);

        // Sort Direction (Asc/Desc)
        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> sortDirBox = new RoundedComboBox<>(sortOptions);
        sortDirBox.setFont(roboto);

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

    private JButton createDateButton(String label, LocalDate currentDate, boolean isStart) {
        String btnText = (currentDate == null) ? label : currentDate.format(dateFormatter);
        RoundedButton btn = new RoundedButton(10);
        btn.setText(btnText);
        btn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(null,
                    "Enter date (yyyy-MM-dd):",
                    currentDate != null ? currentDate.format(dateFormatter) : ""
            );

            if (input != null && !input.trim().isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(input.trim(), dateFormatter);
                    if (isStart) this.startDate = date;
                    else this.endDate = date;
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid Format. Please use YYYY-MM-DD.",
                            "Date Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (isStart) this.startDate = null;
                else this.endDate = null;
            }
            filterData();
        });
        return btn;
    }

    private void addTag(String text, List<String> list) {
        RoundedButton b = new RoundedButton(10);
        b.setText(text);
        b.setFocusPainted(false);
        b.setForeground(Color.BLACK);
        b.addActionListener(e -> {
            list.remove(text);
            filterData();
        });
        filterPanel.add(b);
    }

    @Override
    protected void setUpTable() {
        // 1. Convert List<List<String>> data to String[][] for CustomTable
        String[] headers = {"ID", "Chat group name", "Time created", "Participants"};
        String[][] tableData = Utility.convertToString(this.data);
        table = new CustomTable(tableData, headers); // CustomTable constructor takes String[][]
        table.getTableHeader().setReorderingAllowed(false);
        // === START MANUAL BUTTON SETUP (Like Spam.java) ===

        // 2. Wrap the table in the scroll pane
        this.tableScroll = new JScrollPane(table);
        this.tableScroll.setBorder(BorderFactory.createEmptyBorder());
        this.tableScroll.setPreferredSize(new Dimension(1920, 800));

        // 3. HIDE ID COLUMN (Index 0)
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // 4. ITERATE AND ADD BUTTONS
        for (int i = 0; i < data.size(); i++) {
            // Data needed: The Group ID is in column 0 of the hidden data row.
            final int currentRow = i;

            // --- VIEW PARTICIPANTS BUTTON ---
            RoundedButton participantsBtn = new RoundedButton(15);
            participantsBtn.setText("View Participants");
            participantsBtn.setBackground(MyColor.LIGHT_BLUE);
            participantsBtn.setForeground(Color.WHITE);
            participantsBtn.setFocusPainted(false);
            participantsBtn.setFont(ROBOTO.deriveFont(Font.PLAIN, 14f));

            // Listener calls the action method with the current row index
            participantsBtn.addActionListener(e -> openParticipantsDialog(currentRow));

            // Wrapper panel to center the button in the cell
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setBackground(Color.WHITE);
            wrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)); // Optional separator line
            wrapper.add(participantsBtn);

            // CRITICAL: Place the component directly in the cell (Column 3)
            table.setCellComponent(i, 3, wrapper);
        }

        // 5. Add the scroll pane to the panel (this happens in refreshTable now)
        add(Box.createVerticalGlue());
    }

    @Override
    protected void refreshTable() {
        // 1. Remove the OLD table scroll if it exists
        if (this.tableScroll != null) {
            remove(this.tableScroll);
        }

        // 2. Build the NEW table and set up buttons
        setUpTable();

        // 3. Add the NEW table scroll to the screen
        if (this.tableScroll != null) {
            add(this.tableScroll);
        }

        // 4. Tell Swing to redraw
        revalidate();
        repaint();
    }

    protected void filterData() {
        // 1. Prepare API parameters
        String startStr = null;
        if (startDate != null) {
            startStr = startDate.atStartOfDay().format(apiFormatter);
        }

        String endStr = null;
        if (endDate != null) {
            endStr = endDate.atTime(23, 59, 59).format(apiFormatter);
        }

        // 2. Call Service
        groups = groupService.getGroupListData(
                nameFilter,
                startStr,
                endStr,
                sortBy,
                sortDir
        );

        // 3. Map DTO (GroupDataDTO) to Table Data (List<List<String>>)
        if (groups != null) {
            this.data = groups.stream()
                    .map(g -> {
                        List<String> row = new ArrayList<>();
                        row.add(String.valueOf(g.id())); // Index 0 (ID - HIDDEN)
                        row.add(Utility.safeString(g.groupName())); // Index 1
                        row.add(Utility.safeString(g.createdAt())); // Index 2
                        row.add("View Participants"); // Index 3 (Placeholder text)
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

    // This method remains the same, using the row index to access the correct group ID
    private void openParticipantsDialog(int row) {
        if (groups == null || row < 0 || row >= groups.size()) {
            JOptionPane.showMessageDialog(null, "Group data not available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GroupDataDTO group = groups.get(row);
        List<GroupMemberDTO> members = groupService.getAllMembers(group.id());

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Participants in: " + group.groupName(),
                true);
        dialog.setSize(450, 600);
        dialog.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerWrapper = new JPanel(new GridBagLayout());
        headerWrapper.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerWrapper.setBackground(MyColor.WHITE_BG);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 0, 0, 10);
        g.anchor = GridBagConstraints.WEST;

        JLabel groupName = new JLabel(group.groupName());
        groupName.setFont(ROBOTO.deriveFont(Font.BOLD, 18f));
        headerWrapper.add(groupName, g);

        g.gridx = 1;
        g.weightx = 1.0;
        g.anchor = GridBagConstraints.EAST;

        String timeStr = group.createdAt() != null ? group.createdAt() : "";
        JLabel timeCreated = new JLabel(timeStr);
        timeCreated.setFont(ROBOTO.deriveFont(Font.PLAIN, 14f));
        headerWrapper.add(timeCreated, g);

        dialog.add(headerWrapper, BorderLayout.NORTH);

        // Member List Panel
        JPanel memberListPanel = new JPanel();
        memberListPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        memberListPanel.setLayout(new BoxLayout(memberListPanel, BoxLayout.Y_AXIS));
        memberListPanel.setBackground(Color.WHITE);

        if (members.isEmpty()) {
            memberListPanel.add(Utility.makeText("No members found.", ROBOTO, 14f, Font.PLAIN, MyColor.DARK_GRAY, null));
        } else {
            for (GroupMemberDTO member : members) {
                RoundedPanel memberWrapper = new RoundedPanel();
                memberWrapper.setLayout(new BorderLayout());
                memberWrapper.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                memberWrapper.setPreferredSize(new Dimension(360, 32));
                memberWrapper.setMaximumSize(new Dimension(500, 35));
                memberWrapper.setBackground(new Color(0,0,0, 15));
                memberWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel name = Utility.makeText(member.getUsername(), ROBOTO, 14f, Font.PLAIN, MyColor.DARK_GRAY, null);
                JLabel role = Utility.makeText("Role: " + member.getRole(), ROBOTO, 14f, Font.PLAIN, MyColor.DARK_GRAY, null);

                memberWrapper.add(name, BorderLayout.WEST);
                memberWrapper.add(role, BorderLayout.EAST);

                memberListPanel.add(memberWrapper);
                memberListPanel.add(Box.createVerticalStrut(5));
            }
        }

        JScrollPane scrollPane = new JScrollPane(memberListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
    }

    // --- Removed the flawed ButtonRenderer and ButtonEditor classes ---
}