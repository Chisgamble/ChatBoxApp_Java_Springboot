package components.admin;

import components.MyColor;
import util.Utility;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CustomTable extends JTable {

    private final Map<String, JComponent> componentMap = new HashMap<>();

    public CustomTable(String[][] data, String[] headers) {
        super(data, headers);

        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);
        setRowHeight(40);

        // ================================
        //          RENDERER
        // ================================
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                String key = row + "," + column;

                // If custom component exists → use it as renderer
                if (componentMap.containsKey(key)) {
                    JComponent comp = componentMap.get(key);

                    // Make sure the wrapper panel is opaque
                    comp.setOpaque(true);

                    // Change background on selection
                    if (isSelected) {
                        comp.setBackground(table.getSelectionBackground());
                    } else {
                        comp.setBackground(Color.WHITE);
                    }

                    return comp;
                }


                JLabel cell = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setFont(new Font("Roboto", Font.PLAIN, 16));
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

                // coloring logic
                String txt = cell.getText();
                if ("Active".equals(txt))      cell.setForeground(Color.GREEN);
                else if ("Banned".equals(txt)) cell.setForeground(Color.RED);
                else                           cell.setForeground(MyColor.DARK_GRAY);
                if(column == 0) cell.setForeground(new Color(0x0084FF));

                return cell;
            }
        };

        // Apply renderer to all columns
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ================================
        //          EDITOR
        // ================================
        setDefaultEditor(Object.class, new TableCellEditor() {

            JComponent currentComp;

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                String key = row + "," + column;

                if (componentMap.containsKey(key)) {
                    currentComp = componentMap.get(key);
                    return currentComp;  // this editor is actually the component
                }
                return null;
            }

            @Override public Object getCellEditorValue() {
                return null; // component is direct, no value needed
            }

            // Required methods
            @Override public boolean isCellEditable(java.util.EventObject e) { return true; }
            @Override public boolean shouldSelectCell(java.util.EventObject e) { return true; }
            @Override public boolean stopCellEditing() { return true; }
            @Override public void cancelCellEditing() {}
            @Override public void addCellEditorListener(javax.swing.event.CellEditorListener l) {}
            @Override public void removeCellEditorListener(javax.swing.event.CellEditorListener l) {}
        });

        // ================================
        //          HEADER STYLE
        // ================================
        JTableHeader header = getTableHeader();
        header.setBackground(new Color(170, 170, 170, 20));
        header.setForeground(MyColor.DARK_GRAY);
        header.setFont(new Font("Roboto", Font.BOLD, 18));
        Border padding = BorderFactory.createEmptyBorder(10, 0, 10, 0);

        Border bottomLine = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);

        header.setBorder(BorderFactory.createCompoundBorder(bottomLine, padding));
    }

    public CustomTable(List<List<String>> data, String[] headers) {
        super(Utility.convertToString(data), headers);

        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);
        setRowHeight(40);

        // ================================
        //          RENDERER
        // ================================
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                String key = row + "," + column;

                // If custom component exists → use it as renderer
                if (componentMap.containsKey(key)) {
                    JComponent comp = componentMap.get(key);

                    // Make sure the wrapper panel is opaque
                    comp.setOpaque(true);

                    // Change background on selection
                    if (isSelected) {
                        comp.setBackground(table.getSelectionBackground());
                    } else {
                        comp.setBackground(Color.WHITE);
                    }

                    return comp;
                }


                JLabel cell = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setFont(new Font("Roboto", Font.PLAIN, 16));
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

                // coloring logic
                String txt = cell.getText();
                if ("Active".equals(txt))      cell.setForeground(Color.GREEN);
                else if ("Banned".equals(txt)) cell.setForeground(Color.RED);
                else                           cell.setForeground(MyColor.DARK_GRAY);
                if(column == 0) cell.setForeground(new Color(0x0084FF));

                return cell;
            }
        };

        // Apply renderer to all columns
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ================================
        //          EDITOR
        // ================================
        setDefaultEditor(Object.class, new TableCellEditor() {

            JComponent currentComp;

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                String key = row + "," + column;

                if (componentMap.containsKey(key)) {
                    currentComp = componentMap.get(key);
                    return currentComp;  // this editor is actually the component
                }
                return null;
            }

            @Override public Object getCellEditorValue() {
                return null; // component is direct, no value needed
            }

            // Required methods
            @Override public boolean isCellEditable(java.util.EventObject e) { return true; }
            @Override public boolean shouldSelectCell(java.util.EventObject e) { return true; }
            @Override public boolean stopCellEditing() { return true; }
            @Override public void cancelCellEditing() {}
            @Override public void addCellEditorListener(javax.swing.event.CellEditorListener l) {}
            @Override public void removeCellEditorListener(javax.swing.event.CellEditorListener l) {}
        });

        // ================================
        //          HEADER STYLE
        // ================================
        JTableHeader header = getTableHeader();
        header.setBackground(new Color(170, 170, 170, 20));
        header.setForeground(MyColor.DARK_GRAY);
        header.setFont(new Font("Roboto", Font.BOLD, 18));
        Border padding = BorderFactory.createEmptyBorder(10, 0, 10, 0);

        Border bottomLine = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);

        header.setBorder(BorderFactory.createCompoundBorder(bottomLine, padding));
    }

    // ================================
    //      ADD COMPONENT TO CELL
    // ================================
    public void setCellComponent(int row, int column, JComponent component) {
        String key = row + "," + column;

        component.setOpaque(true);
        component.setPreferredSize(new Dimension(
                getColumnModel().getColumn(column).getWidth(),
                getRowHeight()
        ));

        componentMap.put(key, component);

        repaint(getCellRect(row, column, false));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return componentMap.containsKey(row + "," + column);
    }
}
