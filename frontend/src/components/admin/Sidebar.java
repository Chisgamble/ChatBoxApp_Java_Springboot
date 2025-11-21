package components.admin;

import components.MyColor;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Sidebar extends JPanel {
    private SidebarListener listener;
    private String currentPage = ""; // track current page
    private final Map<String, JButton> buttons = new HashMap<>(); // store buttons by pageName

    public interface SidebarListener {
        void onButtonClicked(String pageName);
    }

    public Sidebar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMinimumSize(new Dimension(180, 1080));
        setMaximumSize(new Dimension(225, 1080));
        setPreferredSize(new Dimension(180, 1080));
        setBackground(MyColor.WHITE_BG);
        setBorder(BorderFactory.createLineBorder(MyColor.LIGHT_BLACK));

        // Create buttons
        createButton("User List", "UserList");
        createButton("Chat Group List", "ChatGroupList");
        createButton("Login Activity", "LoginActivity");
        createButton("Spam", "Spam");
        createButton("New User List", "NewUser");
        createButton("Graphs", "GraphPage");
        createButton("User And Friends", "Friend");
        createButton("Active", "Active");

        setCurrentPage("UserList");
        // add vertical glue at bottom so buttons stick to top
        add(Box.createVerticalGlue());
    }

    private void createButton(String text, String pageName) {
        JButton button = new JButton(text);

        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setForeground(new Color(60, 60, 60));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(MyColor.WHITE_BG);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Make button fill horizontally
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // store in map for later coloring
        buttons.put(pageName, button);

        button.addActionListener(e -> {
            // notify listener
            if (listener != null) listener.onButtonClicked(pageName);

            // update current page highlight
            setCurrentPage(pageName);
        });

        add(button);
    }

    public void setButtonListener(SidebarListener listener) {
        this.listener = listener;
    }

    public void setCurrentPage(String pageName) {
        currentPage = pageName;
        updateButtonColors();
    }

    private void updateButtonColors() {
        for (Map.Entry<String, JButton> entry : buttons.entrySet()) {
            JButton b = entry.getValue();
            if (entry.getKey().equals(currentPage)) {
                b.setBackground(new Color(0xD1DAE3)); // highlight color
                b.setForeground(MyColor.LIGHT_BLACK);
                b.setFont(new Font("Roboto", Font.BOLD, 15));
            } else {
                b.setBackground(MyColor.WHITE_BG);
                b.setForeground(new Color(60, 60, 60));
                b.setFont(new Font("Roboto", Font.BOLD, 14));
            }
        }
    }
}
