package components;

import model.Msg;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class FriendCardRenderer extends JPanel implements ListCellRenderer<Msg> {

    private final Avatar avatar;
    private final JLabel nameLabel;
    private final JLabel msgLabel;

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

        msgLabel = new JLabel();
        msgLabel.setForeground(Color.GRAY);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(nameLabel, BorderLayout.NORTH);
        centerContainer.add(msgLabel, BorderLayout.SOUTH);

        add(westWrapper, BorderLayout.WEST);
        add(centerContainer, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Msg> list,
            Msg msg,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        // Update data
        avatar.setInitials(msg.getSenderInitials());
        nameLabel.setText(msg.getSenderName());
        msgLabel.setText(msg.getContent());

        // Selection effect
        if (isSelected) {
            setBackground(new Color(0xDDEAFF)); // light blue
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }
}
