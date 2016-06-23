package pl.dpbz.poid.zadanie3.converter;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-05-24.
 */
public abstract class SamplesToWaveConverter {

    private int samplesCountInOnePart;
    private double samplingFrequency;
    private List<Double[]> listOfAllSamples;
    private AudioFormat audioFormat;
    public SamplesToWaveConverter(double samplingFrequency, int samplesCountInOnePart, AudioFormat audioFormat){
        listOfAllSamples = new ArrayList<>();
        this.samplingFrequency = samplingFrequency;
        this.samplesCountInOnePart = samplesCountInOnePart;
        this.audioFormat = audioFormat;
    }

    abstract public void setupFrequenciesFromSamples(Integer[] samples);

    public void saveGeneratedSamples(String path) throws IOException {
        List<Double> allSamples = new ArrayList<>();
        for (Double[] tab : listOfAllSamples){
            for (Double i : tab) {
                allSamples.add(i);
            }
        }
        System.out.println("Samples ALL "+allSamples.size());
        byte[] bytesToSave = new byte[allSamples.size() * 2];

        int count = 0;
        for (int i = 0; i < allSamples.size(); i++){

            BigDecimal bi = new BigDecimal(Double.toString(allSamples.get(i).doubleValue()));
            byte[] b = bi.toBigInteger().toByteArray();
            if (b.length == 1) {
                bytesToSave[count+1] = b[0];
                bytesToSave[count] = 0;
            }else{
                bytesToSave[count+1] = b[0];
                bytesToSave[count] = b[1];
            }
            count += 2;
        }

        InputStream b_in = new ByteArrayInputStream(bytesToSave);
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(path));
        dos.write(bytesToSave);
        File file = new File(path);
        AudioInputStream stream = new AudioInputStream(b_in, audioFormat,
                bytesToSave.length/2);
        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
        b_in.close();
        dos.close();
        stream.close();
    }

    public int getSamplesCountInOnePart() {
        return samplesCountInOnePart;
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public List<Double[]> getListOfAllSamples() {
        return listOfAllSamples;
    }
}
