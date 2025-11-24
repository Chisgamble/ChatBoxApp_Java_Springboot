package components.admin;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class MainPanel extends JPanel {
    protected List<List<String>> data;
    protected final Font ROBOTO = new Font("Roboto", Font.PLAIN, 16);
    protected List<List<String>> filtered;
    protected CustomTable table;

    public MainPanel(){}

    public MainPanel(List<List<String>> data) {
        this.data = data;
        this.filtered = data; // default
    }

    protected abstract void buildFilterPanel();
    protected abstract void setUpTable();

    protected void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            removeAll();
            revalidate();
            repaint();

            buildFilterPanel();
            setUpTable();
        });
    }
}
