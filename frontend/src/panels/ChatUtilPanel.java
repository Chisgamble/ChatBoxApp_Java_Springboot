package panels;

import components.Avatar;
import components.FriendCard;
import components.MsgCardList;
import model.Msg;
import model.User;

import java.awt.*;
import java.security.DigestException;

import javax.swing.*;
import javax.swing.border.Border;

import java.util.ArrayList;
import java.util.List;


public class ChatUtilPanel extends JPanel {
    Border border = BorderFactory.createLineBorder(Color.black);
    JPanel centerContainer = new JPanel();
    User user;
    boolean isGroup;
    boolean isAdmin;
    String cur_option = "Search In Chat";
    String[] inboxOptions = {"Search In Chat", "Create Group With", "Unfriend", "Report Spam", "Block"};
    String[] groupOptions = {"Search In Chat", "Members", "Leave Group"};
    String[] groupAdminOptions = {"Search In Chat", "Members", "Encrypt Group", "Delete Group"};

    public ChatUtilPanel(int width, int height, boolean isGroup, boolean isAdmin) {
        this.user = new User("You");
        this.isGroup = isGroup;
        this.isAdmin = isAdmin;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, height));
        this.setBorder(border);
        this.setBackground(Color.BLUE);

        JPanel avatarWrapper = new JPanel();
        avatarWrapper.setLayout(new BoxLayout(avatarWrapper, BoxLayout.Y_AXIS));
        avatarWrapper.setOpaque(false);
        avatarWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        Avatar avatar = new Avatar(user.getInitials(), 100);
        JLabel name = new JLabel(user.getName());
        name.setOpaque(false);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setFont(name.getFont().deriveFont(24f));

        avatarWrapper.add(Box.createVerticalStrut(20));
        avatarWrapper.add(avatar);
        avatarWrapper.add(Box.createVerticalStrut(20));
        avatarWrapper.add(name);
        avatarWrapper.add(Box.createVerticalStrut(20));

        avatarWrapper.validate();
        avatarWrapper.setMaximumSize(new Dimension(Short.MAX_VALUE, avatarWrapper.getPreferredSize().height));

        String[] options;
        if (!isGroup) {
            options = inboxOptions;
        } else if (isGroup && isAdmin) {
            options = groupAdminOptions;
        } else {
            options = groupOptions;
        }

        centerContainer.setLayout(new BorderLayout());
        centerContainer.setPreferredSize(new Dimension(width - 10, height - 60));
        centerContainer.setOpaque(false);

        if (cur_option.equals("Selection")) {
            centerContainer.add(setupOption(options));
        } else if (cur_option.equals("Search In Chat")) {
            List<Msg> msgs = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                msgs.add(new Msg());
            }
            centerContainer.add(new MsgCardList(msgs, width));
        }


//        String[] names = {"Sammael", "Chris", "Doc", "Fridge", "Clockhead"};
//        for (int i = 0; i < 5; i++){
//            User u = new User(names[i]);
//            allUsers.add(u);
//            addMsgCard(u, width);
//        }


//        centerContainer.add(scrollPane, BorderLayout.CENTER);

        this.add(avatarWrapper);
        this.add(centerContainer);

    }

    JPanel setupOption(String[] options) {
        JPanel optionWrapper = new JPanel();
        optionWrapper.setLayout(new BoxLayout(optionWrapper, BoxLayout.Y_AXIS));
        optionWrapper.setOpaque(false);
        optionWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        for (String option : options) {
            JButton button = new JButton(option);
            button.setOpaque(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setFont(button.getFont().deriveFont(14f));
            button.validate();

            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.addActionListener(e -> onOptionButtonClick(option));

            optionWrapper.add(Box.createVerticalStrut(5));
            optionWrapper.add(button);
            optionWrapper.add(Box.createVerticalStrut(5));
        }
        optionWrapper.validate();
        optionWrapper.setMaximumSize(new Dimension(Short.MAX_VALUE, optionWrapper.getPreferredSize().height));
        return optionWrapper;
    }

    private void onOptionButtonClick(String option) {
        // Update the current selected option
        cur_option = option;

        // Change the center panel based on the selected option
        Component newcenterContainer = null;
        if (cur_option.equals("Search In Chat")) {
            List<Msg> msgs = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                msgs.add(new Msg()); // Simulate messages for search
            }
            newcenterContainer = new MsgCardList(msgs,getWidth());
        } else {
            String[] options = null;
            if (!isGroup) {
                options = inboxOptions;
            } else if (isGroup && isAdmin) {
                options = groupAdminOptions;
            } else {
                options = groupOptions;
            }
            newcenterContainer = setupOption(options);
        }

        // Replace the current center panel with the new one

        centerContainer.removeAll(); // Remove old components
        centerContainer.add(newcenterContainer); // Add new panel
        centerContainer.revalidate(); // Revalidate the panel
        centerContainer.repaint(); // Repaint the panel
    }

}
