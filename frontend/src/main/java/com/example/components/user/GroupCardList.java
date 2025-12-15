    package com.example.components.user;

    import com.example.dto.GroupCardDTO;
    import com.example.listener.GroupCardListener;
    import com.example.listener.GroupListener;

    import javax.swing.*;
    import java.util.List;

    public class GroupCardList extends JScrollPane {

        JPanel listPanel;
        List<GroupCardDTO> data;
        GroupCard selectedCard;

        public GroupCardList(List<GroupCardDTO> groups,
                             int width,
                             GroupCardListener listener) {

            this.data = groups;

            listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setOpaque(false);

            for (GroupCardDTO g : groups) {
                GroupCard card = new GroupCard(g, width, (c, group) -> {
                    select(c);
                    listener.onGroupSelected(c, group);
                });
                listPanel.add(card);
            }

            this.setViewportView(listPanel);
            this.setOpaque(false);
            this.getViewport().setOpaque(false);
        }

        private void select(GroupCard card) {
            if (selectedCard != null) {
                selectedCard.setSelected(false);
            }
            selectedCard = card;
            selectedCard.setSelected(true);
        }

        // helper method để xóa item khỏi UI
        public void removeCard(GroupCard card) {
            listPanel.remove(card);
            listPanel.revalidate();
            listPanel.repaint();
        }
    }