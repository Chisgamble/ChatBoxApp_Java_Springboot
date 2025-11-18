package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import components.SearchBar;
import components.UserMenu;

public class UserUtilPanel extends JPanel{
    Border border = BorderFactory.createLineBorder(Color.black);
    public UserUtilPanel(int width, int height){
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.setBorder(border);
        this.setBackground(Color.GREEN);

        JPanel topContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topContainer.setPreferredSize(new Dimension(width, 100));
        topContainer.setOpaque(true);
        topContainer.setBackground(Color.YELLOW);
        topContainer.add(new UserMenu(20,20));
        topContainer.add(new SearchBar(20, 7, width-10, 30 ));

        JPanel centerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        centerContainer.setPreferredSize(new Dimension(width, height - 60));
        centerContainer.setOpaque(true);
        centerContainer.setBackground(Color.CYAN);

        this.add(topContainer, BorderLayout.NORTH);
        this.add(centerContainer, BorderLayout.CENTER);
    }
}
