/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie4.equalizer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Equalizer {
    
    private List<OneSlider> sliders;

    public Equalizer(){
        sliders = new ArrayList<>();
        double side = 20;
        do{
            sliders.add(new OneSlider(side, side*4));
            side *= 4;
        }while (side < 20000);
    }

    public List<OneSlider> getSliders() {
        return sliders;
    }
    
    
    
}
