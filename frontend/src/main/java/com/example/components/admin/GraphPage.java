package com.example.components.admin;

import com.example.components.MyColor;
import com.example.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphPage extends JPanel {

    public GraphPage() {
        Font roboto = new Font("Roboto", Font.PLAIN, 16);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(MyColor.WHITE_BG);

        List<Integer> scores = new ArrayList<Integer>();
        Random random = new Random();
        int maxDataPoints = 12;
        int maxScore = 20;
        for (int i = 0; i < maxDataPoints ; i++) {
            scores.add(random.nextInt(maxScore));
        }

        DrawGraph newUserGraph = new DrawGraph(scores, 1000, 400);
        JPanel firstWrapper = new JPanel(new FlowLayout(FlowLayout.LEADING));
        firstWrapper.setPreferredSize(new Dimension(1900, 515));
        firstWrapper.setMaximumSize(new Dimension(1950, 520));
        firstWrapper.add(newUserGraph);
        firstWrapper.setBackground(MyColor.WHITE_BG);


        JLabel newUser = Utility.makeText("New users in year: ", roboto,  16f, Font.BOLD, MyColor.LIGHT_BLACK, null);
        FilterButton year1 = new FilterButton("year", 10);
        year1.setBackground(Color.WHITE);
        year1.setMaximumSize(new Dimension(year1.getWidth(), 40));
        JPanel newWrapper = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        newWrapper.add(newUser);
        newWrapper.add(year1);
        newWrapper.setMaximumSize(new Dimension(1900, 60));
        newWrapper.setBackground(MyColor.WHITE_BG);

        DrawGraph activeUserGraph = new DrawGraph(scores, 1000, 400);
        JPanel secondWrapper = new JPanel(new FlowLayout(FlowLayout.LEADING));
        secondWrapper.setPreferredSize(new Dimension(1900, 515));
        secondWrapper.setMaximumSize(new Dimension(1950, 520));
        secondWrapper.add(activeUserGraph);
        secondWrapper.setBackground(MyColor.WHITE_BG);


        JLabel activeUser = Utility.makeText("Active users in year: ", roboto,  16f, Font.BOLD, MyColor.LIGHT_BLACK, null);
        FilterButton year2 = new FilterButton("year", 10);
        year2.setBackground(Color.WHITE);
        year2.setMaximumSize(new Dimension(year2.getWidth(), 40));
        JPanel activeWrapper = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        activeWrapper.add(activeUser);
        activeWrapper.add(year2);
        activeWrapper.setMaximumSize(new Dimension(1900, 60));
        activeWrapper.setBackground(MyColor.WHITE_BG);

        JScrollPane scroll = new JScrollPane();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(newWrapper);
        panel.add(firstWrapper);
        panel.add(activeWrapper);
        panel.add(secondWrapper);
//        panel.setPreferredSize(new Dimension(1920,1080));

        scroll.getViewport().add(panel);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll);

    }
}
