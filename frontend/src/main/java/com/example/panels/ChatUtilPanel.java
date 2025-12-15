package com.example.panels;

import com.example.components.Avatar;
import com.example.components.ConfirmPopup;
import com.example.components.user.*;
import com.example.dto.*;
import com.example.dto.response.StrangerCardResDTO;
import com.example.listener.SearchBarListener;

import java.awt.*;

import javax.swing.*;

import com.example.services.InboxService;
import com.example.services.UserService;
import com.example.util.Utility;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.example.ui.ChatScreen;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChatUtilPanel extends JPanel implements SearchBarListener {
    ChatScreen mainFrame;

    JPanel topContainer = new JPanel();
    JPanel centerContainer = new JPanel();
    Component listContainer = null;

    InboxService inboxService = new InboxService();

    UserMiniDTO friend;
    List<InboxMsgDTO> inboxMsgs = new ArrayList<>();
    List<GroupMsgDTO> groupMsgs = new ArrayList<>();
    List<BaseMsgDTO> filteredMsgs = new ArrayList<>();

    List<GroupMemberDTO> allMembers = new ArrayList<>();
    List<GroupMemberDTO> filteredMembers = new ArrayList<>();
    MsgCardList msgList;

    UserService userService = new UserService();
    
    boolean isGroup;
    boolean isAdmin;
    boolean itemSelected = false;
    String cur_option = "Selection";
    String[] inboxOptions = {"Search In Chat", "Create Group With", "Delete All Chat History", "Unfriend", "Report Spam", "Block"};
    String[] groupOptions = {"Search In Chat", "Members", "Leave Group"};
    String[] groupAdminOptions = {"Search In Chat", "Members", "Add New Members", "Change Group Name", "Encrypt Group","Delete All Chat History", "Delete Group"};

    public ChatUtilPanel(ChatScreen mainFrame, int width, int height, UserMiniDTO friend) {
        this.mainFrame = mainFrame;
        if (friend == null){
            this.friend = new UserMiniDTO();
            this.friend.setUsername("");
            this.friend.setInitials("");
        }else {
            this.friend = friend;
        }
        
        this.isGroup = mainFrame.getContext().isGroup();
        this.isAdmin = mainFrame.getContext().isAdmin();

        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(width, height));
//        this.setBorder(border);

        topContainer = setupAvatarWrapper();

        centerContainer.setLayout(new BorderLayout());
        centerContainer.setPreferredSize(new Dimension(width - 25, height - 60));
        centerContainer.setOpaque(false);

        listContainer = setupOption();
        centerContainer.add(listContainer, BorderLayout.CENTER);

        this.add(topContainer, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);

    }

    void updatePanel(){
        this.removeAll();
        centerContainer.removeAll();
        topContainer.removeAll();

        topContainer = setupAvatarWrapper();
        centerContainer.add(setupOption(), BorderLayout.CENTER);

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
                    System.out.println("Delete friend");
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

        Avatar avatar = new Avatar(friend.getInitials(), 100);
        JLabel name = new JLabel(friend.getUsername());
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

            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setOpaque(true);
                    button.setBackground(new Color(230,230,230));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setOpaque(false);
                    button.setBackground(null);
                }
            });

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

    public void showUser(UserMiniDTO user) {
        this.friend = user;
        this.isGroup = false;
        updatePanel();
    }

    public void showGroup(GroupCardDTO group, String currentUserRole) {
        this.friend.setUsername(group.getGroupname());
        this.friend.setInitials(Utility.getInitials(group.getGroupname()));

        this.isAdmin = currentUserRole.equals("admin");
        this.isGroup = true;
        updatePanel();
    }

    private void onOptionButtonClick(String option) {
        // Update the current selected option
        cur_option = option;

        String[] needConfirm = {"Delete All Chat History", "Unfriend", "Report Spam", "Block",
                                "Leave Group", "Delete All Chat History", "Delete Group"};
        String[] notification = {"delete all chat history", "unfriend this friend", "report this friend for spamming", "block this friend",
                                 "leave this group", "delete all chat history", "delete this group"};
        if (Arrays.asList(needConfirm).contains(option)){
            boolean confirmed = ConfirmPopup.show(
                    mainFrame,
                    notification[Arrays.asList(needConfirm).indexOf(option)]
            );

            if (confirmed) {
                mainFrame.handleConfirmedOption(option);
            }

            cur_option = "Selection";
            updatePanel();
            return;
        }else if (option.equals("Search In Chat")){
            enterSearchMode();
            return;
        }else if (option.equals("Change Group Name")){
            if (ChangeGroupNamePopup.show(mainFrame)){
                System.out.println("Change group name");
            }
            cur_option = "Selection";
        }else if (option.equals("Create Group With")){

            CreateGroupPopup.show(
                    mainFrame,
                    mainFrame.getFriends(),
                    mainFrame   // callback
            );
            cur_option = "Selection";
        }else if (option.equals("Add New Members")){
            try {
                List<StrangerCardResDTO> users = userService.getAllStrangerCards(mainFrame.getContext().getTargetUser().getId());

                AddMemberPopup.show(
                        mainFrame,
                        users,
                        mainFrame   // callback
                );
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Failed to load all users");
            }

            cur_option = "Selection";
        }else if (option.equals("Members")){
            System.out.println("test members");
            for (GroupMemberDTO m : allMembers){
                System.out.println(m.getUsername());
            }
            enterMembersMode();
            return;
        }

        updatePanel();
    }

    public void setInboxMessages(List<InboxMsgDTO> messages) {
        this.inboxMsgs = messages;
        this.isGroup = false;
        this.filteredMsgs.clear();
        this.filteredMsgs.addAll(messages);
    }

    public void setGroupMessages(List<GroupMsgDTO> messages) {
        this.groupMsgs = messages;
        this.isGroup = true;
        this.filteredMsgs.clear();
        this.filteredMsgs.addAll(messages);
    }

    void updateMsgList(List<? extends BaseMsgDTO> msgs){
        centerContainer.removeAll();

        listContainer = new MsgCardList(msgs, getWidth() - 25);
        centerContainer.add(listContainer, BorderLayout.CENTER);

        centerContainer.revalidate();
        centerContainer.repaint();
    }

    void updateMemberList(List<GroupMemberDTO> members){
        centerContainer.removeAll();

        listContainer = new MemberCardList(
                members,
                getWidth() - 15,
                mainFrame.getContext().isAdmin()
        );

        centerContainer.add(listContainer, BorderLayout.CENTER);

        centerContainer.revalidate();
        centerContainer.repaint();
    }

    void enterMembersMode(){
        cur_option = "Members";

        centerContainer.removeAll();

        filteredMembers.clear();
        filteredMembers.addAll(allMembers);

        listContainer = new MemberCardList(
                filteredMembers,
                getWidth() - 15,
                mainFrame.getContext().isAdmin()
        );

        updateSearchHeader();

        centerContainer.add(listContainer, BorderLayout.CENTER);

        centerContainer.revalidate();
        centerContainer.repaint();
    }

    void enterSearchMode() {
        cur_option = "Search In Chat";

        centerContainer.removeAll();

        msgList = new MsgCardList(filteredMsgs, getWidth() - 25);
        JList<?> list = msgList.getList();

        list.addListSelectionListener(e -> {
            itemSelected = list.getSelectedIndices().length > 0;
            updateSearchHeader();
        });

        updateSearchHeader();
        centerContainer.add(msgList, BorderLayout.CENTER);

        centerContainer.revalidate();
        centerContainer.repaint();
    }

    void updateSearchHeader() {
        topContainer.removeAll();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
        topContainer.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel back = new JLabel(new FlatSVGIcon("assets/arrow-left-solid-full.svg", 24, 24));
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cur_option = "Selection";
                updatePanel();
            }
        });

        SearchBar sb = new SearchBar(20, 5, getWidth() - 100, 30, this);

        JLabel trash = new JLabel(new FlatSVGIcon("assets/trash-solid-full.svg", 24, 24));
        trash.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        trash.setVisible(itemSelected);

        trash.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ConfirmPopup.show(mainFrame, "delete selected messages")) {
                    deleteSelectedMessages();
                }
            }
        });

        topContainer.add(back);
        topContainer.add(Box.createHorizontalStrut(10));
        topContainer.add(sb);
        topContainer.add(Box.createHorizontalStrut(10));
        topContainer.add(trash);

        topContainer.revalidate();
        topContainer.repaint();
    }

    void deleteSelectedMessages() {
        try{
            JList<BaseMsgDTO> list = msgList.getList();
            List<BaseMsgDTO> toRemove = list.getSelectedValuesList();

            filteredMsgs.removeIf(toRemove::contains);
            msgList.updateList(filteredMsgs);

            List<Long> ids = toRemove.stream()
                    .map(BaseMsgDTO::getId)
                    .toList();

            if (isGroup) {
//                inboxService.deleteGroupMessages(ids);
            } else {
                inboxService.deleteMessagesBySender(mainFrame.getCurrentChat().getInboxId(), ids);
            }

            itemSelected = false;
            updateSearchHeader();
            updateMsgList(filteredMsgs);

            mainFrame.reloadCurrentInbox();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Failed to delete messages\n" + ex.getMessage());
        }

    }


    public List<?> searchMessages(String keyword) {
        if (isGroup && groupMsgs != null) {
            return groupMsgs.stream()
                    .filter(m -> m.getContent().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        } else if (!isGroup && inboxMsgs != null) {
            return inboxMsgs.stream()
                    .filter(m -> m.getContent().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }
        return List.of();
    }

    @Override
    public void onSearchChange(String text) {
        if (cur_option.equals("Search In Chat")) {
            filteredMsgs.clear();

            System.out.println("Searching for: '" + text + "'");

            if (text.isEmpty() || text.equals("Search")) {
                if (isGroup) {
                    filteredMsgs.addAll(groupMsgs);
                    System.out.println(filteredMsgs);
                } else {
                    filteredMsgs.addAll(inboxMsgs);
                }
            } else {
                if (isGroup) {
                    for (GroupMsgDTO msg : groupMsgs) {
                        System.out.println("Checking group message: " + msg.getContent());
                        if (msg.getContent().toLowerCase().contains(text.toLowerCase())) {
                            filteredMsgs.add(msg);
                        }
                    }
                } else {
                    for (InboxMsgDTO msg : inboxMsgs) {
                        System.out.println("Checking inbox message: " + msg.getContent());
                        if (msg.getContent().toLowerCase().contains(text.toLowerCase())) {
                            filteredMsgs.add(msg);
                        }
                    }
                }
            }
            updateMsgList(filteredMsgs);  // Update the list with the filtered users
        }else if (cur_option.equals("Members")){
            filteredMembers.clear();
            if (text.isEmpty() || text.equals("Search")) {
                filteredMembers.addAll(allMembers);  // Show all users if the search bar is empty
            } else {
                for (GroupMemberDTO mem: allMembers) {
                    if (mem.getUsername().toLowerCase().contains(text.toLowerCase())) {
                        filteredMembers.add(mem);
                    }
                }
            }

            updateMemberList(filteredMembers);
        }
    }

    public void setAllMembers(List<GroupMemberDTO> allMembers) {
        this.allMembers = allMembers;
    }

    public void clear() {
        this.friend = new UserMiniDTO(); // empty friend
        this.friend.setUsername("");
        this.friend.setInitials("");

        this.inboxMsgs.clear();
        this.groupMsgs.clear();
        this.filteredMsgs.clear();

        this.cur_option = "Selection";

        centerContainer.removeAll();
        topContainer.removeAll();

        revalidate();
        repaint();
    }

}
