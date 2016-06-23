/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie4.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Convolution {

    public static List<Double> computeConvolutedSignal(List<Double> xn, List<Double> hn){
        List<Double> convolutedSignal = new ArrayList<>();
        
        for(int n = 0; n < (hn.size() + xn.size() - 1)*2; n++){
            double value = 0.0;
            for(int k = 0; k < hn.size(); k++){
                if(n-k < xn.size() && n-k >=0){
//                    System.out.println("h "+k+" x "+(n-k));
                    value += hn.get(k)*xn.get(n-k);
                }
            }
//            System.out.println("---------");
            convolutedSignal.add(value);
        }
        
        return convolutedSignal;
    }
}
