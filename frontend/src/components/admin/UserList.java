package components.admin;

import components.MyColor;
import components.RoundedButton;
import components.RoundedComboBox;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import util.Utility;
import javax.swing.*;
import java.awt.*;

public class UserList extends MainPanel {
    private List<String> usernameFilter;
    private List<String> nameFilter;
    private List<String> statusFilter;

    public UserList() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        //=== Data ===
        data = Arrays.asList(
                Arrays.asList("thaibao", "Nguyễn Thái Bảo", "Active", "Ho Chi Minh City", "2003-05-20", "Male", "bao@gmail.com", ""),
                Arrays.asList("linhngoc", "Phạm Ngọc Linh", "Banned", "Da Nang", "2002-11-02", "Female", "linh@gmail.com", ""),
                Arrays.asList("minhduc", "Trần Minh Đức", "Active", "Hanoi", "2001-08-12", "Male", "duc@gmail.com", "")
        );
        filtered = new ArrayList<>(data);
        usernameFilter = new ArrayList<>();
        nameFilter = new ArrayList<>();
        statusFilter = new ArrayList<>();

        refreshTable();
    }

    protected void setUpTable(){
        String[] headers = {"Username","Full name","Status","Address","Date of birth","Gender","Email",""};

        table = new CustomTable(filtered, headers);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(1920, 800));
        add(scroll);
        // === Status + Action column customization ===
        for (int i = 0; i < filtered.size(); i++) {

            // button column
            RoundedButton action = new RoundedButton(15);
            action.setText("...");
            action.setBorder(BorderFactory.createEmptyBorder(3, 10, 4, 10));
            action.setFont(ROBOTO.deriveFont(Font.BOLD, 26f));
            action.setBackground(new Color(255,255,255,1));
            action.setFocusPainted(false);

            JPanel btnWrap = new JPanel(new GridBagLayout());
            btnWrap.add(action);
            btnWrap.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            table.setCellComponent(i, 7, btnWrap);

            // status color
            String status = filtered.get(i).get(2).toLowerCase();
            Color statusColor = switch (status) {
                case "active" -> Color.GREEN;
                case "banned" -> Color.RED;
                case "offline" -> Color.GRAY;
                default -> Color.BLACK;
            };

            // circle
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
        add(Box.createVerticalGlue());
    }

    protected void  buildFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        filterPanel.setBackground(MyColor.WHITE_BG);

        FilterButton usernameFilterBtn = new FilterButton("Filter by username");
        //Username filter event
        usernameFilterBtn.addFilterAction(usernameFilter, this::setUpFiltered, this::refreshTable);
        filterPanel.add(usernameFilterBtn);

        //username filter tags
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
}
