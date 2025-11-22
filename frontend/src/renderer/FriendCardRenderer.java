package renderer;

import components.Avatar;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class FriendCardRenderer extends JPanel implements ListCellRenderer<User> {

    private final Avatar avatar;
    private final JLabel nameLabel;
    private final JLabel userLabel;

    public FriendCardRenderer(int width) {
        setLayout(new BorderLayout(10, 0));
        setOpaque(true);
        setPreferredSize(new Dimension(width, 60));
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        avatar = new Avatar(" ");

        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        westWrapper.setOpaque(false);
        westWrapper.add(avatar);

        nameLabel = new JLabel();
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        nameLabel.setHorizontalTextPosition(SwingConstants.LEFT); // text on the left, icon on the right
        nameLabel.setVerticalTextPosition(SwingConstants.CENTER); // vertically centered
        nameLabel.setIconTextGap(5);

        userLabel = new JLabel();
        userLabel.setForeground(Color.GRAY);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(nameLabel, BorderLayout.NORTH);
        centerContainer.add(userLabel, BorderLayout.SOUTH);

        add(westWrapper, BorderLayout.WEST);
        add(centerContainer, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends User> list,
            User user,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        // Update data
        avatar.setInitials(user.getInitials());
        nameLabel.setText(user.getName());
        FlatSVGIcon icon;
        if (user.isActive())
            icon = new FlatSVGIcon("assets/online-icon.svg", 10, 10);
        else
            icon = new FlatSVGIcon("assets/offline-icon.svg", 10, 10);
        nameLabel.setIcon(icon);
        userLabel.setText(user.getLastMsg());

        // Selection effect
        if (isSelected) {
            setBackground(new Color(0xDDEAFF)); // light blue
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }
}
