/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.bz.poid.fouriertest;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;
import org.apache.commons.math.complex.Complex;

/**
 *
 * @author Daniel
 */
public class FourierProc {

    private BufferedImage image;
    private Complex[][] fourierImage;
    private BufferedImage fourierImageForDisplaying;
    private int imageWidth;
    private int imageHeight;

    public FourierProc(BufferedImage image) {
        this.image = copyImage(image);

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();

        fourierImage = new Complex[image.getWidth()][image.getHeight()];
        for (int i = 0; i < fourierImage.length; i++) {
            for (int j = 0; j < fourierImage[i].length; j++) {
                fourierImage[i][j] = new Complex(0.0, 0.0);
            }
        }
    }

    public static BufferedImage copyImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void computeFourierTransform() {
        //Wprowadzenie początkowych danych
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                double[] tab = new double[1];
                tab = image.getRaster().getPixel(x, y, tab);
                fourierImage[x][y] = new Complex(tab[0], 0.0);
            }
        }
        //Wiersze
        for (int i = 0; i < fourierImage.length; i++) {
            fourierImage[i] = computeDIFForOneDimension(fourierImage[i]);
        }
        //Kolumny
        fourierImage = convertTable(fourierImage);
        for (int i = 0; i < fourierImage.length; i++) {
            fourierImage[i] = computeDIFForOneDimension(fourierImage[i]);
        }
        fourierImage = convertTable(fourierImage);
