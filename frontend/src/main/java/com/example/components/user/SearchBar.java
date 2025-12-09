package com.example.components.user;

import com.example.components.RoundedTextField;
import com.example.listener.SearchBarListener;

import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SearchBar extends RoundedTextField implements FocusListener, DocumentListener {
    private SearchBarListener sbListener;

    public SearchBar(int radius, int pad, int width, int height, SearchBarListener sbListener) {
        super(radius, pad);
        this.sbListener = sbListener;
        this.setPreferredSize(new Dimension(width, height));
        this.setForeground(Color.GRAY);
        this.setText("Search");
        this.addFocusListener(this);
        this.getDocument().addDocumentListener(this);
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

    @Override
    public void insertUpdate (javax.swing.event.DocumentEvent e){
        notifySearchTextChanged();
    }

    @Override
    public void removeUpdate (javax.swing.event.DocumentEvent e){
        notifySearchTextChanged();
    }

    @Override
    public void changedUpdate (javax.swing.event.DocumentEvent e){
        notifySearchTextChanged();
    }

    private void notifySearchTextChanged () {
        if (sbListener != null) {
            sbListener.onSearchChange(SearchBar.this.getText());
        }
    }

}
