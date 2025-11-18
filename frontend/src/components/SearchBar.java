package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SearchBar extends RoundedTextField implements FocusListener{
    public SearchBar(int radius,int pad, int width, int height){
        super(radius, pad);
        this.setPreferredSize(new Dimension(width,height));
        this.setForeground(Color.GRAY);
        this.setText("Search");
//        this.setBorder(BorderFactory.createLineBorder(Color.RED, 1));

        this.addFocusListener(this);

    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().equals("Search")) {
            this.setText("");
            this.setForeground(Color.BLACK);
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()) {
            this.setForeground(Color.GRAY);
            this.setText("Search");
        }
    }
}
