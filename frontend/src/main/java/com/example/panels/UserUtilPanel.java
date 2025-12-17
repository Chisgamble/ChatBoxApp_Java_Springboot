package com.example.panels;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import com.example.components.MyColor;
import com.example.components.RoundedComboBox;
import com.example.components.user.*;
import com.example.dto.*;
import com.example.dto.response.FriendRequestResDTO;
import com.example.dto.response.StrangerCardResDTO;
import com.example.listener.FriendRequestListener;
import com.example.listener.ProfileListener;
import com.example.listener.SearchBarListener;
import com.example.listener.UserMenuListener;
import com.example.services.FriendRequestService;
import com.example.services.FriendService;
import com.example.services.UserService;
import com.example.ui.ChatContext;
import com.example.ui.ChatScreen;
import com.example.ui.ProfilePopup;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUtilPanel extends JPanel implements UserMenuListener,
        SearchBarListener,
        FriendRequestListener,
        ProfileListener {
    ChatScreen mainFrame;
    JPanel topContainer;
    JPanel centerContainer;
    RoundedComboBox<String> comboBox;
    SearchBar sb;
    Component list = null;

    UserService userService = new UserService();
    FriendService friendService = new FriendService();
    FriendRequestService friendRequestService = new FriendRequestService();

    UserMiniDTO user;
    List<InboxDTO> inboxes;
    List<FriendCardDTO> friendsMaster;
    List<GroupCardDTO> groupsMaster;
    List<FriendRequestResDTO> requestsMaster;
    List<StrangerCardResDTO> usersMaster;
    List<MsgDTO> msgMaster;

    List<?> filteredList;

    String searchText = "";
    boolean filterOnlineOnly = false; // comboBox

    String cur_option;

    public  UserUtilPanel(
            ChatScreen mainFrame,
            int width, int height,
            UserMiniDTO user,
            List<FriendCardDTO> friends) {
        this.cur_option = "Friends";
        this.mainFrame = mainFrame;
        this.inboxes = null;
        this.user = user;
        this.friendsMaster = friends;

        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        topContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topContainer.setPreferredSize(new Dimension(width, 100));
        topContainer.setOpaque(false);
        topContainer.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
//        topContainer.setBackground(Color.YELLOW);

        String[] options = {"Default", "Online"};
        comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(new Font("Roboto", Font.PLAIN, 12));
        comboBox.setPreferredSize(new Dimension(80, 30));
        comboBox.addActionListener(e -> {
            filterOnlineOnly = comboBox.getSelectedItem().equals("Online");
            applyFilters();
        });

        JPanel searchArea = new JPanel(new BorderLayout(10,5));
        searchArea.setOpaque(false);
        sb = new SearchBar(20, 7, width - 110, 30, this);
        searchArea.add(sb, BorderLayout.CENTER);
        searchArea.add(comboBox, BorderLayout.EAST);

        topContainer.add(new UserMenu(20, 20, this));
        topContainer.add(searchArea);

        centerContainer = new JPanel(new BorderLayout());
        centerContainer.setPreferredSize(new Dimension(width - 10, height - 60));
        centerContainer.setOpaque(false);

        if (friends != null){
            FriendCardList friendList = new FriendCardList(friends, width - 10, mainFrame);
            friendList.getList().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    FriendCardDTO selected =
                            friendList.getList().getSelectedValue();

                    if (selected != null) {
                        mainFrame.openInbox(selected);
                    }
                }
            });
            list = friendList;
        }


        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerContainer.add(scrollPane, BorderLayout.CENTER);

        this.add(topContainer, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);
    }

    @Override
    public void onSearchChange(String text) {
        System.out.println("change");
        this.searchText = text.toLowerCase();
        applyFilters();
    }

    @Override
    public void onMenuOptionSelected(String option) {
        cur_option = option;

        if (cur_option.equals("Logout")) {
            try {
                mainFrame.onLogout();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
            return;
        }else if ("Profile".equals(option)) {
            ProfilePopup popup = new ProfilePopup(mainFrame, mainFrame.getUser().getId(), this);
            popup.setVisible(true);
            return;
        }else if("SearchMsg".equals(option)) {
            try {
                if (msgMaster == null)
                    msgMaster = userService.getAllRelatedMessages();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to load messages: " + e.getMessage());
                msgMaster = List.of();
            }
        }
        applyFilters();
    }

    List<FriendCardDTO> filterFriends(List<FriendCardDTO> list) {
        return list.stream()
                .filter(f -> f.getUsername().toLowerCase().contains(searchText))
                .filter(f -> !filterOnlineOnly || f.getIsActive()) // online only
                .toList();
    }

    List<GroupCardDTO> filterGroups(List<GroupCardDTO> list) {
        return list.stream()
                .filter(f -> f.getGroupname().toLowerCase().contains(searchText))
                .toList();
    }

    List<FriendRequestResDTO> filterRequests(List<FriendRequestResDTO> list) {
        return list.stream()
                .filter(r -> r.getSenderUsername().toLowerCase().contains(searchText))
                .toList();
    }

    List<StrangerCardResDTO> filterUsers(List<StrangerCardResDTO> list) {
        return list.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(searchText))
                .toList();
    }

    List<MsgDTO> filterMsgs(List<MsgDTO> list) {
        return list.stream()
            .filter(m ->
                m.getContent() != null &&
                m.getContent().toLowerCase().contains(searchText)
            )
            .toList();
    }

    void renderList(Component comp) {
        centerContainer.removeAll();
        this.list = comp;

        JScrollPane scrollPane = new JScrollPane(comp);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerContainer.add(scrollPane, BorderLayout.CENTER);

        updateSearchArea(cur_option.equals("Friends"));
        revalidate();
        repaint();
    }

    public void applyFilters() {
        List<?> data;

        switch (cur_option) {
            case "Friends":
                friendsMaster = userService.getAllFriends(user.getId());
                data = filterFriends(friendsMaster);

                FriendCardList friendList =
                        new FriendCardList((List<FriendCardDTO>) data, getWidth() - 15, mainFrame);

                friendList.getList().addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        System.out.println(friendList.getList().getSelectedValue());
                        FriendCardDTO selected =
                                friendList.getList().getSelectedValue();

                        if (selected != null) {
                            mainFrame.openInbox(selected);
                        }
                    }
                });

                renderList(friendList);
                break;

            case "Groups":
                groupsMaster = userService.getAllGroups(user.getId());
                data = filterGroups(groupsMaster);

                GroupCardList groupList =
                        new GroupCardList((List<GroupCardDTO>) data,
                                getWidth() - 15,
                                (card, group) ->{
                                    mainFrame.openGroupChat(group);
                                });

                renderList(groupList);
                break;

            case "Friend request":
                requestsMaster = userService.getAllFriendRequests(user.getId());
                data = filterRequests(requestsMaster);
                renderList(new FriendRequestList((List<FriendRequestResDTO>) data, getWidth() - 15, this));
                break;

            case "Find User":
                try {
                    usersMaster = userService.getAllStrangerCards(user.getId());
                    data = filterUsers(usersMaster);
                    renderList(new StrangerCardList((List<StrangerCardResDTO>) data, getWidth() - 15, this));
                    break;
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "Failed to load all users");
                }

            case "SearchMsg":
                if (msgMaster == null) {
                    msgMaster = List.of();
                }

                data = filterMsgs(msgMaster);
                renderList(new GlobalMsgCardList(
                        (List<MsgDTO>) data,
                        getWidth() - 15,
                        mainFrame
                ));
                break;

            default:
                break;
        }
        SwingUtilities.invokeLater(this::selectChatFromContext);
    }

    void updateSearchArea(boolean showOnline){
        comboBox.setVisible(showOnline);
        sb.setPreferredSize(new Dimension(getWidth() - (showOnline ? 110 : 20), 30));
    }

    @Override
    public void onAccept(FriendRequestResDTO friend) {
        try {
            friendRequestService.updateFriendRequest(friend, this.user.getId(), "accepted");
            JOptionPane.showMessageDialog(mainFrame, "You are now friend with " + friend.getSenderUsername());
        }catch(Exception e){
            JOptionPane.showMessageDialog(mainFrame, "Failed to accept\n" + e.getMessage());
        }
    }

    @Override
    public void onReject(FriendRequestResDTO friend) {
        try {
            friendRequestService.updateFriendRequest(friend, this.user.getId(), "rejected");
            JOptionPane.showMessageDialog(mainFrame, "You have rejected friend request from " + friend.getSenderUsername());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Failed to reject\n" + e.getMessage());
        }
    }

    @Override
    public void onSent (Long receiverId){
        try {
            friendRequestService.create(user.getId(), receiverId);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Failed to send friend request\n" + e.getMessage());
        }
    }

    @Override
    public void onProfileUpdated(UserDTO updatedUser) {
        UserMiniDTO user = new UserMiniDTO(
                updatedUser.id(),
                updatedUser.email(),
                updatedUser.username(),
                updatedUser.role());
        this.user = user;
        mainFrame.setUser(user);
        mainFrame.getCurrentChat().setThisUser(user);

        this.revalidate();
        this.repaint();
    }

    public void switchTab(String option) {
        this.cur_option = option;
        this.searchText = "";
        if (sb != null) sb.setText("");

        applyFilters(); //gọi renderList và selectChatFromContext
    }

    public void selectChatFromContext() {
        ChatContext ctx = mainFrame.getCurrentChat();
        if (ctx == null) return;

        if (ctx.isGroup() && ctx.getTargetGroup() != null) {
            if (this.list instanceof GroupCardList groupCardList) {
                groupCardList.selectAndScrollToGroup(ctx.getTargetGroup().getId());
            }
        }
        else if (!ctx.isGroup() && ctx.getTargetUser() != null) {
            if (list instanceof FriendCardList friendList) {
                JList<FriendCardDTO> jList = friendList.getList();
                ListModel<FriendCardDTO> model = jList.getModel();
                Long targetId = ctx.getTargetUser().getInboxId();

                for (int i = 0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).getInboxId().equals(targetId)) {
                        jList.setSelectedIndex(i);
                        jList.ensureIndexIsVisible(i);
                        break;
                    }
                }
            }
        }
    }

    public void removeFriend(Long userId) {
        if (friendsMaster == null) return;

        System.out.println(userId + " - ");
        for (FriendCardDTO fr : friendsMaster){
            System.out.print(fr.getFriendId() + " - ");
        }
        System.out.println("Before remove: " + friendsMaster.size());
        friendsMaster.removeIf(f ->  f.getFriendId().equals(userId));
        System.out.println("After remove: " + friendsMaster.size());

        // nếu đang ở tab Friends → render lại
        if ("Friends".equals(cur_option)) {
            applyFilters();
        }
        System.out.println(friendsMaster.size());
    }

    public void removeGroup(Long groupId) {
        if (groupsMaster == null) return;

        groupsMaster.removeIf(g -> g.getId().equals(groupId));

        if ("Groups".equals(cur_option)) {
            applyFilters();
        }
    }

    public void updateGroupName(Long groupId, String newName) {

        if (groupsMaster == null) return;

        for (GroupCardDTO g : groupsMaster) {
            if (g.getId().equals(groupId)) {
                g.setGroupname(newName);
                break;
            }
        }

        if ("Groups".equals(cur_option)) {
            applyFilters();

            SwingUtilities.invokeLater(() -> {
                if (list instanceof GroupCardList gList) {
                    gList.selectGroupById(groupId);
                }
            });
        }
    }


}
