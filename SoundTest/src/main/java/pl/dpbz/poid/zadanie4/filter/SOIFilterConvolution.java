/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie4.filter;

import java.util.List;
import org.apache.commons.math.complex.Complex;
import pl.dpbz.poid.zadanie3.chart.ChartDrawer;
import static pl.dpbz.poid.zadanie4.filter.SOIFilter.addElems;
import pl.dpbz.poid.zadanie4.soi.SoundSignal;
import pl.dpbz.poid.zadanie4.utils.Convolution;
import pl.dpbz.poid.zadanie4.utils.Fourier;

/**
 *
 * @author Daniel
 */
public class SOIFilterConvolution extends SOIFilter {
    @Override
    public void setSignalWindows(Integer[] samples) {
        signalWindows.clear();
        this.samplesCount = samples.length;
        SoundSignal ss;
        System.out.println(samples.length);
        for (int windowIndex = 0; windowIndex < samples.length / M; windowIndex++) {
//            System.out.println(windowIndex*R + ", " + ((windowIndex*R)+M) + " " + M);
            ss = new SoundSignal((int) (windowIndex * R), (int) M, samples);
//            System.out.println("SS size "+ss.getSamples().size());
            signalWindows.add(ss);
        }
    }

    @Override
    public void computeLowPassFilterParameters() {
        impulseResponse.clear();
//        System.out.println("Start");
        double[] tab = new double[(int)L];
        for (int k = 0; k < L; k++) {
//            System.out.println(k + " ");
            if (k == (L - 1) / 2) {
                tab[k]=(2 * fc / SAMPLING_FREQUENCY);
            } else {
                tab[k] = computeStrangeSincFunction(k);
            }
            double window = windowType.getValue(k, M);
            tab[k] = tab[k] * window;
        }
        for (int i = 0; i < tab.length; i++){
            impulseResponse.add(tab[i]);
        }
    }
    
    @Override
    public void computeFilter() {
        outputSignal = new Double[samplesCount];
        for (int i = 0; i < outputSignal.length; i++) {
            outputSignal[i] = 0.0;
        }

        resultsOfFilterOperations.clear();
        List<Double> convolutedElements;
        for (int windowIndex = 0; windowIndex < signalWindows.size(); windowIndex++) {
            convolutedElements = Convolution.computeConvolutedSignal(impulseResponse, signalWindows.get(windowIndex).getSamples());
            resultsOfFilterOperations.add(convolutedElements.toArray(new Double[convolutedElements.size()]));
        }
        int sizeOfNewWindow = resultsOfFilterOperations.get(0).length;
        for (int i = 0; i < resultsOfFilterOperations.size(); i++) {
            addElems((int) (i * R), resultsOfFilterOperations.get(i), outputSignal);
        }
    }

}