//        fourierImage = changeImageQuadrants(fourierImage);
    }

    /**
     * Obraca naszego fouriera i umieszcza przekształcenie w fourierImage
     */
    public void computeInverseFourierTransform() {
//        //Wiersze
        for (int i = 0; i < fourierImage.length; i++) {
            fourierImage[i] = computeInverseDIFForOneDimension(fourierImage[i]);
        }
        //Kolumny
        fourierImage = convertTable(fourierImage);
        for (int i = 0; i < fourierImage.length; i++) {
            fourierImage[i] = computeInverseDIFForOneDimension(fourierImage[i]);
        }
        fourierImage = convertTable(fourierImage);
    }

    public static int[][] convertTable(int[][] tab) {
        int[][] tab2 = new int[tab.length][tab[0].length];
        for (int x = 0; x < tab.length; x++) {
            for (int y = 0; y < tab[x].length; y++) {
                tab2[y][x] = tab[x][y];
            }
        }
        return tab2;
    }

    public boolean isCircleInImage(int radius, int centerX, int centerY) {
        int left = centerX - radius;
        int right = centerX + radius;
        int top = centerY - radius;
        int bottom = centerY + radius;

        if (left < 0 || right >= imageWidth || top < 0 || bottom >= imageHeight) {
            return false;
        }

        return true;
    }

    /**
     * Robi filtr dolnoprzepustowy. Tworzy koło, na Fourierze, którego bok jest
     * odległy od środka o distanceFromCenter
     *
     * @param radius
     */
    public void computeHighPassFilter(int radius, int circleCenterX, int circleCenterY) {
        fourierImage = FourierProc.changeImageQuadrants(fourierImage);
        if (isCircleInImage(radius, circleCenterX, circleCenterY)) {
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    if (computeDistance(circleCenterX, circleCenterY, x, y) <= radius) {
                        fourierImage[x][y] = new Complex(0, 0);
                    }
                }
            }
        }
        fourierImage = FourierProc.changeImageQuadrants(fourierImage);
    }

    /**
     * Robi filtr dolnoprzepustowy. Tworzy kwadrat, na Fourierze, którego bok
     * jest odległy od środka o distanceFromCenter
     *
     * @param distanceFromCenter
     */
    public void computeLowPassFilter(int radius, int circleCenterX, int circleCenterY) {
        fourierImage = FourierProc.changeImageQuadrants(fourierImage);
        if (isCircleInImage(radius, circleCenterX, circleCenterY)) {
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    if (computeDistance(circleCenterX, circleCenterY, x, y) > radius) {
                        fourierImage[x][y] = new Complex(0, 0);
                    }
                }
            }
        }
        fourierImage = FourierProc.changeImageQuadrants(fourierImage);
    }

    /**
     *
     * @param distanceFromCenterExternal - zewnętrzny kwadrat
     * @param distanceFromCenterInternal - wewnętrzny kwadrat
     */
    public void computeBandPassFilter(int radiusExternal, int radiusInternal, int circleCenterX, int circleCenterY) {
        fourierImage = FourierProc.changeImageQuadrants(fourierImage);
        if (isCircleInImage(radiusExternal, circleCenterX, circleCenterY) && isCircleInImage(radiusInternal, circleCenterX, circleCenterY)) {

            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    if(x == imageWidth/2 && y == imageHeight/2){
                        continue;
                    }
                    if (computeDistance(circleCenterX, circleCenterY, x, y) > radiusExternal) {
                        fourierImage[x][y] = new Complex(0, 0);
                    } else if (computeDistance(circleCenterX, circleCenterY, x, y) < radiusInternal) {
                        fourierImage[x][y] = new Complex(0, 0);
                    }
                }
            }
        }
        fourierImage = FourierProc.changeImageQuadrants(fourierImage);
    }

    public void computeEdgeDetectionFilter(int radiusExternal, int radiusInternal, int circleCenterX, int circleCenterY, double alpha1, double alpha2) {
        fourierImage = FourierProc.changeImageQuadrants(fourierImage);
        if (isCircleInImage(radiusExternal, circleCenterX, circleCenterY) && isCircleInImage(radiusInternal, circleCenterX, circleCenterY)) {
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    if(x == imageWidth/2 && y == imageHeight/2){
                        continue;
                    }
                    if (computeDistance(circleCenterX, circleCenterY, x, y) > radiusExternal) {
                        fourierImage[x][y] = new Complex(0, 0);
                    } else if (computeDistance(circleCenterX, circleCenterY, x, y) < radiusInternal) {
                        fourierImage[x][y] = new Complex(0, 0);
                    } else {
                       //Tutaj wycinanie tych kawałków
                        //obliczamy x do którego trzeba
                        double px = x - imageWidth/2;
                        double py = y - imageHeight/2;
                        double d = Math.sqrt(px*px + py*py);
                        px /=d;
                        py /=d;
                        double aRad1 = Math.toRadians(alpha1);
                        double aRad2 = Math.toRadians(alpha2);
                        double a = Math.atan2(-py, -px)+Math.PI;
                        double a2 = Math.atan2(py, px)+Math.PI;
                        if((aRad1 <= a && a <= aRad2)|| (aRad1 <= a2 && a2 <= aRad2)) {
                            fourierImage[x][y] = new Complex(0, 0);
                        }
                    }
                }
            }
        }
        fourierImage = FourierProc.changeImageQuadrants(fourierImage);
    }

    public void computeFourierPhaseModificationFilter(int k, int l) {

        for (int n = 0; n < imageWidth; n++) {
            for (int m = 0; m < imageHeight; m++) {
                double real = Math.cos((-n * k * 2 * Math.PI / (double) imageWidth) + (-m * l * 2 * Math.PI / (double) imageHeight) + (k + l) * Math.PI);
                double img = -Math.sin((-n * k * 2 * Math.PI / (double) imageWidth) + (-m * l * 2 * Math.PI / (double) imageHeight) + (k + l) * Math.PI);
                fourierImage[n][m] = fourierImage[n][m].multiply(new Complex(real, img));
            }
        }
    }

    public static Complex[][] convertTable(Complex[][] tab) {
        Complex[][] tab2 = new Complex[tab.length][tab[0].length];
        for (int x = 0; x < tab.length; x++) {
            for (int y = 0; y < tab[x].length; y++) {
                tab2[y][x] = tab[x][y];
            }
        }
        return tab2;
    }

    public void computeDFT() {

        int N = imageWidth;
        int M = imageHeight;
        int counter = 1;
        for (int x = 0; x < imageHeight; x++) {
            for (int y = 0; y < imageWidth; y++) {

                double[] t = new double[1];
                double imageValue = image.getRaster().getPixel(x, y, t)[0];
                double realValue = 0;
                double imaginaryValue = 0;
                for (int k = 0; k < N; k++) {
                    for (int l = 0; l < M; l++) {
                        realValue += imageValue
                                * Math.cos(((2.0 * Math.PI * (double) k * (double) x) + (2.0 * Math.PI * (double) l * (double) y)) / N);
                        imaginaryValue += imageValue * (-Math.sin(((2.0 * Math.PI * (double) k * (double) x) + (2.0 * Math.PI * (double) l * (double) y)) / N));
                    }
                }
                fourierImage[x][y] = new Complex(realValue, imaginaryValue);
                System.out.println(counter + " of " + (M * N));
                counter++;
            }
        }

    }

    private Complex[] computeDIFForOneDimension(Complex[] pixelTable) {
        int bits = 9;
        double N = pixelTable.length;

        Complex[] transformedSignal = new Complex[(int) N];

        for (int i = 0; i < transformedSignal.length; i++) {
            transformedSignal[i] = new Complex(0.0, 0.0);
        }

        Complex signalTab[] = new Complex[(int) N];
        Complex[] localTR = new Complex[(int) N];
        int index = 0;
        for (int i = 0; i < pixelTable.length; i++) {
            signalTab[index] = new Complex(pixelTable[i].getReal(), pixelTable[i].getImaginary());
            index++;
        }

        index = 0;
        for (Complex cv : signalTab) {
//            System.out.println("x(" + index + ") = " + cv.getReal() + " IM: i" + cv.getImaginary());
            index++;
        }
        //Zmienna określająca na jakiej wielkości ma operować na tablicy
        int part = 2;
        //Pętla określająca cykl przechodzenia, przez kolejne kolumny 
        for (int iteration = 1; iteration <= bits; iteration++) {
//            System.out.println("PART "+part);
            //Ile razy ma się wykonać
            for (int i = 0; i < part; i += 2) {

                int r = 0;
                for (int actualIndex = (signalTab.length / part) * i, counter = 0; counter < signalTab.length / part; counter++, actualIndex++) {
                    int secondIndex = (actualIndex + (signalTab.length / part));
                    Complex a = signalTab[actualIndex].add(signalTab[secondIndex]);
                    Complex b = signalTab[actualIndex].subtract(signalTab[secondIndex]);
                    Complex W = new Complex(Math.cos((2.0 * Math.PI * r) / N), -Math.sin((2.0 * Math.PI * r) / N));
                    b = b.multiply(W);
                    signalTab[actualIndex] = a;
                    signalTab[secondIndex] = b;
                    r += part - (part / 2);
                }
            }
            part += part;
        }

        localTR[0] = signalTab[0];
        localTR[localTR.length - 1] = signalTab[signalTab.length - 1];
        for (int i = 1; i < signalTab.length - 1; i++) {
            String bitIndex = Integer.toBinaryString(i);
            if (bitIndex.length() < bits) {
                while (bitIndex.length() < bits) {
                    bitIndex = "0" + bitIndex;
                }
            }
            char[] tab = bitIndex.toCharArray();

            for (int j = 0; j < tab.length / 2; j++) {
                char temp = tab[j];
                tab[j] = tab[tab.length - j - 1];
                tab[tab.length - j - 1] = temp;
            }
            bitIndex = new String(tab);
            localTR[Integer.parseInt(bitIndex, 2)] = signalTab[i];
        }
        for (int i = 0; i < localTR.length; i++) {
            transformedSignal[i] = new Complex(localTR[i].getReal(), localTR[i].getImaginary());
        }
        return transformedSignal;
    }

    private Complex[] computeInverseDIFForOneDimension(Complex[] pixelTable) {
        int bits = 9;
        double N = pixelTable.length;

        Complex[] transformedSignal = new Complex[(int) N];

        for (int i = 0; i < transformedSignal.length; i++) {
            transformedSignal[i] = new Complex(0.0, 0.0);
        }

        Complex signalTab[] = new Complex[(int) N];
        Complex[] localTR = new Complex[(int) N];
        int index = 0;
        for (int i = 0; i < pixelTable.length; i++) {
            signalTab[index] = new Complex(pixelTable[i].getReal(), pixelTable[i].getImaginary());
            index++;
        }

        index = 0;
        for (Complex cv : signalTab) {
//            System.out.println("x(" + index + ") = " + cv.getReal() + " IM: i" + cv.getImaginary());
            index++;
        }
        //Zmienna określająca na jakiej wielkości ma operować na tablicy
        int part = 2;
        //Pętla określająca cykl przechodzenia, przez kolejne kolumny 
        for (int iteration = 1; iteration <= bits; iteration++) {
//            System.out.println("PART "+part);
            //Ile razy ma się wykonać
            for (int i = 0; i < part; i += 2) {

                int r = 0;
                for (int actualIndex = (signalTab.length / part) * i, counter = 0; counter < signalTab.length / part; counter++, actualIndex++) {
                    int secondIndex = (actualIndex + (signalTab.length / part));
                    Complex a = signalTab[actualIndex].add(signalTab[secondIndex]);
                    Complex b = signalTab[actualIndex].subtract(signalTab[secondIndex]);
                    Complex W = new Complex(Math.cos((2.0 * Math.PI * r) / N), Math.sin((2.0 * Math.PI * r) / N));
                    b = b.multiply(W);
                    signalTab[actualIndex] = a;
                    signalTab[secondIndex] = b;
                    r += part - (part / 2);
                }
            }
            part += part;
        }

        localTR[0] = signalTab[0];
        localTR[localTR.length - 1] = signalTab[signalTab.length - 1];
        for (int i = 1; i < signalTab.length - 1; i++) {
            String bitIndex = Integer.toBinaryString(i);
            if (bitIndex.length() < bits) {
                while (bitIndex.length() < bits) {
                    bitIndex = "0" + bitIndex;
                }
            }
            char[] tab = bitIndex.toCharArray();

            for (int j = 0; j < tab.length / 2; j++) {
                char temp = tab[j];
                tab[j] = tab[tab.length - j - 1];
                tab[tab.length - j - 1] = temp;
            }
            bitIndex = new String(tab);
            localTR[Integer.parseInt(bitIndex, 2)] = signalTab[i];
        }
        for (int i = 0; i < localTR.length; i++) {
            transformedSignal[i] = new Complex(localTR[i].getReal() / N, localTR[i].getImaginary() / N);
        }
        return transformedSignal;
    }

    private Complex[] inverseDFT(Complex[] tab) {
        double N = 256;
        double[] realSamples = new double[tab.length];
        double[] imaginarySamples = new double[tab.length];
        Complex[] returnTable = new Complex[tab.length];
        for (int i = 0; i < tab.length; i++) {
            realSamples[i] = tab[i].getReal();
            imaginarySamples[i] = tab[i].getImaginary();
        }

        double realValue = 0;
        double imaginaryValue = 0;
        for (double n = 0; n < N; n++) {
            realValue = 0;
            imaginaryValue = 0;
            for (int m = 0; m < N; m++) {
                realValue += realSamples[m] * Math.cos((2.0 * Math.PI * (double) m * n) / N);
                realValue += imaginarySamples[m] * Math.sin((2.0 * Math.PI * (double) m * n) / N);
                imaginaryValue += realSamples[m] * Math.sin((2.0 * Math.PI * (double) m * n) / N);
                imaginaryValue -= imaginarySamples[m] * Math.cos((2.0 * Math.PI * (double) m * n) / N);
            }
//            System.out.println("Con: "+con+" z "+(int)N*2+" pixel "+n+" z "+(int)N+" : "+realValue+" "+imaginaryValue);
            returnTable[(int) n] = new Complex(realValue / (N), imaginaryValue / (N));
        }
        con++;
        return returnTable;
    }
    private int con = 1;

    public BufferedImage getImage() {
        return image;
    }

    public double[][] getFourierImageModules() {
        double[][] tab = new double[fourierImage.length][fourierImage[0].length];
        for (int x = 0; x < fourierImage.length; x++) {

            for (int y = 0; y < fourierImage[x].length; y++) {
                tab[x][y] = fourierImage[x][y].abs();
            }
        }

        return tab;
    }

    public double[][] getFourierImageArgument() {
        double[][] tab = new double[fourierImage.length][fourierImage[0].length];
        for (int x = 0; x < fourierImage.length; x++) {

            for (int y = 0; y < fourierImage[x].length; y++) {
                tab[x][y] = Math.atan2(fourierImage[x][y].getImaginary(), fourierImage[x][y].getReal());
            }
        }

        return tab;
    }

    /**
     * Zwraca nam tablicę z wartościami pikseli obrazka
     *
     * @return
     */
    public BufferedImage getInverseFourierImage() {
        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, image.getType());
        double[][] tab = new double[fourierImage.length][fourierImage[0].length];
        for (int x = 0; x < fourierImage.length; x++) {
            for (int y = 0; y < fourierImage[x].length; y++) {
                tab[x][y] = fourierImage[x][y].getReal();
            }
        }
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int[] t = new int[1];
//                System.out.println("ZESP: "+inverseImage[x][y]);
                double value = tab[x][y];
