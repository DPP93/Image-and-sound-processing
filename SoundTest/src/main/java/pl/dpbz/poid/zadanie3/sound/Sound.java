package pl.dpbz.poid.zadanie3.sound;

/**
 * Created by Daniel on 2016-05-13.
 */
public class Sound {
    private int[] samples;
    private double samplingFrequency;

    public Sound(Integer[] samples, double samplingFrequency) {
        this.samples = new int[samples.length];
        for (int i = 0; i < this.samples.length; i++) {
            this.samples[i] = samples[i].intValue();
        }

        this.samplingFrequency = samplingFrequency;
    }

    public Sound(int[] samples, double samplingFrequency) {
        this.samples = samples;
        this.samplingFrequency = samplingFrequency;
    }

    public int[] getSamples() {
        return samples;
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }
}
