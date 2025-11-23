package panels;

import components.Avatar;
import components.ConfirmPopup;
import components.user.*;
import listener.SearchBarListener;
import model.Msg;
import model.User;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import ui.ChatScreen;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChatUtilPanel extends JPanel implements SearchBarListener {
    ChatScreen mainFrame;

    Border border = BorderFactory.createLineBorder(Color.black);
    JPanel topContainer = new JPanel();
    JPanel centerContainer = new JPanel();
    Component listContainer = null;
    
    User user;
    List<Msg> msgs = new ArrayList<>();
    List<Msg> filteredMsgs = new ArrayList<>();
    List<User> allMembers = new ArrayList<>( List.of(
            new User("Sammael"),
            new User("Chris"),
            new User("Doc"),
            new User("Fridge"),
            new User("Dante"),
            new User("Faust"),
            new User("Heathcliff"),
            new User("Ishmael"))
    );
    List<User> filteredMembers = new ArrayList<>();
    
    boolean isGroup;
    boolean isAdmin;
    boolean itemSelected;
    String cur_option = "Selection";
    String[] inboxOptions = {"Search In Chat", "Create Group With", "Delete All Chat History", "Unfriend", "Report Spam", "Block"};
    String[] groupOptions = {"Search In Chat", "Members", "Leave Group"};
    String[] groupAdminOptions = {"Search In Chat", "Members", "Add New Members", "Change Group Name", "Encrypt Group","Delete All Chat History", "Delete Group"};

    public ChatUtilPanel(ChatScreen mainFrame, int width, int height, boolean isGroup, boolean isAdmin) {
        this.mainFrame = mainFrame;

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

        topContainer = setupAvatarWrapper();

        centerContainer.setLayout(new BorderLayout());
        centerContainer.setPreferredSize(new Dimension(width - 25, height - 60));
        centerContainer.setOpaque(false);

        if (cur_option.equals("Selection")) {
            listContainer = setupOption();
            centerContainer.add(listContainer, BorderLayout.CENTER);
        } else if (cur_option.equals("Search In Chat")) {
            listContainer = new MsgCardList(msgs, width - 25);
            centerContainer.add(listContainer, BorderLayout.CENTER);
        }

        this.add(topContainer, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);

    }

    void updatePanel(){
        this.removeAll();
        centerContainer.removeAll();
        topContainer.removeAll();

        if (cur_option.equals("Selection")){
            topContainer = setupAvatarWrapper();
            centerContainer.add(setupOption(), BorderLayout.CENTER);
        }else if (cur_option.equals("Search In Chat")){
            itemSelected = false;
            MsgCardList msgList = new MsgCardList(msgs,getWidth() - 25);
            JList<Msg> list = msgList.getList();
            list.addListSelectionListener(e -> {
                itemSelected = list.getSelectedIndices().length > 0;
                updatePanel();
            });
            setupSearchArea(itemSelected);
            centerContainer.add(msgList, BorderLayout.CENTER);
        }else if (cur_option.equals("Members")){
            centerContainer.add(new MemberCardList(allMembers, getWidth() - 25, isAdmin));
            setupSearchArea(itemSelected);
        }

        this.add(topContainer, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    void setupSearchArea(boolean itemSelected){
        topContainer.removeAll();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        topContainer.setBorder(BorderFactory.createEmptyBorder(20,5,20,5));
        SearchBar sb = new SearchBar(20,5, getWidth() - 100, 30, this);
        JLabel label = new JLabel(new FlatSVGIcon("assets/arrow-left-solid-full.svg", 24, 24));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cur_option = "Selection";
                updatePanel();
            }
        });

        JLabel deleteBtn = new JLabel(new FlatSVGIcon("assets/trash-solid-full.svg", 24, 24));
        deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ConfirmPopup.show(mainFrame, "deleting the message(s)")) {
                    System.out.println("Delete user");
                    updatePanel();
                }
            }
        });
        deleteBtn.setVisible(itemSelected);

        topContainer.add(Box.createVerticalStrut(20));
        topContainer.add(label);
        topContainer.add(Box.createHorizontalStrut(10));
        topContainer.add(sb);
        topContainer.add(Box.createHorizontalStrut(10));
        topContainer.add(deleteBtn);
        topContainer.add(Box.createVerticalStrut(20));
    }

    JPanel setupAvatarWrapper(){
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
        return avatarWrapper;
    }

    JPanel setupOption() {
        JPanel optionWrapper = new JPanel();
        optionWrapper.setLayout(new BoxLayout(optionWrapper, BoxLayout.Y_AXIS));
        optionWrapper.setOpaque(false);
        optionWrapper.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        String[] options;
        if (!isGroup) {
            options = inboxOptions;
        } else if (isGroup && isAdmin) {
            options = groupAdminOptions;
        } else {
            options = groupOptions;
        }

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

        String[] needConfirm = {"Delete All Chat History", "Unfriend", "Report Spam", "Block",
                                "Leave Group", "Delete All Chat History", "Delete Group"};
        String[] notification = {"delete all chat history", "unfriend this user", "report this user for spamming", "block this user",
                                 "leave this group", "delete all chat history", "delete this group"};
        if (Arrays.asList(needConfirm).contains(option)){
            if (ConfirmPopup.show(mainFrame, notification[Arrays.asList(needConfirm).indexOf(option)])) {
                System.out.println("Perform action");
            }
            cur_option = "Selection";
        }else if (option.equals("Change Group Name")){
            if (ChangeGroupNamePopup.show(mainFrame)){
                System.out.println("Change group name");
            }
            cur_option = "Selection";
        }else if (option.equals("Create Group With")){
            List<User> allFriends = allMembers;

            CreateGroupPopup.show(
                    mainFrame,
                    allFriends,
                    mainFrame   // callback
            );
            cur_option = "Selection";
        }else if (option.equals("Add New Members")){
            List<User> allFriends = allMembers;
            //TODO: remove already existed members from allFriends here
            AddMemberPopup.show(
                    mainFrame,
                    allFriends,
                    mainFrame   // callback
            );
            cur_option = "Selection";
        }

        updatePanel();
    }

    void updateMsgList(List<Msg> msgs){
        listContainer = new MsgCardList(msgs, getWidth()-25);
        listContainer.revalidate();
        listContainer.repaint();
    }

    void updateMemberList(List<User> members){
        this.remove(listContainer);
        listContainer = new FriendCardList(members, getWidth()-15);
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
