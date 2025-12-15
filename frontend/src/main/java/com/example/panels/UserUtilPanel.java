package com.example.panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.example.components.MyColor;
import com.example.components.RoundedComboBox;
import com.example.components.user.*;
import com.example.dto.*;
import com.example.dto.response.FriendRequestResDTO;
import com.example.dto.response.StrangerCardResDTO;
import com.example.listener.FriendRequestListener;
import com.example.listener.SearchBarListener;
import com.example.listener.UserMenuListener;
import com.example.services.AuthService;
import com.example.services.FriendRequestService;
import com.example.services.FriendService;
import com.example.services.UserService;
import com.example.ui.ChatContext;
import com.example.ui.ChatScreen;
import com.example.ui.ProfilePopup;

public class UserUtilPanel extends JPanel implements UserMenuListener, SearchBarListener, FriendRequestListener {
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

    public  UserUtilPanel(ChatScreen mainFrame, int width, int height, UserMiniDTO user, List<FriendCardDTO> friends) {
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

        FriendCardList friendList = new FriendCardList(friends, width - 10, mainFrame);
        friendList.getList().addListSelectionListener(e -> {
            System.out.println("Event  ------------------------------");
            if (!e.getValueIsAdjusting()) {
                System.out.println(friendList.getList().getSelectedValue());
                FriendCardDTO selected =
                        friendList.getList().getSelectedValue();

                if (selected != null) {
                    System.out.println(selected.getFriendId());
                    mainFrame.openInbox(selected);
                    System.out.println(selected.getFriendId());
                }
            }
        });
        list = friendList;

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
                AuthService authService = new AuthService();
                authService.logout();
                mainFrame.onLogout();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
            return;
        }else if ("Profile".equals(option)) {
            ProfilePopup popup = new ProfilePopup(mainFrame);
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

    void applyFilters() {
        List<?> data;

        switch (cur_option) {
            case "Friends":
                friendsMaster = userService.getAllFriends(user.getId());
                data = filterFriends(friendsMaster);

                FriendCardList friendList =
                        new FriendCardList((List<FriendCardDTO>) data, getWidth() - 15, mainFrame);

                friendList.getList().addListSelectionListener(e -> {
                    System.out.println("Event  ------------------------------");
                    if (!e.getValueIsAdjusting()) {
                        System.out.println(friendList.getList().getSelectedValue());
                        FriendCardDTO selected =
                                friendList.getList().getSelectedValue();

                        if (selected != null) {
                            mainFrame.getContext().setTargetUser(selected);
                            mainFrame.getContext().setGroup(false);
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
                                    mainFrame.getContext().setTargetGroup(group); // ⭐
                                    mainFrame.getContext().setGroup(true);        // ⭐
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
                        getWidth() - 15
                ));
                break;

            default:
                break;
        }
        SwingUtilities.invokeLater(this::selectFriendFromContext);
    }


//    public void selectFirstFriend() {
//        if (list instanceof FriendCardList friendList) {
//            if (!friendList.getList().isSelectionEmpty()) return;
//            friendList.getList().setSelectedIndex(0);
//        }
//    }


//    void updateList(List<User> users){
//        boolean showOnline = false;
//        if (cur_option.equals("Friend request") ){
//            List<FriendRequestResDTO> friend_requests = userService.getAllFriendRequests(friend.getId());
//            list = new FriendRequestList(friend_requests, getWidth() - 15, this);
//        }else if(cur_option.equals("Friends")){
//            loadFriends();
//            list = new FriendCardList(friends, getWidth() - 15, mainFrame);
//            showOnline = true;
//        }else if (cur_option.equals("SearchMsg")){
//            list = new GlobalMsgCardList(all_msgs, getWidth() - 15);
//        }else if (cur_option.equals("Groups")){
//            //Group card is a msg card but groups will be passed instead of msg
//            //since the card only display name and last msg
//            list = new GlobalMsgCardList(all_msgs, getWidth() - 15);
//        }else if (cur_option.equals("Find User")){
////            list = new StrangerCardList(, getWidth() - 15);
//        }else if (cur_option.equals("Logout")) {
//            try {
//                AuthService authService = new AuthService();
//                authService.logout();
//                mainFrame.onLogout();
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//            }
//        }
//
//        this.remove(centerContainer);
//
//        JScrollPane scrollPane = new JScrollPane(list);
//        scrollPane.setBorder(null);
//        scrollPane.setOpaque(false);
//        scrollPane.getViewport().setOpaque(false);
//
//        centerContainer.removeAll();
//        centerContainer.add(scrollPane, BorderLayout.CENTER);
//        this.add(centerContainer, BorderLayout.CENTER);
//        updateSearchArea(showOnline);
//        this.revalidate();
//        this.repaint();
//    }

    void updateSearchArea(boolean showOnline){
        comboBox.setVisible(showOnline);
        sb.setPreferredSize(new Dimension(getWidth() - (showOnline ? 110 : 20), 30));
    }

//    @Override
//    public void onPromote(User friend) {
//        api.promoteMember(friend.getId(),
//                () -> JOptionPane.showMessageDialog(this, "Promoted " + friend.getName()),
//                () -> JOptionPane.showMessageDialog(this, "Failed to promote")
//        );
//    }
//
//    @Override
//    public void onRemove(User friend) {
//        api.removeMember(friend.getId(),
//                () -> JOptionPane.showMessageDialog(this, "Removed " + friend.getName()),
//                () -> JOptionPane.showMessageDialog(this, "Failed to remove")
//        );
//    }

    @Override
    public void onAccept(FriendRequestResDTO friend) {
        try {
            friendRequestService.updateFriendRequest(friend, this.user.getId(), "accepted");
            JOptionPane.showMessageDialog(this, "You are now friend with " + friend.getSenderUsername());
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Failed to accept\n" + e.getMessage());
        }
    }

    @Override
    public void onReject(FriendRequestResDTO friend) {
        try {
            friendRequestService.updateFriendRequest(friend, this.user.getId(), "rejected");
            JOptionPane.showMessageDialog(this, "You have rejected friend request from " + friend.getSenderUsername());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to reject\n" + e.getMessage());
        }
    }

    @Override
    public void onSent (Long receiverId){
        try {
            friendRequestService.create(user.getId(), receiverId);
            JOptionPane.showMessageDialog(this, "You have sent friend request");
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Failed to send friend request\n" + e.getMessage());
        }
    }

    private void loadFriends() {
        friendsMaster = userService.getAllFriends(user.getId());
    }

    private void loadFriendRequests() {
        List<FriendRequestResDTO> requests = friendRequestService.getAll(user.getId());
        FriendRequestList list = new FriendRequestList(requests, 400, this);
        add(list);
    }

//    private void loadGroupMembers() {
//        List<User> members = api.getGroupMembers();
//        MemberCardList list = new MemberCardList(members, 400, true, this);
//        add(list);
//    }

    public void selectFriendFromContext() {
        ChatContext ctx = mainFrame.getContext();
        if (ctx == null || ctx.getTargetUser() == null) return;

        String targetUsername = ctx.getTargetUser().getUsername();

        if (list instanceof FriendCardList friendList) {
            JList<FriendCardDTO> jList = friendList.getList();

            ListModel<FriendCardDTO> model = jList.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                FriendCardDTO dto = model.getElementAt(i);
                if (dto.getUsername().equals(targetUsername)) {
                    jList.setSelectedIndex(i);
                    jList.ensureIndexIsVisible(i);
                    break;
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

}
