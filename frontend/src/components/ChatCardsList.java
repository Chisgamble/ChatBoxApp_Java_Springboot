package components;

import javax.swing.*;
import java.awt.*;

public class ChatCardsList extends JPanel {

    private final JList<Object[]> chatList;

    /**
     * @param chatData each element is {senderInitials, name, message}
     * @param width    width of the JList / ChatCard
     * @param height   preferred height of each ChatCard
     */
    public ChatCardsList(Object[][] chatData, int width, int height) {
        this.setLayout(new BorderLayout());

        // Create the JList
        chatList = new JList<>(chatData);

        // Set custom renderer to use ChatCard
        chatList.setCellRenderer(new ListCellRenderer<Object[]>() {
            @Override
            public Component getListCellRendererComponent(
                    JList<? extends Object[]> list,
                    Object[] value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                // Create a ChatCard for each item
                ChatCard card = new ChatCard(
                        (String) value[0],  // sender initials
                        (String) value[1],  // name
                        (String) value[2],  // message
                        width,              // width of the ChatCard
                        height              // height of the ChatCard
                );

                // Handle selection highlight
                if (isSelected) {
                    card.setBackground(list.getSelectionBackground());
                    card.setOpaque(true);
                } else {
                    card.setBackground(list.getBackground());
                    card.setOpaque(false);
                }

                return card;
            }
        });

        // Allow variable row height
        chatList.setFixedCellHeight(-1);
        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(chatList);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public JList<Object[]> getChatList() {
        return chatList;
    }
}
