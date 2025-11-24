package renderer;

import components.Avatar;
import model.Msg;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MsgCardRenderer implements ListCellRenderer<Msg> {

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Msg> list,
            Msg msg,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setPreferredSize(new Dimension(list.getWidth(), 70));
        panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

        panel.setOpaque(true);
        panel.setBackground(isSelected ? new Color(0xE8F1FF) : Color.WHITE);

        Avatar avatar = new Avatar(msg.getSenderInitials());

        JPanel westWrapper = new JPanel(new GridBagLayout());
        westWrapper.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        westWrapper.setOpaque(false);
        westWrapper.add(avatar);

        JLabel nameLabel = new JLabel(msg.getSenderName());
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 15f));

        JLabel msgLabel = new JLabel(msg.getContent());
        msgLabel.setForeground(Color.GRAY);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(nameLabel, BorderLayout.NORTH);
        center.add(msgLabel, BorderLayout.SOUTH);

        panel.add(westWrapper, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }
}
