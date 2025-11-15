import app.chatbox.frontend.components.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class Signup {
    public static Font getFont(String file_path) throws IOException, FontFormatException {
        InputStream is = Signup.class.getResourceAsStream(file_path);
        assert is != null;
        return Font.createFont(Font.TRUETYPE_FONT, is);
    }

    public static JLabel makeText(String value, Font font, Float font_size, Integer font_type, Color foreground, Color background){
        JLabel res = new JLabel();
        res.setText(value);
        res.setFont(font.deriveFont(font_type, font_size));
        res.setForeground(foreground);
        res.setBackground(background);
        res.setHorizontalAlignment(SwingConstants.LEFT);
        res.setVerticalAlignment(SwingConstants.CENTER);
        res.setAlignmentX(0.5f);
        return res;
    }

    public static void main(String[] arg) throws IOException, FontFormatException {

        JFrame mine = new JFrame();
        try{
            Font baloo = getFont("/font/Baloo_2/Baloo2-VariableFont_wght.ttf");
            //Sign up
            JLabel sign_up = makeText("SIGN UP", baloo, 75f, Font.BOLD, MyColor.LIGHT_BLUE, null);

            // or log in
            JLabel or_login = new JLabel("Or login");
            or_login.setFont(baloo.deriveFont(Font.PLAIN, 25f));
            or_login.setForeground(MyColor.LIGHT_BLUE);
            or_login.setAlignmentX(0.5f);

            // Username
            JTextField usr_name = new JTextField("Username or email");
            usr_name.setFont(baloo.deriveFont(Font.PLAIN, 18f));
            usr_name.setMaximumSize(new Dimension(325, 48));
            usr_name.setPreferredSize(new Dimension(325, 48));
            usr_name.setBackground(Color.WHITE);
            usr_name.setBorder(
                    BorderFactory.createCompoundBorder(
                            new RoundedBorder(Color.BLACK, 2, 20), // <- radius 20px
                            BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    )
            );
            usr_name.setAlignmentX(0.5f);
            usr_name.setToolTipText("Username or email");
            usr_name.setForeground(Color.GRAY);
            usr_name.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (usr_name.getText().equals("Username or email")) {
                        usr_name.setText("");
                        usr_name.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (usr_name.getText().isEmpty()) {
                        usr_name.setForeground(Color.GRAY);
                        usr_name.setText("Username or email");
                    }
                }
            });

            // Panel
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(sign_up);
            panel.add(or_login);
            panel.add(Box.createVerticalStrut(10));
            panel.add(usr_name);

            mine.setLayout(new BorderLayout());
            mine.add(panel, BorderLayout.CENTER);
            mine.setVisible(true);
        }
        catch (IOException | FontFormatException e) {
            System.out.println("io font error");
            throw new RuntimeException(e);
        }
        catch(Throwable e){
            System.out.println("Error");
        }


    }
}
