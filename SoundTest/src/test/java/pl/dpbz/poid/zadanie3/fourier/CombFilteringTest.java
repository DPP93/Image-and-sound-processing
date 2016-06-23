package pl.dpbz.poid.zadanie3.fourier;

import org.junit.Test;
import pl.dpbz.poid.zadanie3.converter.WaveToSamplesConverter;
import pl.dpbz.poid.zadanie3.sound.ComplexSound;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Daniel on 2016-05-23.
 */
public class CombFilteringTest {


/*    @Test
    public void shouldComputeFluteFrequency1779Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/natural/flute/1779Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/natural/flute/1779Hz.wav"));
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        CombFiltering combFilter = new CombFiltering(cs, samplingFrequency);
        combFilter.computeFrequency();
        double exp = 1779;
        double res = combFilter.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeFluteFrequency276Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/natural/flute/276Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/natural/flute/276Hz.wav"));
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        CombFiltering combFilter = new CombFiltering(cs, samplingFrequency);
        combFilter.computeFrequency();
        double exp = 276;
        double res = combFilter.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeViolaFrequency130Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/natural/viola/130Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/natural/viola/130Hz.wav"));
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        CombFiltering combFilter = new CombFiltering(cs, samplingFrequency);
        combFilter.computeFrequency();
        double exp = 130;
        double res = combFilter.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeViolaFrequency698Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/natural/viola/698Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/natural/viola/698Hz.wav"));
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        CombFiltering combFilter = new CombFiltering(cs, samplingFrequency);
        combFilter.computeFrequency();
        double exp = 698;
        double res = combFilter.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeArtDiff80Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/artificial/diff/80Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/artificial/diff/80Hz.wav"));
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        CombFiltering combFilter = new CombFiltering(cs, samplingFrequency);
        combFilter.computeFrequency();
        double exp = 80;
        double res = combFilter.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeArtDiff1366Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/artificial/diff/1366Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/artificial/diff/1366Hz.wav"));
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        CombFiltering combFilter = new CombFiltering(cs, samplingFrequency);
        combFilter.computeFrequency();
        double exp = 1366;
        double res = combFilter.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeArtMed90Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/artificial/med/90Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/artificial/med/90Hz.wav"));
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        CombFiltering combFilter = new CombFiltering(cs, samplingFrequency);
        combFilter.computeFrequency();
        double exp = 90;
        double res = combFilter.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeArtMed1537Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/artificial/med/1537Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/artificial/med/1537Hz.wav"));
        ComplexSound cs = new ComplexSound(ints, samplingFrequency, 15);
        CombFiltering combFilter = new CombFiltering(cs, samplingFrequency);
        combFilter.computeFrequency();
        double exp = 1537;
        double res = combFilter.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }*/
}