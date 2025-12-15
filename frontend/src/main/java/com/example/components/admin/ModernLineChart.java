package com.example.components.admin;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Map;

public class ModernLineChart extends JPanel {

    private TimeSeries series;

    public ModernLineChart(String title, String yAxisLabel) {
        setLayout(new BorderLayout());

        series = new TimeSeries("New Users");
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title, "Month", yAxisLabel, dataset,
                false, true, false
        );

        styleChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.WHITE);

        add(chartPanel, BorderLayout.CENTER);
    }

    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlineVisible(false);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

        // --- 1. RENDERER STYLING ---
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(52, 152, 219));
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setDefaultShapesVisible(true);
        renderer.setDefaultShapesFilled(true);

        // --- SHOW VALUES ON TOP OF POINTS ---
        // "{2}" means display the Y-value (Count).
        // NumberFormat "0" ensures integers (5, not 5.0)
        XYItemLabelGenerator labelGenerator = new StandardXYItemLabelGenerator(
                "{2}", new SimpleDateFormat("MMM"), new DecimalFormat("0")
        );
        renderer.setDefaultItemLabelGenerator(labelGenerator);
        renderer.setDefaultItemLabelsVisible(true);

        // Push the label up slightly so it doesn't overlap the line
        renderer.setSeriesItemLabelFont(0, new Font("Roboto", Font.BOLD, 12));

        // --- 2. AXIS STYLING ---
        Font labelFont = new Font("Roboto", Font.BOLD, 14);
        Font axisFont = new Font("Roboto", Font.PLAIN, 12);

        // X-Axis: Show Month Only (MMM = Jan, Feb, Mar)
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH, 1, new SimpleDateFormat("MMM")));
        dateAxis.setLabelFont(labelFont);
        dateAxis.setTickLabelFont(axisFont);

        // Y-Axis
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLabelFont(labelFont);
        rangeAxis.setTickLabelFont(axisFont);
    }

    public void updateData(Map<LocalDate, Integer> data) {
        series.clear();
        for (Map.Entry<LocalDate, Integer> entry : data.entrySet()) {
            LocalDate date = entry.getKey();
            series.addOrUpdate(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()), entry.getValue());
        }
    }
}