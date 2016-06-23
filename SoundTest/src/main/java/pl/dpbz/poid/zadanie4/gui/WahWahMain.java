/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie4.gui;

import com.sun.media.sound.WaveFileReader;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import pl.dpbz.poid.zadanie3.chart.ChartDrawer;
import pl.dpbz.poid.zadanie3.converter.WaveToSamplesConverter;
import pl.dpbz.poid.zadanie4.filter.SOIFilter;
import pl.dpbz.poid.zadanie4.filter.SOIWahWah;
import pl.dpbz.poid.zadanie4.utils.SamplesToWave;

/**
 *
 * @author Daniel
 */
public class WahWahMain {

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        
        Integer[] samples = WaveToSamplesConverter.convertWaveToIntSamples(new File("chirp_100Hz_1000Hz_lin.wav"));
        AudioFormat af = WaveToSamplesConverter.getAudioFormat(new File("chirp_100Hz_1000Hz_lin.wav"));
        
        SOIWahWah filter = new SOIWahWah();
        filter.setAmplify(2);
        filter.setFrequency(5);
        filter.setLeftSideFreq(200);
        filter.setRightSideFreq(800);
        filter.setWidth(20);
        filter.setM(1024);
        filter.setN(11);
        filter.setR(1024);
        filter.setSignalWindows(samples);
        filter.computeFilter();
        ChartDrawer.drawChart(filter.getOutputSignal(), "Wah Wah Muachachacha");
        
        SamplesToWave saver = new SamplesToWave(44100, filter.getOutputSignal(), af);
        saver.saveWave("GAGA.wav");
        System.out.println("Można wyłączyć");
    }
}
