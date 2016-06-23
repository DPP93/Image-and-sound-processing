package pl.dpbz.poid.zadanie3.converter;

import pl.dpbz.poid.zadanie3.generator.SignalGenerator;
import pl.dpbz.poid.zadanie3.phase.PhaseSpaceMultiDimensional;
import pl.dpbz.poid.zadanie3.sound.Sound;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2016-05-24.
 */
public class PhaseSpaceSampleConverter extends SamplesToWaveConverter {


    private PhaseSpaceMultiDimensional phaseSpaceMultiDimensional;

    public PhaseSpaceSampleConverter(double samplingFrequency, int samplesCountInOnePart, AudioFormat audioFormat) {
        super(samplingFrequency, samplesCountInOnePart, audioFormat);
    }

    @Override
    public void setupFrequenciesFromSamples(Integer[] samples) {
        List<Integer[]> samplesToCompute = new ArrayList<>();
        System.out.println("Samples " + samples.length);
        for(int i = 0; i < samples.length - getSamplesCountInOnePart(); i+= getSamplesCountInOnePart()){
            Integer[] tab = new Integer[super.getSamplesCountInOnePart()];
            for(int j = 0; j < tab.length; j++){
                tab[j] = samples[i+j];
            }
            samplesToCompute.add(tab);
        }

        System.out.println("To count "+samplesToCompute.get(0).length);
        double t1 = 0;
        for (Integer[] t : samplesToCompute){
            phaseSpaceMultiDimensional = new PhaseSpaceMultiDimensional(new Sound(t, getSamplingFrequency()));
            phaseSpaceMultiDimensional.setStartSample(0);
            phaseSpaceMultiDimensional.setWindow(t.length);
            phaseSpaceMultiDimensional.setupPhaseSpace();
            phaseSpaceMultiDimensional.computeFrequency();

            double f = phaseSpaceMultiDimensional.getFrequency();
//            System.out.println("PHASE "+f);
            getListOfAllSamples().add(SignalGenerator.generateSinusoidalSignal(32760, f, t1, getSamplesCountInOnePart(), getSamplingFrequency()));
            t1 += ((double)getSamplesCountInOnePart() / getSamplingFrequency());
        }

//        final XYSeries phase = new XYSeries("D");
//
//        double count = 0;
//        final XYSeriesCollection dataset = new XYSeriesCollection();
//        for (Double[]tab : getListOfAllSamples()){
//            for (Double i : tab){
//                phase.add(count/getSamplingFrequency(), i);
//                count++;
//            }
//        }
//
//        dataset.addSeries(phase);
//        JFreeChart chart = ChartFactory.createXYLineChart("Elements", "index", "Distance",
//                dataset, PlotOrientation.VERTICAL, true, true, false);
//        XYPlot xyPlot = (XYPlot) chart.getPlot();
//        xyPlot.setDomainCrosshairVisible(true);
//        xyPlot.setRangeCrosshairVisible(true);
//        XYItemRenderer renderer = xyPlot.getRenderer();
//        renderer.setSeriesPaint(0, Color.blue);
//        ChartDrawer.drawChart(chart);
    }
}
