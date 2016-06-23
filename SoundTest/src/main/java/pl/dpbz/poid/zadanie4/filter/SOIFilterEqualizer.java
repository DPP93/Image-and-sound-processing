/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie4.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math.complex.Complex;
import pl.dpbz.poid.zadanie3.chart.ChartDrawer;
import pl.dpbz.poid.zadanie4.equalizer.Equalizer;
import static pl.dpbz.poid.zadanie4.filter.SOIFilter.addElems;
import pl.dpbz.poid.zadanie4.soi.SoundSignal;
import pl.dpbz.poid.zadanie4.soi.WindowType;
import pl.dpbz.poid.zadanie4.utils.Fourier;

/**
 * Nie powinno się to tak nazywać, ale SOIFilter ma funkcjonalności, które się
 * przydadzą
 *
 * @author Daniel
 */
public class SOIFilterEqualizer extends SOIFilter {

    private Equalizer equalizer;
    private Complex[] responseForEqualizer;

    public SOIFilterEqualizer(Equalizer equalizer) {
        this.equalizer = equalizer;
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    @Override
    public void computeFilter() {
        Double[] eqResp = new Double[(int) N];
        outputSignal = new Double[samplesCount];
        for (int i = 0; i < outputSignal.length; i++) {
            outputSignal[i] = 0.0;
        }
        Complex[] signalTab;
        Complex[] resultOfChangingAmplitude;
        resultsOfFilterOperations.clear();
        double frequencyResolution = SAMPLING_FREQUENCY / N;
        List<SoundSignal> signals = new ArrayList<>(signalWindows);

        for (int i = 0; i < eqResp.length; i++){
            eqResp[i] = 0.0;
        }
        
        for (int equalizerSlide = 0; equalizerSlide < equalizer.getSliders().size(); equalizerSlide++) {
            int leftSide = (int) ((equalizer.getSliders().get(equalizerSlide).getLeftSideOfFrequencies() * N) / SAMPLING_FREQUENCY);
            int rightSide = (int) ((equalizer.getSliders().get(equalizerSlide).getRightSideOfFrequencies() * N) / SAMPLING_FREQUENCY);
            double edge = 0;
            if (equalizer.getSliders().get(equalizerSlide).getEdge() >= 1) {
                edge = equalizer.getSliders().get(equalizerSlide).getEdge();
            } else {
                edge = 1.0 / Math.abs(equalizer.getSliders().get(equalizerSlide).getEdge());
            }
//                    System.out.println(leftSide + " " + rightSide + " " + edge);
            for (int spectrumIndex = leftSide; spectrumIndex < rightSide; spectrumIndex++) {
                eqResp[spectrumIndex] = new Double(edge);
                eqResp[eqResp.length - spectrumIndex - 1] = new Double(edge);
            }
        }
        ChartDrawer.drawChart(eqResp,"Equalizer");
        for (int windowIndex = 0; windowIndex < signalWindows.size(); windowIndex++) {
            signalTab = new Complex[signals.get(windowIndex).getSamples().size()];
//            System.out.println(signalTab.length);
            for (int sampleIndex = 0; sampleIndex < signalTab.length; sampleIndex++) {
                signalTab[sampleIndex] = new Complex(signals.get(windowIndex).getSamples().get(sampleIndex), 0.0);
            }
            signalTab = Fourier.computeFastFourier(signalTab, bits);

            resultOfChangingAmplitude = new Complex[signalTab.length];
            for (int i = 0; i < resultOfChangingAmplitude.length; i++) {
                resultOfChangingAmplitude[i] = new Complex(0, 0);
            }
            for (int i = 0; i < resultOfChangingAmplitude.length; i++) {
                resultOfChangingAmplitude[i] = signalTab[i].multiply(eqResp[i]);
            }
            if (windowIndex == signalWindows.size() / 2) {
                ChartDrawer.drawChart(signalTab, "Signal before");
                ChartDrawer.drawChart(resultOfChangingAmplitude, "Window spectrum after");
            }
            resultOfChangingAmplitude = Fourier.computeInverseFastFourier(resultOfChangingAmplitude, bits);
            if (windowIndex == signalWindows.size() / 2) {
                ChartDrawer.drawFromChart(resultOfChangingAmplitude, "Window after inverse");
            }
            Double[] d = new Double[resultOfChangingAmplitude.length];
            for (int i = 0; i < d.length; i++) {
                d[i] = resultOfChangingAmplitude[i].getReal();
            }
            resultsOfFilterOperations.add(d);
        }
        System.out.println("Końcowka dodaję");
        for (int i = 0; i < resultsOfFilterOperations.size(); i++) {
            System.out.println((i + 1) + " Elem " + (i * R));
            addElems((int) (i * R), resultsOfFilterOperations.get(i), outputSignal);
        }
    }

//    @Override
//    public void computeFilter() {
//        computeLowPassFilterParameters();
////        System.out.println("Samples count "+samplesCount);
//        outputSignal = new Double[samplesCount];
//        for(int i = 0; i < outputSignal.length; i++){
//            outputSignal[i] = 0.0;
//        }
//        Complex[] signalTab;
//        Complex[] impulseResponseComplex = new Complex[impulseResponse.size()];
//        Complex[] resultOfMultiplication;
//        resultsOfFilterOperations.clear();
//        for (int i = 0; i < impulseResponse.size(); i++) {
//            impulseResponseComplex[i] = new Complex(impulseResponse.get(i), 0.0);
//        }
//        impulseResponseComplex = Fourier.computeFastFourier(impulseResponseComplex, bits);
//
//        for (int windowIndex = 0; windowIndex < signalWindows.size(); windowIndex++) {
//            signalTab = new Complex[signalWindows.get(windowIndex).getSamples().size()];
////            System.out.println(signalTab.length);
//            for (int sampleIndex = 0; sampleIndex < signalTab.length; sampleIndex++) {
//                signalTab[sampleIndex] = new Complex(signalWindows.get(windowIndex).getSamples().get(sampleIndex), 0.0);
//            }
//            signalTab = Fourier.computeFastFourier(signalTab, bits);
//
//            resultOfMultiplication = new Complex[signalTab.length];
//            for (int i = 0; i < resultOfMultiplication.length; i++) {
//                resultOfMultiplication[i] = signalTab[i].multiply(impulseResponseComplex[i]);
//            }
//
//            resultOfMultiplication = Fourier.computeInverseFastFourier(resultOfMultiplication, bits);
//            Double[] d = new Double[resultOfMultiplication.length];
//            for (int i = 0; i < d.length; i++) {
//                d[i] = resultOfMultiplication[i].getReal();
//            }
//            resultsOfFilterOperations.add(d);
//            if (windowIndex == signalWindows.size() - 1) {
//                //ChartDrawer.drawChart(signalTab, "signal spec" + windowIndex);
//                //ChartDrawer.drawChart(impulseResponseComplex, "impulse resp spec" + windowIndex);
//                //ChartDrawer.drawChart(d, "result of inverse from mul" + windowIndex);
//            }
//        }
//        
////        System.out.println("Windows "+resultsOfFilterOperations.size());
////        System.out.println("OutputSize "+outputSignal.length);
////        System.out.println("Size of one window"+resultsOfFilterOperations.get(0).length);
////        System.out.println("HOP Size "+R);
//        for (int i = 0; i < resultsOfFilterOperations.size(); i++) {
////            System.out.println((i+1) +" Elem "+(i*R));
//            addElems((int)(i*R), resultsOfFilterOperations.get(i), outputSignal);
//        }
//    }
//    @Override
//    public void computeLowPassFilterParameters() {
//        System.out.println("------------------------------------chuj------------------------------------------------");
//        impulseResponse.clear();
//        for (int i = 0; i < (int) L; i++) {
//            impulseResponse.add(0.0);
//        }
//        for (int i = 0; i < N - L; i++) {
//            impulseResponse.add(0.0);
//        }
//        for (int equalizerSlide = 0; equalizerSlide < equalizer.getSliders().size(); equalizerSlide++) {
//            double leftSide = (int) equalizer.getSliders().get(equalizerSlide).getLeftSideOfFrequencies();
//            double rightSide = (int) equalizer.getSliders().get(equalizerSlide).getLeftSideOfFrequencies();
//            double edge = equalizer.getSliders().get(equalizerSlide).getEdge();
//            if (equalizer.getSliders().get(equalizerSlide).getEdge() >= 1) {
//                edge = equalizer.getSliders().get(equalizerSlide).getEdge();
//            } else {
//                edge = 1.0 / Math.abs(equalizer.getSliders().get(equalizerSlide).getEdge());
//            }
////                    System.out.println(leftSide + " " + rightSide + " " + edge);
//            double[] tabLowPass = new double[(int) L];
//            double[] tabHighPass = new double[(int) L];
//            for (int k = 0; k < L; k++) {
//                if (k == (L - 1) / 2) {
//                    tabHighPass[k] = (2 * leftSide / SAMPLING_FREQUENCY)*edge;
//                } else {
//                    tabHighPass[k] = computeStrangeSincFunction(k, leftSide)*edge;
//                }
//                tabHighPass[k] *= Math.pow(-1, k);
//            }
//
//            for (int k = 0; k < L; k++) {
//                if (k == (L - 1) / 2) {
//                    tabLowPass[k] = (2 * rightSide / SAMPLING_FREQUENCY)*edge;
//                } else {
//                    tabLowPass[k] = computeStrangeSincFunction(k, rightSide)*edge;
//                }
//            }
//            for (int i = 0; i < (int) L; i++) {
//                double sum = impulseResponse.get(i)+(tabHighPass[i]*tabLowPass[i]);
//                System.out.println("Sum "+sum);
//                impulseResponse.set(i, sum);
//            }
////            if(equalizerSlide == 0 || equalizerSlide == 4){
////                ChartDrawer.drawChart(impulseResponse.toArray(new Double[impulseResponse.size()]), "Impulse response for EQ");
////            }
//        }
//    }
    @Override
    public void setWindowType(WindowType windowType
    ) {

    }

    @Override
    public void setFc(double fc
    ) {

    }

}
