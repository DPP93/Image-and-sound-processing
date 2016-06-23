/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.poid.bzdp.zadanieObrazy;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Daniel
 */
public class ImageHistogram extends JFrame{

    private XYSeriesCollection categoryDataset;
    private Color c;
    
    private ImageHistogram(String title, XYSeriesCollection dataset, Color c) {
        super(title);
        this.categoryDataset = dataset;
        this.c = c;
        
        final JFreeChart chart = createChart(dataset, c);
        final ChartPanel chartPanel = new ChartPanel(chart);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    public static void showHistogram(String title, XYSeriesCollection dataset, Color c){
        final ImageHistogram histogram = new ImageHistogram(title, dataset, c);
        histogram.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        histogram.pack();
        RefineryUtilities.centerFrameOnScreen(histogram);
        histogram.setVisible(true);
        histogram.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    
    private JFreeChart createChart(final XYSeriesCollection dataset, Color c) {
        

        final JFreeChart chart = ChartFactory.createXYBarChart(
            "Histogram",
            "Brightness", 
            false,
            "Values", 
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        XYItemRenderer renderer = chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, c);
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        
        return chart;
        
    }
    
    

}
