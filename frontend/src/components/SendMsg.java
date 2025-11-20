package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import components.RoundedTextArea;
import java.awt.event.ActionEvent;

public class SendMsg extends JPanel implements ActionListener {
    private final JButton sendButton = new JButton("send");
    private final RoundedTextArea textArea = new RoundedTextArea(10, 7);

    public SendMsg(int width, int height){
        this.setLayout(new FlowLayout( FlowLayout.LEFT, 5,5));
        this.setPreferredSize(new Dimension(width - 10, height ));

        sendButton.addActionListener(this);
        Dimension buttonSize = sendButton.getPreferredSize();

        textArea.setPreferredSize(new Dimension(width - buttonSize.width - 20, 30));
//        textArea.setMinimumSize(new Dimension(0, 5));
//        textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));

        this.add(textArea);
        this.add(sendButton);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == sendButton){
            String text = textArea.getText().trim();
            if (text.isEmpty()) return;

            System.out.println(textArea.getText());
            textArea.setText("");   // Clear field after sending
            textArea.requestFocus();

        }
    }
}
