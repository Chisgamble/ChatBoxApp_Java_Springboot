package components.user;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import components.Avatar;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MemberCard extends JPanel {

    public MemberCard(User user, int width) {
//        this.setLayout(new BorderLayout(10, 0));
//        this.setOpaque(false);
//        this.setPreferredSize(new Dimension(width, 60));
//        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
//        this.setMinimumSize(new Dimension(width, 60));
//        this.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(Color.BLACK, 1),  // LineBorder for the border
//                new EmptyBorder(5,5,5,5)  // EmptyBorder for padding (10px on all sides)
//        ));
//
//        Avatar avatar = new Avatar(user.getInitials());
//
//        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        westWrapper.setOpaque(false); // optional, to be transparent
//        westWrapper.add(avatar);
//
//        JLabel nameLabel = new JLabel(user.getName());
//        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16f));
//
//        FlatSVGIcon icon = null;
//        if (user.isActive())
//            icon = new FlatSVGIcon("assets/online-icon.svg", 10, 10);
//        else
//            icon = new FlatSVGIcon("assets/offline-icon.svg", 10, 10);
//
//        // Set icon on JLabel
//        nameLabel.setIcon(icon);
//
//        // Position the icon relative to text
//        nameLabel.setHorizontalTextPosition(SwingConstants.LEFT); // text on the left, icon on the right
//        nameLabel.setVerticalTextPosition(SwingConstants.CENTER); // vertically centered
//        nameLabel.setIconTextGap(5); // gap between text and icon
//
//        JLabel msgLabel = new JLabel(user.isAdmin() ? "Admin" : "Member");
//        msgLabel.setForeground(Color.GRAY);
//        msgLabel.setOpaque(false);
//        msgLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
//
//        JPanel centerContainer = new JPanel(new BorderLayout());
//        centerContainer.setOpaque(false);
//        centerContainer.add(nameLabel, BorderLayout.NORTH);
//        centerContainer.add(msgLabel, BorderLayout.SOUTH);
//
//        this.add(westWrapper, BorderLayout.WEST);
//        this.add(centerContainer, BorderLayout.CENTER);
//
    }
}
