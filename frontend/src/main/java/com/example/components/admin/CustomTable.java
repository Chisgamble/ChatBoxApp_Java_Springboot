package com.example.components.admin;

import com.example.components.MyColor;
import com.example.util.Utility;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CustomTable extends JTable {

    private final Map<String, JComponent> componentMap = new HashMap<>();

    // Constructor 1: Arrays
    public CustomTable(String[][] data, String[] headers) {
        initTable(data, headers);
    }

    // Constructor 2: Lists
    public CustomTable(List<List<String>> data, String[] headers) {
        String[][] dataArray = Utility.convertToString(data);
        initTable(dataArray, headers);
    }

    // === SHARED INIT METHOD ===
    private void initTable(Object[][] data, Object[] headers) {
        // 1. Explicitly use DefaultTableModel to ensure updateData() works later
        DefaultTableModel model = new DefaultTableModel(data, headers) {
            // Optional: Disable direct text editing in the model,
            // since you handle editing via components in the Table
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.setModel(model);

        // 2. Standard Settings
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);
        setRowHeight(40);

        // 3. Setup Renderers & Editors
        setupRenderers();
        setupEditors();
        setupHeader();
    }

    private void setupRenderers() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                String key = row + "," + column;

                if (componentMap.containsKey(key)) {
                    JComponent comp = componentMap.get(key);
                    comp.setOpaque(true);
                    comp.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    return comp;
                }

                JLabel cell = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setFont(new Font("Roboto", Font.PLAIN, 16));
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

                String txt = cell.getText();
                if ("Active".equals(txt))      cell.setForeground(Color.GREEN);
                else if ("Banned".equals(txt)) cell.setForeground(Color.RED);
                else                           cell.setForeground(MyColor.DARK_GRAY);
                if(column == 0) cell.setForeground(new Color(0x0084FF));

                return cell;
            }
        };

        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setupEditors() {
        setDefaultEditor(Object.class, new TableCellEditor() {
            JComponent currentComp;
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                String key = row + "," + column;
                if (componentMap.containsKey(key)) {
                    currentComp = componentMap.get(key);
                    return currentComp;
                }
                return null;
            }
            @Override public Object getCellEditorValue() { return null; }
            @Override public boolean isCellEditable(java.util.EventObject e) { return true; }
            @Override public boolean shouldSelectCell(java.util.EventObject e) { return true; }
            @Override public boolean stopCellEditing() { return true; }
            @Override public void cancelCellEditing() {}
            @Override public void addCellEditorListener(javax.swing.event.CellEditorListener l) {}
            @Override public void removeCellEditorListener(javax.swing.event.CellEditorListener l) {}
        });
    }

    private void setupHeader() {
        JTableHeader header = getTableHeader();
        header.setBackground(new Color(170, 170, 170, 20));
        header.setForeground(MyColor.DARK_GRAY);
        header.setFont(new Font("Roboto", Font.BOLD, 18));
        Border padding = BorderFactory.createEmptyBorder(10, 0, 10, 0);
        Border bottomLine = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);
        header.setBorder(BorderFactory.createCompoundBorder(bottomLine, padding));
    }

    // Component logic
    public void setCellComponent(int row, int column, JComponent component) {
        String key = row + "," + column;
        component.setOpaque(true);
        // Safety check to ensure column exists
        if(column < getColumnCount()) {
            component.setPreferredSize(new Dimension(
                    getColumnModel().getColumn(column).getWidth(),
                    getRowHeight()
            ));
        }
        componentMap.put(key, component);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Only allow editing if we have a custom component (like a button)
        return componentMap.containsKey(row + "," + column);
    }

    // ================================
    //      UPDATE DATA (FIXED)
    // ================================
    public void updateData(List<List<String>> newData) {
        // This cast is now SAFE because we explicitly used DefaultTableModel in initTable
        DefaultTableModel model = (DefaultTableModel) getModel();

        // 1. Clear Data
        model.setRowCount(0);

        // 2. Clear Components (Status dots must be re-added by UserList after this calls!)
        componentMap.clear();

        // 3. Add New Data
        if (newData != null) {
            for (List<String> row : newData) {
                // toArray() returns Object[], which addRow accepts.
                // This is safer than toArray(new String[0]) if mixed types exist.
                model.addRow(row.toArray());
            }
        }

        // 4. Force UI Refresh
        model.fireTableDataChanged();
    }
}