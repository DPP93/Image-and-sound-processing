package pl.dpbz.poid.zadanie3.generator;

import pl.dpbz.poid.zadanie3.sound.Sound;

/**
 * Created by Daniel on 2016-05-21.
 */
public class SignalGenerator {

    public static Double[] generateSinusoidalSignal(double amplitude, double frequency, double t1, double time, double samplingFrequency){
        Double[] samples = new Double[(int) (time)];
        double T = 1.0/frequency;
        for(double i = 0; i < samples.length; i++){
            double t = i / samplingFrequency;
            samples[(int) i] = computeSinusoidalSignalValue(amplitude, t1, t, T);
        }

        return samples;
    }

    private static double computeSinusoidalSignalValue(double amplitude, double t1, double t, double T){
        return amplitude * Math.sin(((2*Math.PI)*(t-t1))/T);
    }

    public static Sound generateImpulses (double amplitude, int distanceBetweenImpulses, int impulsesCount){
        int[] samples = new int[(int) distanceBetweenImpulses * impulsesCount];
//        System.out.println("Impulses = " + impulsesCount);
        for(int i=0; i<samples.length; i++){
            if (i % distanceBetweenImpulses == 0){
                samples[i] = (int) amplitude;
            }else{
                samples[i] = 0;
            }
        }

        return new Sound(samples, distanceBetweenImpulses);
    }
}
