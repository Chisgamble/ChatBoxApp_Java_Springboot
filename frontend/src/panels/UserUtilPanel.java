package panels;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import components.*;
import listener.UserMenuListener;
import model.User;

public class UserUtilPanel extends JPanel implements UserMenuListener {
    Border border = BorderFactory.createLineBorder(Color.black);
    JPanel list = new JPanel();
    int chat_cnt = 4;

    public UserUtilPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setBorder(border);
        this.setOpaque(false);

        JPanel topContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topContainer.setPreferredSize(new Dimension(width, 100));
        topContainer.setOpaque(true);
        topContainer.setBackground(Color.YELLOW);
        topContainer.add(new UserMenu(20, 20, this));
        topContainer.add(new SearchBar(20, 7, width - 10, 30));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setPreferredSize(new Dimension(width - 10, height - 60));
        centerContainer.setOpaque(false);

//        List<User> demoUsers = List.of(
//                new User(),
//                new User(),
//                new User()
//        );

//        FriendList friendList = new FriendList(demoUsers, width);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < 5; i++){
            addRequestCard(new User(), width);
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
    public void onMenuOptionSelected(String option) {
        // Clear the current cards
        list.removeAll();

        // Add cards based on the selected option
        switch (option) {
            case "Profile":
                // Add profile-related cards (this could be a different type of card)
                // Example: No cards for profile, maybe a user profile display
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
            case "Settings":
                // Add settings-related cards, if needed
                // Example: SettingsCard
                break;
            case "Logout":
                // Handle logout behavior
                break;
            default:
                // Default action if no option matches
                break;
        }

        // Revalidate and repaint the list to reflect changes
        list.revalidate();
        list.repaint();
    }
}
