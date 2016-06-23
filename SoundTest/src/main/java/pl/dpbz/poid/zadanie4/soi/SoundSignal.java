/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie4.soi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class SoundSignal {
    private static final double SAMMPLING_FREQUENCY = 44100;
    private List<Double> samples;
    
    public SoundSignal(int startingIndex, int windowSize, Integer[] samples){
        this.samples = new ArrayList<>();
        
        for(int i = startingIndex; i < startingIndex+windowSize; i++) {
            if(i < samples.length){
                this.samples.add(samples[i].doubleValue());
            }else{
                this.samples.add(0.0);
            } 
        }
    }

    public List<Double> getSamples() {
        return samples;
    }

    public void setSamples(List<Double> samples) {
        this.samples = samples;
    }
    
    
}
