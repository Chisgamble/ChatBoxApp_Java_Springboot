package com.example.renderer;

import com.example.components.Avatar;
import com.example.dto.MsgDTO;
import com.example.dto.MsgType;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

import static com.example.util.Utility.getInitials;

public class GlobalMsgCardRenderer implements ListCellRenderer<MsgDTO> {

    @Override
    public Component getListCellRendererComponent(
            JList<? extends MsgDTO> list,
            MsgDTO msg,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {

        String title;
        if (msg.getType() == MsgType.GROUP) {
            title = msg.getGroupName();
        } else {
            title = msg.getSenderName();
        }

        String content;
        if (msg.getType() == MsgType.GROUP) {
            content = msg.getSenderName() + ": " + msg.getContent();
        } else {
            content = msg.getContent();
        }

        String initials = getInitials(title);

        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setPreferredSize(new Dimension(list.getWidth(), 70));
        panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        panel.setOpaque(true);
        panel.setBackground(
                isSelected ? new Color(0xE8F1FF) : Color.WHITE
        );

        // ===== Avatar
        Avatar avatar = new Avatar(initials);
        JPanel westWrapper = new JPanel(new GridBagLayout());
        westWrapper.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        westWrapper.setOpaque(false);
        westWrapper.add(avatar);

        // ===== Labels
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                titleLabel.getFont().deriveFont(Font.BOLD, 15f)
        );
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JLabel contentLabel = new JLabel(content);
        contentLabel.setForeground(Color.DARK_GRAY);
        contentLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(titleLabel, BorderLayout.NORTH);
        center.add(contentLabel, BorderLayout.SOUTH);

        panel.add(westWrapper, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }
}
