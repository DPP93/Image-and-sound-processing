package pl.dpbz.poid.zadanie3.phase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.dpbz.poid.zadanie3.converter.WaveToSamplesConverter;
import pl.dpbz.poid.zadanie3.sound.Sound;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Daniel on 2016-05-21.
 */
public class PhaseSpaceMultiDimensionalTest {
 /*   private PhaseSpaceMultiDimensional phaseSpace;


    @Test
    public void shouldComputeFluteFrequency1779Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/natural/flute/1779Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/natural/flute/1779Hz.wav"));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 1779;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeFluteFrequency276Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/natural/flute/276Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/natural/flute/276Hz.wav"));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 276;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeViolaFrequency130Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/natural/viola/130Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/natural/viola/130Hz.wav"));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 130;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeViolaFrequency698Hz() throws IOException, UnsupportedAudioFileException {
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File("ExampleSounds/natural/viola/698Hz.wav"));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File("ExampleSounds/natural/viola/698Hz.wav"));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 698;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }


    @Test
    public void shouldComputeEasyFrequency100Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/easy/100Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 100;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeEasyFrequency506Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/easy/506Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 506;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeEasyFrequency1708Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/easy/1708Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 1708;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeMedFrequency90Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/med/90Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 90;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeMedFrequency455Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/med/455Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 455;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeMedFrequency1537Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/med/1537Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 1537;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeDiffFrequency80Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/diff/80Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 80;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeDiffFrequency405Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/diff/405Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 405;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }

    @Test
    public void shouldComputeDiffFrequency1366Hz() throws IOException, UnsupportedAudioFileException {
        String path = "ExampleSounds/artificial/diff/1366Hz.wav";
        Integer[] ints = WaveToSamplesConverter.convertWaveToIntSamples(new File(path));
        double samplingFrequency = WaveToSamplesConverter.getSamplingFrequency(new File(path));
        Sound s = new Sound(ints, samplingFrequency);
        phaseSpace = new PhaseSpaceMultiDimensional(s);
        phaseSpace.setupPhaseSpace();
        phaseSpace.computeFrequency();

        double exp = 1366;
        double res = phaseSpace.getFrequency();
        double leftBoundary = exp * Math.pow(2.0, -(1.0/24.0));
        double rightBoundary = exp * Math.pow(2.0, 1.0/24.0);
        System.out.println("EXP "+ exp + " RES "+ res);
        assertTrue("EXP "+ exp + " RES "+ res, res >= leftBoundary && res <= rightBoundary);
    }*/
}