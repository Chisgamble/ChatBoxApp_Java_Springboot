package util;

import ui.Login;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Utility {
    public static Font getFont(String file_path) throws IOException, FontFormatException {
        InputStream is = Login.class.getResourceAsStream(file_path);
        assert is != null;
        return Font.createFont(Font.TRUETYPE_FONT, is);
    }

    public static JLabel makeText(String value, Font font, Float font_size, Integer font_type, Color foreground, Color background){
        JLabel res = new JLabel();
        res.setText(value);
        res.setFont(font.deriveFont(font_type, font_size));
        res.setForeground(foreground);
        res.setBackground(background);
        return res;
    }
}
