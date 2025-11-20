package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import components.FriendCard;
import components.FriendList;
import components.SearchBar;
import components.UserMenu;
import model.User;

public class UserUtilPanel extends JPanel {
    Border border = BorderFactory.createLineBorder(Color.black);
    int chat_cnt = 4;

    public UserUtilPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setBorder(border);
        this.setBackground(Color.GREEN);

        JPanel topContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topContainer.setPreferredSize(new Dimension(width, 100));
        topContainer.setOpaque(true);
        topContainer.setBackground(Color.YELLOW);
        topContainer.add(new UserMenu(20, 20));
        topContainer.add(new SearchBar(20, 7, width - 10, 30));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setPreferredSize(new Dimension(width - 10, height - 60));
        centerContainer.setOpaque(true);
        centerContainer.setBackground(Color.CYAN);

        List<User> demoUsers = List.of(
                new User(),
                new User(),
                new User()
        );

        FriendList friendList = new FriendList(demoUsers, width);
        JScrollPane scrollPane = new JScrollPane(friendList);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerContainer.add(scrollPane, BorderLayout.CENTER);

        this.add(topContainer, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);
    }
}
