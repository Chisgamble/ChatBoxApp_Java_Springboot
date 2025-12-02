package panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import components.MyColor;
import components.RoundedComboBox;
import components.user.*;
import dto.FriendCardDTO;
import dto.InboxDTO;
import dto.response.RegisterResDTO;
import listener.SearchBarListener;
import listener.UserMenuListener;
import model.Msg;
import model.User;
import services.AuthService;
import ui.ChatScreen;
import ui.Login;
import ui.ProfilePopup;

public class UserUtilPanel extends JPanel implements UserMenuListener, SearchBarListener {
    ChatScreen mainFrame;
    JPanel topContainer;
    JPanel centerContainer;
    RoundedComboBox<String> comboBox;
    SearchBar sb;
    Component list = null;
    List<InboxDTO> inboxes;
    List<FriendCardDTO> friends;
    List<User> allUsers = new ArrayList<>( List.of(
            new User("Sammael"),
            new User("Chris"),
            new User("Doc"),
            new User("Fridge"),
            new User("Dante"),
            new User("Faust"),
            new User("Heathcliff"),
            new User("Ishmael"))
    );
    List<User> filteredUsers = new ArrayList<>();
    List<Msg> all_msgs = new ArrayList<>(List.of(
            new Msg("Sammael"),
            new Msg("Chris"),
            new Msg("Doc"),
            new Msg("Fridge"),
            new Msg("Dante"),
            new Msg("Faust"),
            new Msg("Heathcliff"),
            new Msg("Ishmael"))
    );
    String cur_option;

    public UserUtilPanel(ChatScreen mainFrame, int width, int height, List<InboxDTO> inboxes, List<FriendCardDTO> friends) {
        this.cur_option = "Friends";
        this.mainFrame = mainFrame;
        this.inboxes = inboxes;
        this.friends = friends;

        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        topContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topContainer.setPreferredSize(new Dimension(width, 100));
        topContainer.setOpaque(false);
        topContainer.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
//        topContainer.setBackground(Color.YELLOW);

        String[] options = {"Online", "Offline"};
        comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(new Font("Roboto", Font.PLAIN, 12));
        comboBox.setPreferredSize(new Dimension(80, 30));

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

        list = new FriendCardList(friends, width - 10);

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
        filteredUsers.clear();
        if (text.isEmpty() || text.equals("Search")) {
            filteredUsers.addAll(allUsers);  // Show all users if the search bar is empty
        } else {
            for (User user : allUsers) {
                if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredUsers.add(user);
                }
            }
        }
        updateList(filteredUsers);  // Update the list with the filtered users
    }

    @Override
    public void onMenuOptionSelected(String option) {
        cur_option = option;
        if(option.equals("Profile")) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ProfilePopup popup = new ProfilePopup(parentFrame);
            popup.setVisible(true); // blocks here until popup is closed
        }

        // Add cards based on the selected option
        updateList(allUsers);
    }

    void updateList(List<User> users){
        boolean showOnline = false;
        if (cur_option.equals("Friend request") ){
            list = new FriendRequestList(users, getWidth() - 15);
        }else if(cur_option.equals("Friends")){
            list = new FriendCardList(friends, getWidth() - 15);
            showOnline = true;
        }else if (cur_option.equals("SearchMsg")){
            list = new MsgCardList(all_msgs, getWidth() - 15);
        }else if (cur_option.equals("Groups")){
            //Group card is a msg card but groups will be passed instead of msg
            //since the card only display name and last msg
            list = new MsgCardList(all_msgs, getWidth() - 15);
        }else if (cur_option.equals("Find User")){
            list = new StrangerCardList(allUsers, getWidth() - 15);
        }else if (cur_option.equals("Logout")) {
            try {
                AuthService authService = new AuthService();
                authService.logout();
                mainFrame.onLogout();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        this.remove(centerContainer);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerContainer.removeAll();
        centerContainer.add(scrollPane, BorderLayout.CENTER);
        this.add(centerContainer, BorderLayout.CENTER);
        updateSearchArea(showOnline);
        this.revalidate();
        this.repaint();
    }

    void updateSearchArea(boolean showOnline){
        comboBox.setVisible(showOnline);
        sb.setPreferredSize(new Dimension(getWidth() - (showOnline ? 110 : 20), 30));
    }

}
