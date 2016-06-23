package pl.dpbz.poid.zadanie3.sound;

import org.apache.commons.math.complex.Complex;
import pl.dpbz.poid.zadanie3.fourier.FastFourier;


/**
 * Created by Daniel on 2016-05-13.
 */
public class ComplexSound extends Sound {

    private Complex[] complexSamples;
    private int bits=0;

    public ComplexSound(Integer[] samples, double samplingFrequency, int bits) {
        super(samples, samplingFrequency);
        this.bits = bits;
        System.out.println("SAMPLES COMPLEX "+samples.length);
        //TODO dobierz liczbę bitów w zależności, żeby nie było za dużo tego dobrego
//        while(samples.length < Math.pow(2, this.bits)){
//            this.bits--;
//        }

        complexSamples = new Complex[(int) Math.pow(2,this.bits)];
        for(int i = 0; i < complexSamples.length; i++){
            if(i >= samples.length){
                complexSamples[i] = new Complex(0.0, 0.0);
            }else{
                complexSamples[i] = new Complex(samples[i], 0.0);
            }
        }

    }

    /**
     * Metoda przygotoywuje próbki obliczając dla nich okienko Hanninga i tranformację Fouriera
     */
    public void setupElementsOfComplexSound(){
//        computeHanningWindow();
        computeFourierOnSamples();
    }

    

    private void computeFourierOnSamples(){
        complexSamples = FastFourier.computeFastFourier(complexSamples, bits);
    }

    public ComplexSound(Integer[] samples, double samplingFrequency) {
        super(samples, samplingFrequency);
        complexSamples = new Complex[(int) Math.pow(2,bits)];
        for(int i = 0; i < complexSamples.length; i++){
            complexSamples[i] = new Complex(samples[i], 0.0);
        }
    }

    /**
     * Metoda wymnaża uzyskany sygnał przez funkcję okna
     */
    private void computeHanningWindow(){

        for (int i = 0; i < complexSamples.length; i++){
            double window = 0.5 * (1.0 - Math.cos(2.0 * Math.PI * (double)i) / (double)(complexSamples.length - 1));
            double re = complexSamples[i].getReal() * window;
            double img = complexSamples[i].getImaginary() * window;
            complexSamples[i] = new Complex(re, img);
        }

    }

    public Complex[] getComplexSamples() {
        return complexSamples;
    }

    public int getBits() {
        return bits;
    }
    
    public void setBits(int bits){
        this.bits=bits;
    }
}
