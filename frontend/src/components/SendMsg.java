package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import components.RoundedTextField;
import java.awt.event.ActionEvent;

public class SendMsg extends JPanel implements ActionListener {
    private final JButton sendButton = new JButton("send");
    private final RoundedTextField textField = new RoundedTextField(10, 7);

    public SendMsg(int width, int height){
        this.setLayout(new FlowLayout( FlowLayout.LEFT, 5,5));
        this.setPreferredSize(new Dimension(width - 10, height ));

        sendButton.addActionListener(this);
        Dimension buttonSize = sendButton.getPreferredSize();

        textField.setPreferredSize(new Dimension(width - buttonSize.width - 20, 30));
//        textField.setMinimumSize(new Dimension(0, 5));
//        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));

        this.add(textField);
        this.add(sendButton);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == sendButton){
            String text = textField.getText().trim();
            if (text.isEmpty()) return;

            System.out.println(textField.getText());
            textField.setText("");   // Clear field after sending
            textField.requestFocus();

        }
    }
}