//                double value = (Math.log(inverseImage[x][y]+1.0))*(255.0/Math.log(max + 1));
//                System.out.println("VALUE: "+value);
                int v;
                if (value > 255) {
                    v = 255;
                } else if (value < 0) {
                    v = 0;
                } else {
                    v = (int) value;
                }
//                System.out.println("V: "+v);
                t[0] = v;
                bufferedImage.getRaster().setPixel(x, y, t);
            }
        }

        return bufferedImage;
    }

    public BufferedImage getFourierPowerSpectrum() {
        double[][] tab = getFourierImageModules();
        fourierImageForDisplaying = new BufferedImage(imageWidth, imageHeight, image.getType());
        double max = 0;
        for (int x = 0; x < fourierImageForDisplaying.getWidth(); x++) {
            for (int y = 0; y < fourierImageForDisplaying.getHeight(); y++) {
                if (tab[x][y] > max) {
                    max = tab[x][y];
                }
            }
        }

        for (int x = 0; x < fourierImageForDisplaying.getWidth(); x++) {
            for (int y = 0; y < fourierImageForDisplaying.getHeight(); y++) {
                int[] t = new int[1];
//                System.out.println("ZESP: "+tab[x][y]);
//                double value = (Math.log(tab[x][y]+1.0))*20;
//                double value = tab[x][y];
                double value = (Math.log(tab[x][y] + 1.0)) * (255.0 / Math.log(max + 1));
//                System.out.println("VALUE: "+value + " MAX "+max);
                int v;
                if (value > 255) {
                    v = 255;
                } else if (value < 0) {
                    v = 0;
                } else {
                    v = (int) value;
                }
//                System.out.println("V: "+v);
                t[0] = v;
                fourierImageForDisplaying.getRaster().setPixel(x, y, t);
            }
        }
        return fourierImageForDisplaying;
    }

    public BufferedImage getFourierPhaseSpectrum() {
        double[][] tab = getFourierImageArgument();
        fourierImageForDisplaying = new BufferedImage(imageWidth, imageHeight, image.getType());
        double max = 0;
        for (int x = 0; x < fourierImageForDisplaying.getWidth(); x++) {
            for (int y = 0; y < fourierImageForDisplaying.getHeight(); y++) {
                if (tab[x][y] > max) {
                    max = tab[x][y];
                }
            }
        }

        for (int x = 0; x < fourierImageForDisplaying.getWidth(); x++) {
            for (int y = 0; y < fourierImageForDisplaying.getHeight(); y++) {
                int[] t = new int[1];
//                System.out.println("ZESP: "+tab[x][y]);
//                double value = (Math.log(tab[x][y]+1.0))*20;
//                double value = tab[x][y];
                double value = (Math.log(tab[x][y] + 1.0)) * (255.0 / Math.log(max + 1));
//                System.out.println("VALUE: "+value + " MAX "+max);
                int v;
                if (value > 255) {
                    v = 255;
                } else if (value < 0) {
                    v = 0;
                } else {
                    v = (int) value;
                }
//                System.out.println("V: "+v);
                t[0] = v;
                fourierImageForDisplaying.getRaster().setPixel(x, y, t);
            }
        }
        return changeImageQuadrants(fourierImageForDisplaying);
    }

    public static Complex[][] changeImageQuadrants(Complex[][] tab) {
        int w = tab.length;
        int h = tab[0].length;
        //Tutaj zamieniamy miejscami kolejne ćwiartki
        Complex[][] quarters = new Complex[w][h];
        Complex t;
        //Ćwiartka pierwsza w miejsce czwartej
        for (int x = w / 2; x < w; x++) {
            for (int y = h / 2; y < h; y++) {
                t = tab[x - (w / 2)][y - (h / 2)];
                quarters[x][y] = t;
            }
        }

        //Ćwiartka czwarta w miejsce pierwszej
        for (int x = 0; x < w / 2; x++) {
            for (int y = 0; y < h / 2; y++) {
                t = tab[x + (w / 2)][y + (h / 2)];
                quarters[x][y] = t;
            }
        }

        //Ćwiartka druga w miejsce trzeciej
        for (int x = 0; x < w / 2; x++) {
            for (int y = h / 2; y < h; y++) {
                t = tab[x + (w / 2)][y - (h / 2)];
                quarters[x][y] = t;
            }
        }

        //Ćwiartka trzecia w miejsce drugiej
        for (int x = w / 2; x < w; x++) {
            for (int y = 0; y < h / 2; y++) {
                t = tab[x - (w / 2)][y + (h / 2)];
                quarters[x][y] = t;
            }
        }

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                t = quarters[x][y];
                tab[x][y] = t;
            }
        }

        return tab;
    }

    public static BufferedImage changeImageQuadrants(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        //Tutaj zamieniamy miejscami kolejne ćwiartki
        int[][] quarters = new int[w][h];
        //Ćwiartka pierwsza w miejsce czwartej
        for (int x = w / 2; x < w; x++) {
            for (int y = h / 2; y < h; y++) {
                int[] t = new int[1];
                t = image.getRaster().getPixel(x - (w / 2), y - (h / 2), t);
                quarters[x][y] = t[0];
            }
        }

        //Ćwiartka czwarta w miejsce pierwszej
        for (int x = 0; x < w / 2; x++) {
            for (int y = 0; y < h / 2; y++) {
                int[] t = new int[1];
                t = image.getRaster().getPixel(x + (w / 2), y + (h / 2), t);
                quarters[x][y] = t[0];
            }
        }

        //Ćwiartka druga w miejsce trzeciej
        for (int x = 0; x < w / 2; x++) {
            for (int y = h / 2; y < h; y++) {
                int[] t = new int[1];
                t = image.getRaster().getPixel(x + (w / 2), y - (h / 2), t);
                quarters[x][y] = t[0];
            }
        }

        //Ćwiartka trzecia w miejsce drugiej
        for (int x = w / 2; x < w; x++) {
            for (int y = 0; y < h / 2; y++) {
                int[] t = new int[1];
                t = image.getRaster().getPixel(x - (w / 2), y + (h / 2), t);
                quarters[x][y] = t[0];
            }
        }

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int[] t = new int[1];
                t[0] = quarters[x][y];
                image.getRaster().setPixel(x, y, t);
            }
        }

        return image;
    }

    private static double computeDistance(int x1, int y1, int x2, int y2) {
        double result = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
        return Math.sqrt(result);
    }
}
