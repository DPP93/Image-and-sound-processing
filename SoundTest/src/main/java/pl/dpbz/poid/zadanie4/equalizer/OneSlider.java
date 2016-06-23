/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.zadanie4.equalizer;

/**
 * Nie miałem pojęcia za bardzo jak to nazwać
 * @author Daniel
 */
public class OneSlider {
    private double leftSideOfFrequencies;
    private double rightSideOfFrequencies;
    
    private double edge = 1;

    public OneSlider(double leftSideOfFrequencies, double rightSideOfFrequencies) {
        this.leftSideOfFrequencies = leftSideOfFrequencies;
        this.rightSideOfFrequencies = rightSideOfFrequencies;
    }

    public double getEdge() {
        return edge;
    }

    public void setEdge(double edge) {
        this.edge = edge;
    }

    public double getLeftSideOfFrequencies() {
        return leftSideOfFrequencies;
    }

    public double getRightSideOfFrequencies() {
        return rightSideOfFrequencies;
    }
}
