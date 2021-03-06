/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.bz.poid.fouriertest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Daniel
 */
public class Main {
    
    public static void main(String[] args) throws IOException{
        fourier();
        segementation();
    }
    
    private static void segementation() throws IOException{
        ClassLoader loader = Main.class.getClassLoader();
        BufferedImage image = ImageIO.read(loader.getResource("camera.bmp"));
        ImageIO.write(image, "bmp", new File("segmentationTestBefore.bmp"));
        Segmentation seg = new Segmentation(image);
        seg.computeSegmentationInFasterWay(10,10);
        image = seg.getImage();
        ImageIO.write(image, "bmp", new File("segmentationTestAfter.bmp"));
        System.out.println("DONE");
    }
    
    private static void fourier() throws IOException{
        ClassLoader loader = Main.class.getClassLoader();
<<<<<<< .mine
        BufferedImage image = ImageIO.read(loader.getResource("camera.bmp"));
        System.out.println(image.getColorModel().getPixelSize());
||||||| .r102
        BufferedImage image = ImageIO.read(loader.getResource("lena.bmp"));
=======
        BufferedImage image = ImageIO.read(loader.getResource("pentagon.bmp"));
>>>>>>> .r113
        ImageIO.write(image, "bmp", new File("test1.bmp"));
        FourierProc fp = new FourierProc(image);
        fp.computeFourierTransform();
<<<<<<< .mine
//        fp.computeFourierPhaseModificationFilter(100,0);
||||||| .r102
        fp.computeFourierPhaseModificationFilter(250, 0);
=======
//        fp.computeEdgeDetectionFilter(200, 50, 256, 256, 60, 155);
>>>>>>> .r113
//        fp.computeLowPassFilter(50, 256, 256);
        fp.computeBandPassFilter(230,5,256,256);
        image = fp.getFourierPowerSpectrum();
        image = FourierProc.changeImageQuadrants(image);
        ImageIO.write(image, "bmp", new File("testFourier.bmp"));

        fp.computeInverseFourierTransform();

        BufferedImage img = fp.getInverseFourierImage();
        ImageIO.write(img, "bmp", new File("testInverseFourier.jpg"));
    }
    
}
