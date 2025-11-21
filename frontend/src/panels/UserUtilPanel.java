package panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import components.MyColor;
import components.admin.RoundedComboBox;
import components.user.FriendRequestList;
import components.user.SearchBar;
import components.user.UserMenu;
import listener.SearchBarListener;
import listener.UserMenuListener;
import model.User;
import components.user.FriendCardList;
import ui.ProfilePopup;

public class UserUtilPanel extends JPanel implements UserMenuListener, SearchBarListener {
    Border border = BorderFactory.createLineBorder(Color.black);
    JPanel centerContainer;
    Component list = null;
    List<User> allUsers = new ArrayList<>( List.of(
            new User("Sammael"),
            new User("Chris"),
            new User("Doc"),
            new User("Fridge"),
            new User("Clockhead"))
    );
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

        String[] options = {"Online", "Offline"};
        RoundedComboBox<String> comboBox = new RoundedComboBox<>(options);
        comboBox.setForeground(MyColor.DARK_GRAY);
        comboBox.setFont(new Font("Roboto", Font.PLAIN, 12));
        comboBox.setPreferredSize(new Dimension(80, 30));

        JPanel searchArea = new JPanel(new BorderLayout(10,5));
        searchArea.setOpaque(false);
        searchArea.add(new SearchBar(20, 7, width - 110, 30, this), BorderLayout.CENTER);
        searchArea.add(comboBox, BorderLayout.EAST);

        topContainer.add(new UserMenu(20, 20, this));
        topContainer.add(searchArea);


        centerContainer = new JPanel(new BorderLayout());
        centerContainer.setPreferredSize(new Dimension(width - 10, height - 60));
        centerContainer.setOpaque(false);

        list = new FriendCardList(allUsers, width - 10);

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
        if (cur_option.equals("Friend request") ){
            list = new FriendRequestList(users, getWidth() - 15);
            System.out.print("1");
        }else if(cur_option.equals("Inbox")){
            list = new FriendCardList(users, getWidth() - 15);
            System.out.print("2");
        }

        this.remove(centerContainer);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerContainer.removeAll();
        centerContainer.add(scrollPane, BorderLayout.CENTER);
        this.add(centerContainer, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

}
