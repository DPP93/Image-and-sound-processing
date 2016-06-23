/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie4.equalizer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class EqualizerTest {
    

    @Test
    public void creatingTest() {
        Equalizer eq = new Equalizer();
        for(int i = 0; i < eq.getSliders().size(); i++){
            System.out.println(eq.getSliders().get(i).getLeftSideOfFrequencies() + " "+eq.getSliders().get(i).getRightSideOfFrequencies());
        }
        assertTrue("Number of slides is incorrect",eq.getSliders().size() == 5);
    }
    
}
