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

    // Trong file components.admin.UserList.java

    public void refreshTable() {
        // 1. Xóa tất cả các component cũ đang hiển thị
        this.removeAll();

        // 2. Xây dựng lại giao diện (Filter và Table)
        buildFilterPanel();
        setUpTable();

        // 3. QUAN TRỌNG: Ra lệnh cho Swing tính toán lại bố cục và vẽ lại
        this.revalidate(); // Tính toán lại layout (kích thước, vị trí các component)
        this.repaint();    // Vẽ lại màu sắc, hình ảnh lên màn hình
    }
}
