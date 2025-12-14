package com.example.components.admin;

import com.example.components.MyColor;
import com.example.components.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

/**
 * Interactive filter input field with placeholder text that disappears on focus
 */
public class FilterButton extends RoundedTextField {
    private String placeholderText;
    private boolean isShowingPlaceholder = true;
    private Color placeholderColor = MyColor.DARK_GRAY;
    private Color textColor = MyColor.LIGHT_BLACK;

    public FilterButton(String text) {
        super(30);
        this.placeholderText = text;

        setText(text);
        setForeground(placeholderColor);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        setupFocusListener();
    }

    public FilterButton(String text, int radius) {
        super(radius);
        this.placeholderText = text;

        setText(text);
        setForeground(placeholderColor);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        setupFocusListener();
    }

    private void setupFocusListener() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isShowingPlaceholder) {
                    setText("");
                    setForeground(textColor);
                    isShowingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String currentText = getText().trim();
                if (currentText.isEmpty()) {
                    setText(placeholderText);
                    setForeground(placeholderColor);
                    isShowingPlaceholder = true;
                } else {
                    // Keep the saved text displayed
                    setForeground(textColor);
                    isShowingPlaceholder = false;
                }
            }
        });
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    public void setPlaceholderText(String text) {
        this.placeholderText = text;
        if (isShowingPlaceholder) {
            setText(text);
        }
    }


    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        if (isShowingPlaceholder) {
            setForeground(color);
        }
    }

    public void setTextColor(Color color) {
        this.textColor = color;
        if (!isShowingPlaceholder) {
            setForeground(color);
        }
    }

    public void addFilterAction(List<String> filterList, Runnable function1) {
        addActionListener(e -> {
            String text = getText().trim();

            if (!text.isEmpty() && !filterList.contains(text)) {
                filterList.add(text);
                function1.run();
            }
        });
    }
}