/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.bz.poid.fouriertest;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.imageio.ImageIO;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Daniel
 */
public class FourierProcTest {
    
    private BufferedImage bi;
    private FourierProc fp;
    private int imageWidth;
    private int imageHeight;
    
    public FourierProcTest() {
    }

    @Before
    public void setup() throws IOException{
        ClassLoader loader = FourierProcTest.class.getClassLoader();
        bi = ImageIO.read(loader.getResource("lena.bmp"));
        imageWidth = bi.getWidth();
        imageHeight = bi.getHeight();
        fp = new FourierProc(bi);
    }
    
    @Test
    public void testTableOneConversion() {
        int[][] checkingTab = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] resultTab = {{1,4,7},{2,5,8},{3,6,9}};
        
        checkingTab = FourierProc.convertTable(checkingTab);
        for(int i = 0; i<checkingTab.length; i++){
            for(int j = 0; j < checkingTab[i].length; j++){
                if(checkingTab[i][j] != resultTab[i][j]){
                    assertTrue("Zła konwersja", false);
                }
            }
        }
        assertTrue(true); 
    }
    
    @Test
    public void testTableTwoConversion() {
        int[][] checkingTab = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] resultTab = {{1,2,3},{4,5,6},{7,8,9}};
        
        checkingTab = FourierProc.convertTable(checkingTab);
        checkingTab = FourierProc.convertTable(checkingTab);
        for(int i = 0; i<checkingTab.length; i++){
            for(int j = 0; j < checkingTab[i].length; j++){
                if(checkingTab[i][j] != resultTab[i][j]){
                    assertTrue("Zła konwersja", false);
                }
            }
        }
        assertTrue(true); 
    }
    
    @Test
    public void shouldReturnTrueWhenImageIsInsideBorders(){
        int radius = 50;
        int x = 60, y = 90;
        boolean result = fp.isCircleInImage(radius, x, y);
        StringBuilder sb = new StringBuilder();
        sb.append("Radius "+radius+"\n");
        sb.append("X "+x+" : image width "+imageWidth+" "+(x+radius)+"\n");
        sb.append("Y "+y+" : image height "+imageHeight+" "+(y+radius)+"\n");
        assertTrue(sb.toString(),result);
    }
    
    @Test
    public void shouldReturnFalseWhenImageOusideLeft(){
        int radius = 100;
        int x = -60, y = 90;
        boolean result = fp.isCircleInImage(radius, x, y);
        StringBuilder sb = new StringBuilder();
        sb.append("Radius "+radius+"\n");
        sb.append("X "+x+" : image width "+imageWidth+"\n");
        sb.append("Y "+y+" : image height "+imageHeight+"\n");
        assertTrue(sb.toString(), result == false);
    }
    
    @Test
    public void shouldReturnFalseWhenImageOusideRight(){
        int radius = 100;
        int x = imageWidth + 20, y = 90;
        boolean result = fp.isCircleInImage(radius, x, y);
        StringBuilder sb = new StringBuilder();
        sb.append("Radius "+radius+"\n");
        sb.append("X "+x+" : image width "+imageWidth+"\n");
        sb.append("Y "+y+" : image height "+imageHeight+"\n");
        assertTrue(sb.toString(), result == false);
    }
    
    @Test
    public void shouldReturnFalseWhenImageOusideTop(){
        int radius = 100;
        int x = 100, y = -30;
        boolean result = fp.isCircleInImage(radius, x, y);
        StringBuilder sb = new StringBuilder();
        sb.append("Radius "+radius+"\n");
        sb.append("X "+x+" : image width "+imageWidth+" "+(x+radius)+"\n");
        sb.append("Y "+y+" : image height "+imageHeight+" "+(y+radius)+"\n");
        assertTrue(sb.toString(), result == false);
    }
    
    @Test
    public void shouldReturnFalseWhenImageOusideBottom(){
        int radius = 100;
        int x = 100, y = imageHeight+30;
        boolean result = fp.isCircleInImage(radius, x, y);
        StringBuilder sb = new StringBuilder();
        sb.append("Radius "+radius+"\n");
        sb.append("X "+x+" : image width "+imageWidth+"\n");
        sb.append("Y "+y+" : image height "+imageHeight+"\n");
        assertTrue(sb.toString() + " Powinno być false ", result == false);
    }
    
    @Test
    public void shouldReturnFalseWhenImageOusideLeftCorner(){
        int radius = 100;
        int x = -20, y = -20;
        boolean result = fp.isCircleInImage(radius, x, y);
        StringBuilder sb = new StringBuilder();
        sb.append("Radius "+radius+"\n");
        sb.append("X "+x+" : image width "+imageWidth+"\n");
        sb.append("Y "+y+" : image height "+imageHeight+"\n");
        assertTrue(sb.toString() + " Powinno być false ", result == false);
    }

}
