/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie3.phase;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author bzielinski91
 */
public class Maps {
    
    private Map<Integer, Double> map = new LinkedHashMap<>();
    private Map<Double, Double> histogramWithoutZeros = new LinkedHashMap<>();
    
    public void setMap(Map<Integer, Double> map){
        this.map=map;
    }
    
    public Map<Integer, Double> getMap(){
        return map;
    }
    
    public void setHistogramWithoutZeros(Map<Double, Double> histogramWithoutZeros){
        this.histogramWithoutZeros=histogramWithoutZeros;
    }
    
    public Map<Double,Double> getHistogramWithoutZeros(){
        return histogramWithoutZeros;
    }
    
}
