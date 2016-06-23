package pl.dpbz.poid.zadanie3.converter;

import pl.dpbz.poid.zadanie3.fourier.CombFiltering;
import pl.dpbz.poid.zadanie3.generator.SignalGenerator;
import pl.dpbz.poid.zadanie3.sound.ComplexSound;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-05-24.
 */
public class CombFilteringSampleConverter extends SamplesToWaveConverter {

    private CombFiltering combFiltering;

    public CombFilteringSampleConverter(double samplingFrequency, int samplesCountInOnePart, AudioFormat audioFormat) {
        super(samplingFrequency, samplesCountInOnePart, audioFormat);
    }

    @Override
    public void setupFrequenciesFromSamples(Integer[] samples) {
        System.out.println("Samples conversion "+samples.length);
        List<Integer[]> samplesToCompute = new ArrayList<>();
        System.out.println("Comb Samples " + samples.length);
        for(int i = 0; i < samples.length - getSamplesCountInOnePart(); i+= getSamplesCountInOnePart()){
            Integer[] tab = new Integer[super.getSamplesCountInOnePart()];
            for(int j = 0; j < tab.length; j++){
                tab[j] = samples[i+j];
            }
            samplesToCompute.add(tab);
        }
        System.out.println("Comb: Elements to compute "+samplesToCompute.size());
        System.out.println("Comb: To count "+samplesToCompute.get(0).length);
        double t1 = 0;
        for (Integer[] t : samplesToCompute){

            ComplexSound cs = new ComplexSound(t, getSamplingFrequency(), 15);
            cs.setupElementsOfComplexSound();
            combFiltering = new CombFiltering(cs, getSamplingFrequency());
            combFiltering.computeFrequency();

            double f = combFiltering.getFrequency();
//            System.out.println("COMB "+f);
            getListOfAllSamples().add(SignalGenerator.generateSinusoidalSignal(32760, f, t1, getSamplesCountInOnePart(), getSamplingFrequency()));
            t1 += (double)getSamplesCountInOnePart() / getSamplingFrequency();
        }
    }
}
