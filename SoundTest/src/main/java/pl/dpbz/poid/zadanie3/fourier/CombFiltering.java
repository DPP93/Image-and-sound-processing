package pl.dpbz.poid.zadanie3.fourier;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pl.dpbz.poid.zadanie3.chart.ChartDrawer;
import pl.dpbz.poid.zadanie3.generator.SignalGenerator;
import pl.dpbz.poid.zadanie3.sound.ComplexSound;
import pl.dpbz.poid.zadanie3.sound.Sound;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * Created by Daniel on 2016-05-21.
 */
public class CombFiltering {

    private ComplexSound complexSound;

    private List<Sound> triangleSignals = new ArrayList<>();

    private double samplingFrequency;
    private double combAmplitude = 500000;
    private int combVerticesCount = 10;
    private double combDistanceIncreaser = 1;
    private int startingDistance = 50;
    private int endDistance = 50;
    private Map<Integer, Double> correlations = new LinkedHashMap<>();
    private int maxDist;
    private double frequency;

    public CombFiltering(ComplexSound complexSound, double samplingFrequency) {
        this.complexSound = complexSound;
        this.samplingFrequency = samplingFrequency;
        startingDistance = (int) (65 * Math.pow(2,complexSound.getBits()) / samplingFrequency);
        endDistance = (int) (2100 * Math.pow(2,complexSound.getBits()) / samplingFrequency);
    }

    public double getCombAmplitude() {
        return combAmplitude;
    }

    public void setCombAmplitude(double combAmplitude) {
        this.combAmplitude = combAmplitude;
    }

    public double getCombVerticesCount() {
        return combVerticesCount;
    }

    public void setCombVerticesCount(int combVerticesCount) {
        this.combVerticesCount = combVerticesCount;
    }

    public Map computeFrequency() {

        double k = samplingFrequency / Math.pow(2, complexSound.getBits());

        for (int i = startingDistance; i <= endDistance; i += combDistanceIncreaser) {
            triangleSignals.add(SignalGenerator.generateImpulses(combAmplitude, i, combVerticesCount));
        }

        int c = startingDistance;
        for (int i = 0; i < triangleSignals.size(); i++) {
//            System.out.println(i + " dist "+ c);
            correlations.put(c, calculateCorrelations(complexSound, triangleSignals.get(i)));
            c += combDistanceIncreaser;
        }

//        drawCorrelations(correlations);
        
        double max = 0;
        for (Map.Entry<Integer, Double> entry : correlations.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxDist = entry.getKey();
            }
        }
        System.out.println("distMax + \" \" + max = " + maxDist + " " + max);
        System.out.println("FREQUENCY COMB FILTERING " + maxDist * k);
        frequency = maxDist * k;
        
        return correlations;
    }

    public double getFrequency() {
        return frequency;
    }

    public JFreeChart drawComplexSound() {
        XYSeries complexValues = new XYSeries("Spectrum");

        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < complexSound.getComplexSamples().length; i++) {
            complexValues.add(i, complexSound.getComplexSamples()[i].abs());
        }

        XYSeries combs = new XYSeries("Combs");
        double d = correlations.get(maxDist);
        
        for(int i = 0; i<combVerticesCount; i++){
            combs.add(i*maxDist, combAmplitude);
        }
        
        dataset.addSeries(complexValues);
        dataset.addSeries(combs);
                
        JFreeChart chart = ChartFactory.createXYLineChart("Spectrum", "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
       
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        renderer.setSeriesPaint(1, Color.red);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesLinesVisible(1, false);
        xyPlot.setRenderer(renderer);
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        //ChartDrawer.drawChart(chart);
        return chart;
    }

    public double calculateCorrelations(ComplexSound complexSound, Sound triangle) {
        double correlation = 0;
        int length = triangle.getSamples().length >= complexSound.getComplexSamples().length ?
                complexSound.getComplexSamples().length : triangle.getSamples().length;

        for (int i = 0; i < length; i++) {
            correlation += complexSound.getComplexSamples()[i].abs() * triangle.getSamples()[i];
        }
        correlation /= samplingFrequency;
//        System.out.println("correlation = " + correlation);
        return correlation;
    }

    public JFreeChart drawCorrelations(Map<Integer, Double> correlationMap) {
        final XYSeries phase = new XYSeries("D");

        final XYSeriesCollection dataset = new XYSeriesCollection();

        correlationMap.entrySet().stream().forEach(entry -> phase.add(entry.getKey(), entry.getValue()));

        dataset.addSeries(phase);
        JFreeChart chart = ChartFactory.createXYLineChart("Spectrum", "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        //ChartDrawer.drawChart(chart);
        return chart;
    }
}
