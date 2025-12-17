package com.example.panels;

import com.example.components.Avatar;
import com.example.components.ConfirmPopup;
import com.example.components.user.*;
import com.example.dto.*;
import com.example.dto.response.InboxUserResDTO;
import com.example.dto.response.StrangerCardResDTO;
import com.example.listener.SearchBarListener;

import java.awt.*;

import javax.swing.*;

import com.example.services.GroupService;
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
    SearchBar searchBar;

    InboxService inboxService = new InboxService();

    UserMiniDTO friend;
    List<InboxMsgDTO> inboxMsgs = new ArrayList<>();
    List<GroupMsgDTO> groupMsgs = new ArrayList<>();
    List<BaseMsgDTO> filteredMsgs = new ArrayList<>();

    List<GroupMemberDTO> allMembers = new ArrayList<>();
    List<GroupMemberDTO> filteredMembers = new ArrayList<>();
    MsgCardList msgList;

    UserService userService = new UserService();
    GroupService groupService = new GroupService();

    boolean isGroup = false;
    boolean isAdmin = false;
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

    public void updatePanel(){
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
            if (ChangeGroupNamePopup.show(mainFrame, mainFrame)){
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
                Long groupId = mainFrame.getCurrentChat().getGroupId();
                Long currentUserId = mainFrame.getCurrentChat().getThisUser().getId();
                List<AddMemberCardDTO> users = groupService.getAllFriendsNotInGroup(groupId, currentUserId);

                AddMemberPopup.show(
                        mainFrame,
                        groupId,
                        users,
                        mainFrame   // callback
                );
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Failed to load all users: " + ex.getMessage());
                System.out.println(ex.getMessage());
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

    void updateMemberList(List<GroupMemberDTO> members){
        centerContainer.removeAll();

        listContainer = new MemberCardList(
                members,
                getWidth() - 15,
                isAdmin,
                mainFrame
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
                isAdmin,
                mainFrame
        );

        updateSearchHeader();

        centerContainer.add(listContainer, BorderLayout.CENTER);

        centerContainer.revalidate();
        centerContainer.repaint();
    }

    void enterSearchMode() {
        cur_option = "Search In Chat";
        centerContainer.removeAll();
        itemSelected = false;

        reloadMessages();

        centerContainer.removeAll();

        msgList = new MsgCardList(filteredMsgs, getWidth() - 25);
        JList<BaseMsgDTO> list = msgList.getList();

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = list.locationToIndex(e.getPoint());
                    list.setSelectedIndex(index);

                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem jumpItem = new JMenuItem("Jump to");

                    jumpItem.addActionListener(event -> {
                        BaseMsgDTO selectedMsg = list.getSelectedValue();
                        if (selectedMsg != null) {
                            // Gọi mainFrame để điều hướng tới tin nhắn
                            mainFrame.jumpToMessage(selectedMsg.getId());
                        }
                    });

                    popup.add(jumpItem);
                    popup.show(list, e.getX(), e.getY());
                }
            }
        });

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

        if (searchBar == null) {
            searchBar = new SearchBar(20, 5, getWidth() - 100, 30, this);
        }

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
        topContainer.add(searchBar);
        topContainer.add(Box.createHorizontalStrut(10));
        topContainer.add(trash);

        topContainer.revalidate();
        topContainer.repaint();
    }

    void deleteSelectedMessages() {
        try {
            JList<BaseMsgDTO> list = msgList.getList();
            List<BaseMsgDTO> selected = list.getSelectedValuesList();
            Long currentUserId = mainFrame.getCurrentChat().getThisUser().getId();

            List<BaseMsgDTO> deletable = selected.stream()
                    .filter(m -> m.getSenderId().equals(currentUserId))
                    .toList();

            filteredMsgs.removeIf(deletable::contains);
            msgList.updateList(filteredMsgs);

            List<Long> ids = deletable.stream()
                    .map(BaseMsgDTO::getId)
                    .toList();

            if (isGroup) {
                groupService.deleteMessagesBySender(mainFrame.getCurrentChat().getGroupId(), ids);
            } else {
                inboxService.deleteMessagesBySender(mainFrame.getCurrentChat().getInboxId(), ids);
            }

            itemSelected = false;
//            updateMsgList(filteredMsgs);
            updateSearchHeader();

            mainFrame.reloadCurrentInbox();
            mainFrame.reloadCurrentGroupChat();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to delete messages\n" + ex.getMessage());
        }
    }

    void reloadMessages(){
        if (mainFrame.getCurrentChat().isGroup()){
            this.setGroupMessages(groupService.getInfoAndMsgs(
                    mainFrame.getCurrentChat().getGroupId()).getMsgs());
        }else{
            this.setInboxMessages(inboxService.getInboxWithMessages(
                    mainFrame.getCurrentChat().getInboxId()).getMsgs()
            );
        }
    }

    public void removeMember(Long userId) {
        allMembers.removeIf(m -> m.getUserId().equals(userId));
        enterMembersMode();
    }

    public void updateMemberRole(Long userId, String newRole) {
        for (GroupMemberDTO m : allMembers) {
            if (m.getUserId().equals(userId)) {
                m.setRole(newRole);
                break;
            }
        }
        enterMembersMode();
    }


    @Override
    public void onSearchChange(String text) {
        if (cur_option.equals("Search In Chat")) {
            filteredMsgs.clear();

            if (text.isEmpty() || text.equals("Search")) {
                if (isGroup) {
                    filteredMsgs.addAll(groupMsgs);
                } else {
                    filteredMsgs.addAll(inboxMsgs);
                }
            } else {
                if (isGroup) {
                    for (GroupMsgDTO msg : groupMsgs) {
                        if (msg.getContent().toLowerCase().contains(text.toLowerCase())) {
                            filteredMsgs.add(msg);
                        }
                    }
                } else {
                    for (InboxMsgDTO msg : inboxMsgs) {
                        if (msg.getContent().toLowerCase().contains(text.toLowerCase())) {
                            filteredMsgs.add(msg);
                        }
                    }
                }
            }
            itemSelected = false;
            msgList.updateList(filteredMsgs);
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
