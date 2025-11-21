package components.admin;

import components.MyColor;
import components.RoundedComboBox;

import javax.swing.*;
import java.awt.*;

public class LoginActivity extends JPanel {

    public LoginActivity() {
        Font roboto = new Font("Roboto", Font.PLAIN, 16);

        // This panel IS the main page content
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.LIGHT_BLUE);

        // === Table Data ===
        String[] headers = {"Username", "Full name", "Time"};
        String[][] data = {
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"},
                {"thaibao", "Nguyễn Thái Bảo", "2003-05-20"}
        };

        CustomTable table = new CustomTable(data, headers);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        scroll.setPreferredSize(new Dimension(1920, 800));

        // === Optional: sorting comboboxes (if you want them) ===
        String[] options = {"Name", "Account Age"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(roboto);

        String[] sortOptions = {"Ascending", "Descending"};
        RoundedComboBox<String> asc_des = new RoundedComboBox<>(sortOptions);
        asc_des.setForeground(MyColor.DARK_GRAY);
        asc_des.setFont(roboto);

        // === Add components to main panel ===
        add(scroll);                 // table scroll
        add(Box.createVerticalGlue()); // push content to top if space

    }
}
