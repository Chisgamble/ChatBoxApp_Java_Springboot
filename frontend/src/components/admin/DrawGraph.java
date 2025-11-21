package components.admin;

import components.MyColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

@SuppressWarnings("serial")
public class DrawGraph extends JPanel {
    private static final int MAX_SCORE = 20;
    private static int PREF_W = 0;
    private static int PREF_H = 0;
    private static final int BORDER_GAP = 30;
    private static final Color GRAPH_COLOR = MyColor.LIGHT_BLUE;
    private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
    private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    private static final int GRAPH_POINT_WIDTH = 10;
    private static final int Y_HATCH_CNT = 12;
    private List<Integer> scores;

    public DrawGraph(List<Integer> scores) {
        this.scores = scores;
        PREF_W = 900;
        PREF_H = 400;
    }

    public DrawGraph(List<Integer> scores, int w, int h) {
        this.scores = scores;
        PREF_W = w;
        PREF_H = h;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (scores.size() - 1);
        double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

        List<Point> graphPoints = new ArrayList<Point>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
            graphPoints.add(new Point(x1, y1));
        }

        // create x and y axes
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        g2.setFont(new Font("roboto", Font.PLAIN, 14));
        // create hatch marks for y axis.
        for (int i = 0; i < Y_HATCH_CNT; i++) {
            int x0 = BORDER_GAP;
            int x1 = GRAPH_POINT_WIDTH + BORDER_GAP + 2;
            int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
            int y1 = y0;
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        g2.drawString("1", BORDER_GAP - 3, getHeight());
        for (int i = 0; i < scores.size() - 1; i++) {
            int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
            int x1 = x0;
            int y0 = getHeight() - BORDER_GAP;
            int y1 = y0 - GRAPH_POINT_WIDTH - 2;
            g2.drawLine(x0, y0, x1, y1);

            String label = String.valueOf((i + 2)); // example value
            FontMetrics fm = g2.getFontMetrics();
            int strWidth = fm.stringWidth(label);
            g2.drawString(label, x1 - strWidth/2, y0 + BORDER_GAP);
        }

        Stroke oldStroke = g2.getStroke();
        g2.setColor(GRAPH_COLOR);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setFont(new Font("roboto", Font.BOLD, 16));
        FontMetrics fm2 = g2.getFontMetrics();
        g2.setStroke(oldStroke);
        g2.setColor(GRAPH_POINT_COLOR);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
            int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
            int ovalW = GRAPH_POINT_WIDTH;
            int ovalH = GRAPH_POINT_WIDTH;
            g2.fillOval(x, y, ovalW, ovalH);

            // Draw score on top of the point
            String scoreStr = String.valueOf(scores.get(i));
            int strWidth = fm2.stringWidth(scoreStr);
            // x position: center text on the oval
            int strX = graphPoints.get(i).x - strWidth / 2;
            // y position: slightly above the oval
            int strY = graphPoints.get(i).y - ovalH / 2 - 8; // -2 for padding
            g2.setColor(MyColor.LIGHT_BLACK); // text color

            g2.drawString(scoreStr, strX, strY);
            g2.setColor(GRAPH_POINT_COLOR); // reset color for next point
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

//    private static void createAndShowGui() {
//        List<Integer> scores = new ArrayList<Integer>();
//        Random random = new Random();
//        int maxDataPoints = 12;
//        int maxScore = 20;
//        for (int i = 0; i < maxDataPoints ; i++) {
//            scores.add(random.nextInt(maxScore));
//        }
//        DrawGraph mainPanel = new DrawGraph(scores);
//
//        JFrame frame = new JFrame("DrawGraph");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(mainPanel);
//        frame.pack();
//        frame.setLocationByPlatform(true);
//        frame.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGui();
//            }
//        });
//    }
}