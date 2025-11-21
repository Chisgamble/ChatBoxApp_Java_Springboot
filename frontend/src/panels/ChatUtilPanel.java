package panels;

import components.Avatar;
import components.FriendCard;
import components.MsgCardList;
import components.SearchBar;
import listener.SearchBarListener;
import model.Msg;
import model.User;

import java.awt.*;
import java.security.DigestException;

import javax.swing.*;
import javax.swing.border.Border;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.util.ArrayList;
import java.util.List;


public class ChatUtilPanel extends JPanel implements SearchBarListener {
    Border border = BorderFactory.createLineBorder(Color.black);
    JPanel topContainer = new JPanel();
    JPanel centerContainer = new JPanel();
    Component listContainer = null;
    
    User user;
    List<Msg> msgs = new ArrayList<>();
    List<Msg> filteredMsgs = new ArrayList<>();
    List<User> allMembers = new ArrayList<>();
    List<User> filteredMembers = new ArrayList<>();
    
    boolean isGroup;
    boolean isAdmin;
    String cur_option = "Selection";
    String[] inboxOptions = {"Search In Chat", "Create Group With", "Delete All Chat History", "Unfriend", "Report Spam", "Block"};
    String[] groupOptions = {"Search In Chat", "Members", "Leave Group"};
    String[] groupAdminOptions = {"Search In Chat", "Members", "Encrypt Group","Delete All Chat History", "Delete Group"};

    public ChatUtilPanel(int width, int height, boolean isGroup, boolean isAdmin) {
        this.user = new User("You");
        this.msgs = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            msgs.add(new Msg());
        }
        
        this.isGroup = isGroup;
        this.isAdmin = isAdmin;

        this.setLayout(new BorderLayout());
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
            listContainer = setupOption(options);
            centerContainer.add(listContainer, BorderLayout.CENTER);
        } else if (cur_option.equals("Search In Chat")) {
            listContainer = new MsgCardList(msgs, width - 15);
            centerContainer.add(listContainer, BorderLayout.CENTER);
        }


//        String[] names = {"Sammael", "Chris", "Doc", "Fridge", "Clockhead"};
//        for (int i = 0; i < 5; i++){
//            User u = new User(names[i]);
//            allUsers.add(u);
//            addMsgCard(u, width);
//        }


//        centerContainer.add(scrollPane, BorderLayout.CENTER);
        topContainer = avatarWrapper;
        this.add(topContainer, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);

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

        topContainer.removeAll();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        topContainer.setBorder(BorderFactory.createEmptyBorder(20,5,20,5));
        SearchBar sb = new SearchBar(20,5, getWidth(), 30, this);
        JLabel label = new JLabel(new FlatSVGIcon("assets/x-solid-full.svg", 24, 24));

        topContainer.add(Box.createVerticalStrut(20));
        topContainer.add(sb);
        topContainer.add(Box.createHorizontalGlue());
        topContainer.add(label);
        topContainer.add(Box.createVerticalStrut(20));

        // Change the center panel based on the selected option
        Component newcenterContainer = null;
        if (cur_option.equals("Search In Chat")) {
            newcenterContainer = new MsgCardList(msgs,getWidth() - 15);
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

        // Replace the current panel with the new one
        centerContainer.removeAll(); // Remove old components
        centerContainer.add(newcenterContainer); // Add new panel
        this.revalidate();
        this.repaint();
    }

    void updateMsgList(List<Msg> msgs){
        listContainer = new MsgCardList(msgs, getWidth()-15);
        listContainer.revalidate();
        listContainer.repaint();
    }

    void updateMemberList(List<User> members){


        // Revalidate and repaint the list
        listContainer.revalidate();
        listContainer.repaint();
    }

    @Override
    public void onSearchChange(String text) {
        if (cur_option.equals("Search In Chat")) {
            filteredMsgs.clear();
            if (text.isEmpty() || text.equals("Search")) {
                filteredMsgs.addAll(msgs);  // Show all users if the search bar is empty
            } else {
                for (Msg msg : msgs) {
                    if (msg.getContent().toLowerCase().contains(text.toLowerCase())) {
                        filteredMsgs.add(msg);
                    }
                }
            }
            updateMsgList(filteredMsgs);  // Update the list with the filtered users
        }else if (cur_option.equals("Members")){
            filteredMembers.clear();
            if (text.isEmpty() || text.equals("Search")) {
                filteredMembers.addAll(allMembers);  // Show all users if the search bar is empty
            } else {
                for (User mem: allMembers) {
                    if (mem.getName().toLowerCase().contains(text.toLowerCase())) {
                        filteredMembers.add(mem);
                    }
                }
            }
            updateMemberList(filteredMembers);  // Update the list with the filtered users
        }

    }


}
