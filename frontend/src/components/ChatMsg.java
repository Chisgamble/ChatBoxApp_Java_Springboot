package components;

import javax.swing.*;
import java.awt.*;

public class ChatMsg extends JPanel {

    public ChatMsg(String initials, String content, boolean isLeft, int width, int height){
        this.setPreferredSize(new Dimension(width, getPreferredSize().height));
        if (isLeft)
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        else
            this.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        Avatar avatar = new Avatar(initials);





    }
}
