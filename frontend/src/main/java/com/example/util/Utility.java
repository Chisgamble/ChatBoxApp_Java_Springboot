package com.example.util;

import com.example.ui.Login;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

public class Utility {
    public static Font getFont(String file_path) throws IOException, FontFormatException {
        InputStream is = Login.class.getResourceAsStream(file_path);
        assert is != null;
        return Font.createFont(Font.TRUETYPE_FONT, is);
    }

    public static JLabel makeText(String value, Font font, Float font_size, Integer font_type, Color foreground, Color background) {
        JLabel res = new JLabel();
        res.setText(value);
        if (font == null) {
            font = UIManager.getFont("Label.font");
        }
        res.setFont(font.deriveFont(font_type, font_size));
        res.setForeground(foreground);
        if (background != null) {
            res.setOpaque(true);
            res.setBackground(background);
        }
        return res;
    }

    public static String[][] convertToString(List<List<String>> list) {
        int row = list.size();
        int col = list.get(0).size();
        String[][] result = new String[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                result[i][j] = list.get(i).get(j);
            }
        }
        return result;
    }

    static public String safeString(String s) {
        return s == null ? "" : s;
    }

    static public String safeBool(Boolean b) {
        return b == null ? "" : b.toString();
    }

    static public String safeDate(LocalDate d) {
        return d == null ? "" : d.toString();
    }

    public static String getInitials(String name){
        String[] words = name.split("\\s+");
        if (words.length > 1){
            return words[0].substring(0,1).toUpperCase() + words[1].substring(0,1).toUpperCase();
        }else{
            return words[0].substring(0,1).toUpperCase();
        }
    }
}
