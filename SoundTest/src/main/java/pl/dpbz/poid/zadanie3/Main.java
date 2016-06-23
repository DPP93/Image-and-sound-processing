/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie3;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pl.dpbz.poid.zadanie3.chart.ChartDrawer;
import pl.dpbz.poid.zadanie3.converter.CombFilteringSampleConverter;
import pl.dpbz.poid.zadanie3.converter.PhaseSpaceSampleConverter;
import pl.dpbz.poid.zadanie3.converter.SamplesToWaveConverter;
import pl.dpbz.poid.zadanie3.converter.WaveToSamplesConverter;
import pl.dpbz.poid.zadanie3.fourier.CombFiltering;
import pl.dpbz.poid.zadanie3.phase.PhaseSpaceMultiDimensional;
import pl.dpbz.poid.zadanie3.sound.ComplexSound;
import pl.dpbz.poid.zadanie3.sound.Sound;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Daniel
 */
public class Main {

    public static void main(String args[]) throws UnsupportedAudioFileException, IOException {
        //Zassanie pliku
        File f = new File("ExampleSounds/artificial/easy/100Hz.wav");
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(f);
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(f);

        //Zwykły sound, który nie potrzebuje zespolonych elementów
        Sound s = new Sound(ints, samplingFrequency);

        //Obliczenie częstotliwości przy pomocy fazówki
        PhaseSpaceMultiDimensional pS = new PhaseSpaceMultiDimensional(s);
        pS.setupPhaseSpace();
        pS.computeFrequency();
        System.out.println("Phase space "+ pS.getFrequency());

        //Zespolony dźwięk, który jest bardziej złożony
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        //To oblicza fouriera
        cs.setupElementsOfComplexSound();
        //Tutaj obliczanie częstotliwości przy pomocy fouriera
        CombFiltering cf = new CombFiltering(cs, samplingFrequency);
        cf.computeFrequency();
        cf.drawComplexSound();
        System.out.println("Comb Filtering "+cf.getFrequency());


        //Przy sekwencjach nie ma majstrowania przy ustawieniach, użytkownik
        // zmiennie może podawać tylko liczbę próbek, resztę jak częstotliwość próbkowania
        // i audio format pobiera się z otwieranego pliku

        //Test sekwencji na fazie
        File file = new File("ExampleSounds/seq/DWK_violin.wav");
        Integer[] seqInte = WaveToSamplesConverter.convertWaveToIntSamples(file);
        System.out.println("Samples overview "+seqInte.length);
        double seqSamplingFrequency = WaveToSamplesConverter.getSamplingFrequency(file);
        int samplesPerPart = 3000;
        SamplesToWaveConverter conv =
                new PhaseSpaceSampleConverter
                        (seqSamplingFrequency, samplesPerPart, WaveToSamplesConverter.getAudioFormat(file));

        conv.setupFrequenciesFromSamples(seqInte);
        System.out.println("Zapisuję");
        conv.saveGeneratedSamples("XDPhase.wav");

        //Test sekwencji na grzebieniu
        SamplesToWaveConverter conv2 =
                new CombFilteringSampleConverter
                        (seqSamplingFrequency, samplesPerPart, WaveToSamplesConverter.getAudioFormat(file));

        conv2.setupFrequenciesFromSamples(seqInte);
        System.out.println("Zapisuję");
        conv2.saveGeneratedSamples("XDComb.wav");

        //Rysowanie wykresu sygnału dźwiękowego
        final XYSeries dist = new XYSeries("P0");
        int index = 0;
        for (Integer i : ints){
            dist.add(index/samplingFrequency, i);
            index++;
        }
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(dist);

        JFreeChart chart = ChartFactory.createXYLineChart(f.getName(), "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        NumberAxis domain = (NumberAxis) xyPlot.getRangeAxis();
        domain.setRange(-32768,32768);
        ChartDrawer.drawChart(chart);


        System.out.println("FINISHED");
    }

}
