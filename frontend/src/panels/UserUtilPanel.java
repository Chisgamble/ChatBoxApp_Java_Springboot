package panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import components.*;
import listener.SearchBarListener;
import listener.UserMenuListener;
import model.User;

public class UserUtilPanel extends JPanel implements UserMenuListener, SearchBarListener {
    Border border = BorderFactory.createLineBorder(Color.black);
    JPanel list = new JPanel();
    List<User> allUsers = new ArrayList<>();
    List<User> filteredUsers = new ArrayList<>();
    String cur_option;

    public UserUtilPanel(int width, int height) {
        this.cur_option = "Inbox";
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setBorder(border);
        this.setOpaque(false);

        JPanel topContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topContainer.setPreferredSize(new Dimension(width, 100));
        topContainer.setOpaque(true);
        topContainer.setBackground(Color.YELLOW);
        topContainer.add(new UserMenu(20, 20, this));
        topContainer.add(new SearchBar(20, 7, width - 10, 30, this));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setPreferredSize(new Dimension(width - 10, height - 60));
        centerContainer.setOpaque(false);

        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] names = {"Sammael", "Chris", "Doc", "Fridge", "Clockhead"};
        for (int i = 0; i < 5; i++){
            User u = new User(names[i]);
            allUsers.add(u);
            addRequestCard(u, width);
        }

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerContainer.add(scrollPane, BorderLayout.CENTER);

        this.add(topContainer, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);
    }

    void addFriendCard(User user, int width){
        FriendCard card = new FriendCard(user, width-5);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        list.add(card);
    }

    void addRequestCard(User user, int width){
        FriendRequestCard card = new FriendRequestCard(user, width - 5);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        list.add(card);
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
        updateUserList(filteredUsers);  // Update the list with the filtered users
    }

    @Override
    public void onMenuOptionSelected(String option) {
        // Clear the current cards
        list.removeAll();

        // Add cards based on the selected option
        switch (option) {
            case "Profile":
                cur_option = "Profile";
                break;
            case "Inbox":
                for (int i = 0; i < 5; i++) {
                    addFriendCard(new User(), getWidth());
                }
                break;
            case "Friend request":
                // Add friend request cards
                for (int i = 0; i < 5; i++) {
                    addRequestCard(new User(), getWidth());
                }
                break;
//            case "Settings":
//                break;
//            case "Logout":
//                break;
            default:
                break;
        }

        // Revalidate and repaint the list to reflect changes
        list.revalidate();
        list.repaint();
    }

    // Add the users to the list (JPanel)
    private void updateUserList(List<User> users) {
        list.removeAll();  // Clear the list

        // Add filtered user cards
        if (cur_option.equals("Friend request")) {
            for (User user : users) {
                addRequestCard(user, getWidth());
            }
        }else{
            for (User user : users) {
                addFriendCard( user, getWidth());
            }
        }

        // Revalidate and repaint the list
        list.revalidate();
        list.repaint();
    }


}
