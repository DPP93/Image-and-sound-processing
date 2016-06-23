package pl.dpbz.poid.zadanie3.phase;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pl.dpbz.poid.zadanie3.sound.Sound;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Daniel on 2016-05-20.
 */
public class PhaseSpaceMultiDimensional {

    private double frequency;

    private double[][] histogram;
    private int spaceSize = 15;
    private int window = 1000;
    private int k = 2;
    private int startSample = 3500;
    private int startingComputingSample = 10;
    private double edgeForHistogram = 0.05;
    private int minHistogramParts = 70;
    private int histogramInterval = 100;

    private Sound sound;

    private List<double[]> phaseSpace;
    private double[] distances;


    public PhaseSpaceMultiDimensional(Sound sound) {
        this.sound = sound;
        startSample = sound.getSamples().length/3;
    }

    public int getSpaceSize() {
        return spaceSize;
    }

    public void setSpaceSize(int spaceSize) {
        this.spaceSize = spaceSize;
    }

    public double getFrequency() {
        return frequency;
    }

    public Sound getSound() {
        return sound;
    }

    public List<double[]> getPhaseSpace() {
        return phaseSpace;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getStartSample() {
        return startSample;
    }

    public void setStartSample(int startSample) {
        this.startSample = startSample;
    }

    public int getStartingComputingSample() {
        return startingComputingSample;
    }

    public void setStartingComputingSample(int startingComputingSample) {
        this.startingComputingSample = startingComputingSample;
    }

    public double getEdgeForHistogram() {
        return edgeForHistogram;
    }

    public void setEdgeForHistogram(double edgeForHistogram) {
        this.edgeForHistogram = edgeForHistogram;
    }

    public int getMinHistogramParts() {
        return minHistogramParts;
    }

    public void setMinHistogramParts(int minHistogramParts) {
        this.minHistogramParts = minHistogramParts;
    }

    public int getHistogramInterval() {
        return histogramInterval;
    }

    public void setHistogramInterval(int histogramInterval) {
        this.histogramInterval = histogramInterval;
    }

    public void setupPhaseSpace() {
        phaseSpace = new ArrayList<>();
        int[] samples = sound.getSamples();
        int windowCounter = 0;
        for (int i = startSample; i < samples.length - (spaceSize*k); i++) {
            if(windowCounter > window){
                break;
            }
            double[] tab = new double[spaceSize];
            for(int j = 0; j < tab.length; j++){
                tab[j] = samples[i+k*(j+1)];
            }
            phaseSpace.add(tab);
            windowCounter++;
        }
    }

    public Maps computeFrequency(){
        Map<Integer, Double> map = new LinkedHashMap<>();
        for(int i = startingComputingSample; i < phaseSpace.size(); i++){
//            System.out.println(i + " " +computeDifference(phaseSpace.get(0), phaseSpace.get(i)));
            map.put(i, computeDifference(phaseSpace.get(0), phaseSpace.get(i)));
        }
//        drawElements(map);
        Maps maps = new Maps();
        maps.setMap(map);
        
        
        generateHistogram(map.values());

        Map<Double, Double> histogramWithoutZeros = new LinkedHashMap<>();
        for(double[] d : histogram){
            if(d[1] != 0 ){
                histogramWithoutZeros.put(d[0], d[1]);
            }
        }
//        drawHistogram(histogramWithoutZeros);
         maps.setHistogramWithoutZeros(histogramWithoutZeros);

        int elements = (int) (histogramWithoutZeros.size() * edgeForHistogram);
//        System.out.println("Wzięte po uwagę jest " + (int)(histogramWithoutZeros.size() * edgeForHistogram) + " elementow");
        if(elements < 2 ){
            elements = 2;
        }
        double bound = 0;
        int k = 0;
        for (Double d : histogramWithoutZeros.keySet()){
            if (k == elements){
                bound = d;
                break;
            }
            k++;
        }

//        System.out.println("Bound "+bound);
        boolean wasBiggerThanTwoTimeBound = false;
        double lastValue = -1;
        int lastIndex = 0;
        for(Map.Entry<Integer, Double>entry : map.entrySet()){
            if(lastValue == -1){
                lastIndex = entry.getKey();
                lastValue = entry.getValue();
                continue;
            }

            if(lastValue < entry.getValue() && lastValue < bound && wasBiggerThanTwoTimeBound){
                break;
            }

            if(entry.getValue() > 2 * bound){
                wasBiggerThanTwoTimeBound = true;
            }

            lastIndex = entry.getKey();
            lastValue = entry.getValue();
        }
        frequency = sound.getSamplingFrequency() / lastIndex;
        
        return maps;
    }

    private double computeDifference(double[] tab1, double[] tab2){
        double diff = 0;
        double d = 0;
        for(int i = 0; i < tab1.length; i++){
            d  = Math.abs(tab1[i] - tab2[i]);
            d /= (double)tab1.length;
            diff += d;
        }

        return diff;
    }

    public JFreeChart drawElements(Map<Integer, Double> map){
        final XYSeries phase = new XYSeries("D");

        final XYSeriesCollection dataset = new XYSeriesCollection();
        map.entrySet().stream().forEach(entry -> phase.add(entry.getKey(), entry.getValue()));

        dataset.addSeries(phase);
        JFreeChart chart = ChartFactory.createXYLineChart("Elements", "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        //ChartDrawer.drawChart(chart);
        return chart;
    }

    public JFreeChart drawHistogram(Map<Double, Double> map){
        final XYSeries phase = new XYSeries("D");

        final XYSeriesCollection dataset = new XYSeriesCollection();

//        for(int i = 0; i < histogram.length; i++){
//            phase.add(histogram[i][0], histogram[i][1]);
//        }

        map.entrySet().stream().forEach(entry -> phase.add(entry.getKey(), entry.getValue()));

        dataset.addSeries(phase);
        JFreeChart chart = ChartFactory.createScatterPlot("Histogram", "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        //ChartDrawer.drawChart(chart);
        return chart;
    }

    public void generateHistogram(Collection<Double> col){

        //Co 100
        int interval = histogramInterval;

        /*Przyjąłem, że amplituda jest od 0 do 2000,
        górna wartość amplitudy nie ma znaczenia, bo szukami minimów,
        a minima najwyższe są na poziomie 700 dla ostatniej struny gitary*/
//        System.out.println("MAX "+col.stream().max(Double::compareTo).get().doubleValue());
        int parts = (int) (col.stream().max(Double::compareTo).get().doubleValue() / interval);

        while(parts <= minHistogramParts){
            interval--;
            parts = (int) (col.stream().max(Double::compareTo).get().doubleValue() / interval);
        }

        //Przedziały 0 - 100, 100 - 200 itd.
        histogram = new double[parts][2];
        double val = 0;
        for (int i = 0; i < histogram.length; i++) {
//            System.out.println(val);
            histogram[i][0] = val;
            histogram[i][1] = 0;
            val += interval;

        }

        for(double d : col){
            double actualKey = 0.0;
            for (int i = 0; i < histogram.length; i++) {
                if (d > histogram[i][0]) {
                    actualKey = histogram[i][0];
                } else {
                    break;
                }
            }
            for (int i = 0; i < histogram.length; i++) {
                if (histogram[i][0] == actualKey) {
                    histogram[i][1]++;
                }
            }
        }
    }
}
