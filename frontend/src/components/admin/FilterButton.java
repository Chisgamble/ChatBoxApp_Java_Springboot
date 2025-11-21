package components.admin;

import components.MyColor;
import components.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Interactive filter input field with placeholder text that disappears on focus
 */
public class FilterButton extends RoundedTextField {
    private String placeholderText;
    private String savedText = "";
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
        setupEnterListener();
    }

    private void setupFocusListener() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isShowingPlaceholder) {
                    setText(savedText);
                    setForeground(textColor);
                    isShowingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String currentText = getText().trim();
                if (currentText.isEmpty()) {
                    savedText = "";
                    setText(placeholderText);
                    setForeground(placeholderColor);
                    isShowingPlaceholder = true;
                } else {
                    // Keep the saved text displayed
                    setText(savedText);
                    setForeground(textColor);
                    isShowingPlaceholder = false;
                }
            }
        });
    }

    private void setupEnterListener() {
        addActionListener(e -> {
            String currentText = getText().trim();
            if (!currentText.isEmpty()) {
                savedText = currentText;
                setText(savedText);
                setForeground(textColor);
                isShowingPlaceholder = false;
                // Remove focus to show the saved text
                transferFocus();
            }
            else{
                isShowingPlaceholder = true;
                transferFocus();
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

    public String getActualText() {
        return savedText;
    }

    public void clearSavedText() {
        savedText = "";
        setText(placeholderText);
        setForeground(placeholderColor);
        isShowingPlaceholder = true;
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
}