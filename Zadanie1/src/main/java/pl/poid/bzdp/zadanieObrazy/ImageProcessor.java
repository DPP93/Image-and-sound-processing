/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.poid.bzdp.zadanieObrazy;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Bartłomiej
 */
public class ImageProcessor {

    private BufferedImage originalImage;
    private BufferedImage copiedImage;
    private int imageWidth;
    private int imageHeight;
    private ImageType imageType;
    private double oneDivNine = 1.0 / 9.0;
    private double[][] firstMask = {
        {oneDivNine, oneDivNine, oneDivNine},
        {oneDivNine, oneDivNine, oneDivNine},
        {oneDivNine, oneDivNine, oneDivNine}
    };
    private double[][] secondMask = {
        {0.1, 0.1, 0.1},
        {0.1, 0.2, 0.1},
        {0.1, 0.1, 0.1}
    };
    private double[][] thirdMask = {
        {0.0625, 0.125, 0.0625},
        {0.125, 0.25, 0.125},
        {0.0625, 0.125, 0.0625}
    };
    private double[][] fourthMask = {
        {0, -1, 0},
        {-1, 5, -1},
        {0, -1, 0}
    };

    public ImageProcessor(BufferedImage image) {
        originalImage = image;
        copiedImage = copyImage(image);
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        int pixelSize = image.getColorModel().getPixelSize();
//        System.out.println("PIXEL SIZE " + pixelSize);
        switch (pixelSize) {
            case 1:
                imageType = ImageType.BINARY;
                break;
            case 8:
                imageType = ImageType.GRAYSCALE;
                break;
            case 24:
            default:
                imageType = ImageType.COLOR;
        }
    }

    public static BufferedImage copyImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     *
     * @param originalImage obraz oryginalny BEZ szumu
     * @param reducedImage obraz otrzymany w wyniku odszumiania
     * @return
     */
    public static double computeMSE(BufferedImage originalImage, BufferedImage reducedImage) {
        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

        double mse = 0;
        if (reducedImage.getColorModel().getPixelSize() == 24) {
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    double r = 0, g = 0, b = 0;
                    Color origColor = new Color(originalImage.getRGB(x, y));
                    Color reducedColor = new Color(reducedImage.getRGB(x, y));
                    r = Math.pow(origColor.getRed() - reducedColor.getRed(), 2.0);
                    g = Math.pow(origColor.getGreen() - reducedColor.getGreen(), 2.0);
                    b = Math.pow(origColor.getBlue() - reducedColor.getBlue(), 2.0);
                    mse += r + g + b;
                }
            }
            mse /= (double) (imageHeight * imageWidth);
        }else{
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    double g = 0;
                    Color origColor = new Color(originalImage.getRGB(x, y));
                    Color reducedColor = new Color(reducedImage.getRGB(x, y));
                    g = Math.pow(origColor.getRed() - reducedColor.getRed(), 2.0);
                    mse += g;
                }
            }
            mse /= (double) (imageHeight * imageWidth);
        }
        return mse;
    }
    
    
    public static double computeMAE(BufferedImage originalImage, BufferedImage reducedImage) {
        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

        double mae = 0;
        if (reducedImage.getColorModel().getPixelSize() == 24) {
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    double r = 0, g = 0, b = 0;
                    Color origColor = new Color(originalImage.getRGB(x, y));
                    Color reducedColor = new Color(reducedImage.getRGB(x, y));
                    r = Math.abs(origColor.getRed() - reducedColor.getRed());
                    g = Math.abs(origColor.getGreen() - reducedColor.getGreen());
                    b = Math.abs(origColor.getBlue() - reducedColor.getBlue());
                    mae += r + g + b;
                }
            }
            mae /= (double) (imageHeight * imageWidth);
        }else{
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    double g = 0;
                    Color origColor = new Color(originalImage.getRGB(x, y));
                    Color reducedColor = new Color(reducedImage.getRGB(x, y));
                    g = Math.abs(origColor.getRed() - reducedColor.getRed());
                    mae += g;
                }
            }
            mae /= (double) (imageHeight * imageWidth);
        }

        

        return mae;
    }
    public boolean writeImage(String fileType, String filename) {
        try {
            ImageIO.write(copiedImage, fileType, new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Ustawia oryginalny obraz na nowo, do tego kopiuje go, do copiedImage
     *
     * @param originalImage
     */
    public void setOriginalImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
        copiedImage = copyImage(originalImage);
        int pixelSize = originalImage.getColorModel().getPixelSize();
//        System.out.println("PIXEL SIZE " + pixelSize);
        switch (pixelSize) {
            case 1:
                imageType = ImageType.BINARY;
                break;
            case 8:
                imageType = ImageType.GRAYSCALE;
                break;
            case 24:
            default:
                imageType = ImageType.COLOR;
        }
    }

    /**
     * Zmienia jasność obrazu o zadaną przez użytkownika wartość
     *
     * @param value
     */
    public void changeBrightness(int value) {

        if (imageType != ImageType.BINARY) {
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {

                    Color c = new Color(copiedImage.getRGB(x, y));

                    int red = changeColor(c.getRed(), value);
                    int green = changeColor(c.getGreen(), value);
                    int blue = changeColor(c.getBlue(), value);
                    c = new Color(red, green, blue);
                    copiedImage.setRGB(x, y, c.getRGB());
                }
            }
        }
    }

    public void makeNegative() {
        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {

                Color c = new Color(copiedImage.getRGB(x, y));
                int red = 255 - c.getRed();
                int green = 255 - c.getGreen();
                int blue = 255 - c.getBlue();
                c = new Color(red, green, blue);
                copiedImage.setRGB(x, y, c.getRGB());

            }
        }
    }

    public BufferedImage getCopiedImage() {
        return copiedImage;
    }

    public void changeContrast(double contrast) {
        if (imageType != ImageType.BINARY) {
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {

                    Color c = new Color(copiedImage.getRGB(x, y));

                    int red = changeContrast(c.getRed(), contrast);
                    int green = changeContrast(c.getGreen(), contrast);
                    int blue = changeContrast(c.getBlue(), contrast);
                    c = new Color(red, green, blue);
                    copiedImage.setRGB(x, y, c.getRGB());
                }
            }
        }
    }

    public void reduceNoiseWithAverage(int maskSize) {
        int maskOffset = maskSize / 2;
        int[][] newValues = new int[imageWidth][imageHeight];
        if (imageType == ImageType.GRAYSCALE) {
            for (int x = maskOffset; x < imageWidth - maskOffset; x++) {
                for (int y = maskOffset; y < imageHeight - maskOffset; y++) {
                    //Weź 

                    double average = 0;
                    for (int pixelX = x - maskOffset; pixelX <= x + maskOffset; pixelX++) {
                        for (int pixelY = y - maskOffset; pixelY <= y + maskOffset; pixelY++) {
                            Color c = new Color(copiedImage.getRGB(pixelX, pixelY));
                            //Jako że to szary obrazek każda wartość 
                            average += c.getBlue();
                        }
                    }
                    //Obliczamy średnią arytmetyczną
                    average /= Math.pow(maskSize, 2);
                    if (average > 255) {
                        average = 255;
                    }
//                    System.out.println(average);
                    Color c = new Color((int) average, (int) average, (int) average);
                    newValues[x][y] = c.getRGB();
                }
            }
        } else if (imageType == ImageType.COLOR) {
            for (int x = maskOffset; x < imageWidth - maskOffset; x++) {
                for (int y = maskOffset; y < imageHeight - maskOffset; y++) {
                    //Weź 

                    double averageR = 0;
                    double averageG = 0;
                    double averageB = 0;
                    for (int pixelX = x - maskOffset; pixelX <= x + maskOffset; pixelX++) {
                        for (int pixelY = y - maskOffset; pixelY <= y + maskOffset; pixelY++) {
                            Color c = new Color(copiedImage.getRGB(pixelX, pixelY));
                            //Jako że to szary obrazek każda wartość 
                            averageR += c.getRed();
                            averageG += c.getGreen();
                            averageB += c.getBlue();
                        }
                    }
                    //Obliczamy średnią arytmetyczną
                    averageR /= (maskSize * maskSize);
                    averageG /= (maskSize * maskSize);
                    averageB /= (maskSize * maskSize);

                    Color c = new Color((int) averageR, (int) averageG, (int) averageB);
                    newValues[x][y] = c.getRGB();
                }
            }
        }

        for (int x = maskOffset; x < imageWidth - maskOffset; x++) {
            for (int y = maskOffset; y < imageHeight - maskOffset; y++) {
                copiedImage.setRGB(x, y, newValues[x][y]);
            }
        }

    }

    public void reduceNoiseWithMedian(int maskSize) {
        int maskOffset = maskSize / 2;
        int[][] newValues = new int[imageWidth][imageHeight];
        if (imageType == ImageType.GRAYSCALE) {
            for (int x = maskOffset; x < imageWidth - maskOffset; x++) {
                for (int y = maskOffset; y < imageHeight - maskOffset; y++) {

                    int[] mediana = new int[maskSize * maskSize];
                    int counter = 0;
                    int average = 0;
                    for (int pixelX = x - maskOffset; pixelX <= x + maskOffset; pixelX++) {
                        for (int pixelY = y - maskOffset; pixelY <= y + maskOffset; pixelY++) {
                            Color c = new Color(copiedImage.getRGB(pixelX, pixelY));
                            //Jako że to szary obrazek każda wartość 
                            mediana[counter] = c.getBlue();
                            counter++;
                        }
                    }

                    Arrays.sort(mediana);
                    average = mediana[(maskSize * maskSize) / 2];
                    Color c = new Color(average, average, average);
                    newValues[x][y] = c.getRGB();
                }
            }
        } else if (imageType == ImageType.COLOR) {
            for (int x = maskOffset; x < imageWidth - maskOffset; x++) {
                for (int y = maskOffset; y < imageHeight - maskOffset; y++) {
                    //Weź 

                    int[] medianaR = new int[maskSize * maskSize];
                    int[] medianaG = new int[maskSize * maskSize];
                    int[] medianaB = new int[maskSize * maskSize];
                    int counter = 0;

                    for (int pixelX = x - maskOffset; pixelX <= x + maskOffset; pixelX++) {
                        for (int pixelY = y - maskOffset; pixelY <= y + maskOffset; pixelY++) {
                            Color c = new Color(copiedImage.getRGB(pixelX, pixelY));
                            //Jako że to szary obrazek każda wartość 
                            medianaR[counter] = c.getRed();
                            medianaG[counter] = c.getGreen();
                            medianaB[counter] = c.getBlue();
                            counter++;
                        }
                    }

                    Arrays.sort(medianaR);
                    Arrays.sort(medianaG);
                    Arrays.sort(medianaB);
                    Color c = new Color(medianaR[(maskSize * maskSize) / 2], medianaG[(maskSize * maskSize) / 2], medianaB[(maskSize * maskSize) / 2]);
                    newValues[x][y] = c.getRGB();
                }
            }
        }

        for (int x = maskOffset; x < imageWidth - maskOffset; x++) {
            for (int y = maskOffset; y < imageHeight - maskOffset; y++) {
                copiedImage.setRGB(x, y, newValues[x][y]);
            }
        }
    }

    public void computeHistogram() {
        if (imageType == ImageType.COLOR) {
            //Kolorowy
            Map<String, int[]> colorValues = getColorImageHistogramValues();
            int counter = 0;
            for (Map.Entry<String, int[]> entry : colorValues.entrySet()) {
                XYSeriesCollection dataset = new XYSeriesCollection();
                XYSeries series = new XYSeries(entry.getKey());
                for (int i = 0; i < entry.getValue().length; i++) {
                    series.add(i, entry.getValue()[i]);
                }
                dataset.addSeries(series);
                Color c;
//                System.out.println(counter);
                switch (counter) {
                    case 0:
                        c = Color.RED;
                        break;
                    case 1:
                        c = Color.GREEN;
                        break;
                    case 2:
                        c = Color.BLUE;
                        break;
                    case 3:
                        c = Color.BLACK;
                        break;
                    default:
                        c = Color.RED;
                }
                ImageHistogram.showHistogram(entry.getKey() + " histogram", dataset, c);
                counter++;
            }

        } else {
            //Skala szarości
            int[] grayValues = getGrayscaleImageHistogramValues();
            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries series = new XYSeries("Grayscale");
            for (int i = 0; i < grayValues.length; i++) {
                series.add(i, grayValues[i]);
            }
            dataset.addSeries(series);
            ImageHistogram.showHistogram("Grayscale histogram", dataset, Color.GRAY);

        }
    }

    /**
     * Przyjmujemy, że będą to tylko obrazy czarno-białe
     *
     * @param gMin
     * @param alpha
     */
    public void changeDensityProbabilty(int gMin, double alpha) {
        int[] histogram = getGrayscaleImageHistogramValues();
        int[] newValues = new int[histogram.length];
        for (int f = 0; f < newValues.length; f++) {
            double sum = 0;
            for (int m = 0; m <= f; m++) {
                sum += histogram[m];
            }

            double newValue = Math.log(1.0 - (sum / (double) (imageHeight * imageWidth)));
            if (Double.isNaN(newValue)) {
                newValue = Double.MIN_EXPONENT;
            } else if (Double.isInfinite(newValue)) {
                newValue = Double.MAX_EXPONENT;
            }
            newValue = newValue / alpha;
            newValue = gMin - newValue;
//            System.out.println(newValue);
            newValues[f] = normalizeColor((int) newValue);
        }

        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                Color c = new Color(copiedImage.getRGB(x, y));
                int colorTab[] = new int[1]; 
                colorTab = copiedImage.getRaster().getPixel(x, y, colorTab);
                int color = colorTab[0];
                color = newValues[color];
                int[] tab = new int[1];
                tab[0] = color;
                copiedImage.getRaster().setPixel(x, y, tab);
//                copiedImage.setRGB(x, y, new Color(color, color, color).getRGB());
            }
        }
    }

    public void linearFiltration(int maskType) {

        double[][] mask;
        switch (maskType) {
            case 0:
                mask = firstMask;
                break;
            case 1:
                mask = secondMask;
                break;
            case 2:
                mask = thirdMask;
                break;
            case 3:
                mask = fourthMask;
                break;
            default:
                mask = firstMask;
        }
        int[][] newValues = new int[imageWidth][imageHeight];
        if (imageType == ImageType.COLOR) {
            for (int x = 1; x < imageWidth - 1; x++) {
                for (int y = 1; y < imageHeight - 1; y++) {

                    double valueR = 0, valueG = 0, valueB = 0;
                    for (int i = 0; i < mask.length; i++) {
                        for (int j = 0; j < mask[i].length; j++) {
                            //Tutaj ogarniam wartości dla poszczegolnych przesunięć w splocie
                            int offsetX = 0, offsetY = 0;
                            if (i == 0) {
                                offsetX = -1;
                            } else if (i == 1) {
                                offsetX = 1;
                            }
                            if (j == 0) {
                                offsetY = -1;
                            } else if (j == 1) {
                                offsetY = 1;
                            }

                            //Tutaj właściwe obliczenie
                            Color c = new Color(copiedImage.getRGB(x + offsetX, y + offsetY));
                            valueR += mask[i][j] * c.getRed();
                            valueG += mask[i][j] * c.getGreen();
                            valueB += mask[i][j] * c.getBlue();
                        }
                    }

                    Color c = new Color(normalizeColor((int) valueR), normalizeColor((int) valueG), normalizeColor((int) valueB));
                    newValues[x][y] = c.getRGB();
                }
            }
        } else {
            for (int x = 1; x < imageWidth - 1; x++) {
                for (int y = 1; y < imageHeight - 1; y++) {

                    double value = 0;
                    for (int i = 0; i < mask.length; i++) {
                        for (int j = 0; j < mask[i].length; j++) {
                            //Tutaj ogarniam wartości dla poszczegolnych przesunięć w splocie
                            int offsetX = 0, offsetY = 0;
                            if (i == 0) {
                                offsetX = -1;
                            } else if (i == 1) {
                                offsetX = 1;
                            }
                            if (j == 0) {
                                offsetY = -1;
                            } else if (j == 1) {
                                offsetY = 1;
                            }

                            //Tutaj właściwe obliczenie
                            Color c = new Color(copiedImage.getRGB(x + offsetX, y + offsetY));
                            value += mask[i][j] * c.getRed();
                        }
                    }

                    Color c = new Color(normalizeColor((int) value), normalizeColor((int) value), normalizeColor((int) value));
                    newValues[x][y] = c.getRGB();
                }
            }

        }

        for (int x = 1; x < imageWidth - 1; x++) {
            for (int y = 1; y < imageHeight - 1; y++) {
                copiedImage.setRGB(x, y, newValues[x][y]);
            }
        }

    }

    public void nonLinearFiltration() {
        //TODO: Zamienić ich kolejność
        int[][] newImage = new int[imageWidth][imageHeight];

        newImage[0][0] = copiedImage.getRGB(0, 0);
        newImage[0][imageWidth - 1] = copiedImage.getRGB(0, imageWidth - 1);
        newImage[imageHeight - 1][0] = copiedImage.getRGB(imageHeight - 1, 0);
        newImage[imageHeight - 1][imageWidth - 1] = copiedImage.getRGB(imageHeight - 1, imageWidth - 1);

        if (imageType == ImageType.COLOR) {
            for (int x = 1; x < imageWidth - 1; x++) {
                for (int y = 1; y < imageHeight - 1; y++) {
                    //Pewnie można to ładniej zrobić, ale jest piątek rano i mi się nie chce
                    Color c0 = new Color(copiedImage.getRGB(x - 1, y - 1));
                    Color c1 = new Color(copiedImage.getRGB(x, y - 1));
                    Color c2 = new Color(copiedImage.getRGB(x + 1, y - 1));
                    Color c3 = new Color(copiedImage.getRGB(x + 1, y));
                    Color c4 = new Color(copiedImage.getRGB(x + 1, y + 1));
                    Color c5 = new Color(copiedImage.getRGB(x, y + 1));
                    Color c6 = new Color(copiedImage.getRGB(x - 1, y + 1));
                    Color c7 = new Color(copiedImage.getRGB(x - 1, y));

                    int aR0 = c0.getRed(),
                            aR1 = c1.getRed(),
                            aR2 = c2.getRed(),
                            aR3 = c3.getRed(),
                            aR4 = c4.getRed(),
                            aR5 = c5.getRed(),
                            aR6 = c6.getRed(),
                            aR7 = c7.getRed();
                    int aG0 = c0.getGreen(),
                            aG1 = c1.getGreen(),
                            aG2 = c2.getGreen(),
                            aG3 = c3.getGreen(),
                            aG4 = c4.getGreen(),
                            aG5 = c5.getGreen(),
                            aG6 = c6.getGreen(),
                            aG7 = c7.getGreen();
                    int aB0 = c0.getBlue(),
                            aB1 = c1.getBlue(),
                            aB2 = c2.getBlue(),
                            aB3 = c3.getBlue(),
                            aB4 = c4.getBlue(),
                            aB5 = c5.getBlue(),
                            aB6 = c6.getBlue(),
                            aB7 = c7.getBlue();

                    int sRX, sRY, sGX, sGY, sBX, sBY;

                    sRX = (aR2 + (2 * aR3) + aR4) - (aR0 + (2 * aR7) + aR6);
                    sRY = (aR0 + (2 * aR1) + aR2) - (aR6 + (2 * aR5) + aR4);

                    sGX = (aG2 + (2 * aG3) + aG4) - (aG0 + (2 * aG7) + aG6);
                    sGY = (aG0 + (2 * aG1) + aG2) - (aG6 + (2 * aG5) + aG4);

                    sBX = (aB2 + (2 * aB3) + aB4) - (aB0 + (2 * aB7) + aB6);
                    sBY = (aB0 + (2 * aB1) + aB2) - (aB6 + (2 * aB5) + aB4);

                    newImage[x][y] = new Color(sobelOperator(sRX, sRY), sobelOperator(sGX, sGY), sobelOperator(sBX, sBY)).getRGB();
                }
            }

        } else {
            for (int x = 1; x < imageWidth - 1; x++) {
                for (int y = 1; y < imageHeight - 1; y++) {
                    //Pewnie można to ładniej zrobić, ale jest piątek rano i mi się nie chce
                    Color c0 = new Color(copiedImage.getRGB(x - 1, y - 1));
                    Color c1 = new Color(copiedImage.getRGB(x, y - 1));
                    Color c2 = new Color(copiedImage.getRGB(x + 1, y - 1));
                    Color c3 = new Color(copiedImage.getRGB(x + 1, y));
                    Color c4 = new Color(copiedImage.getRGB(x + 1, y + 1));
                    Color c5 = new Color(copiedImage.getRGB(x, y + 1));
                    Color c6 = new Color(copiedImage.getRGB(x - 1, y + 1));
                    Color c7 = new Color(copiedImage.getRGB(x - 1, y));

                    int aR0 = c0.getRed(),
                            aR1 = c1.getRed(),
                            aR2 = c2.getRed(),
                            aR3 = c3.getRed(),
                            aR4 = c4.getRed(),
                            aR5 = c5.getRed(),
                            aR6 = c6.getRed(),
                            aR7 = c7.getRed();

                    int sRX, sRY;

                    sRX = (aR2 + (2 * aR3) + aR4) - (aR0 + (2 * aR7) + aR6);
                    sRY = (aR0 + (2 * aR1) + aR2) - (aR6 + (2 * aR5) + aR4);

                    newImage[x][y] = new Color(sobelOperator(sRX, sRY), sobelOperator(sRX, sRY), sobelOperator(sRX, sRY)).getRGB();
                }
            }
        }

        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                copiedImage.setRGB(x, y, newImage[x][y]);
            }
        }
    }

    private int sobelOperator(int sx, int sy) {
        return normalizeColor((int) (Math.sqrt(Math.pow(sx, 2) + Math.pow(sy, 2))));
    }

    private Map<String, int[]> getColorImageHistogramValues() {
        CategoryDataset categoryDataset;
        // row keys...
        final String redSeries = "Red";
        final String greenSeries = "Green";
        final String blueSeries = "Blue";
        final String lumine = "Luminosity";
        int[] redValues = new int[256];
        int[] greenValues = new int[256];
        int[] blueValues = new int[256];
        int[] luminosityValues = new int[256];
        Map<String, int[]> values = Maps.newLinkedHashMap();

        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                Color c = new Color(copiedImage.getRGB(x, y));
                int[] tab = new int[3];
                tab = copiedImage.getRaster().getPixel(x, y, tab);
                redValues[tab[0]] += 1;
                greenValues[tab[1]] += 1;
                blueValues[tab[2]] += 1;
                double luminosity = (0.299 * c.getRed()) + (0.587 * c.getGreen()) + (0.144 * c.getBlue());
                if(luminosity > 255){
                    luminosity = 255;
                }
//                System.out.println("Lumine "+luminosity);
                luminosityValues[(int)luminosity] += 1;
            }
        }
        
        values.put(redSeries, redValues);
        values.put(greenSeries, greenValues);
        values.put(blueSeries, blueValues);
        values.put(lumine, luminosityValues);
        return values;
    }

    private int[] getGrayscaleImageHistogramValues() {
        CategoryDataset categoryDataset;
        // row keys...
        int[] grayValues = new int[256];
        Map<String, int[]> values = Maps.newLinkedHashMap();

        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                Color c = new Color(copiedImage.getRGB(x, y));
                int[] tab = new int[1];
                tab = copiedImage.getRaster().getPixel(x, y, tab);
//                grayValues[c.getRed()] += 1;
                grayValues[tab[0]] += 1;
            }
        }
        return grayValues;
    }

    private int[] channelHistogram(boolean isColor, Color c) {
        int[] histogram = new int[256];

        return histogram;
    }

    private int changeColor(int colorValue, int value) {
        if ((colorValue + value) > 255) {
            return 255;
        } else if ((colorValue + value < 0)) {
            return 0;
        } else {
            return colorValue + value;
        }
    }

    private int changeContrast(int colorValue, double contrastParameter) {
        int contrastColor = (int) (contrastParameter * (colorValue - (255 / 2)) + (255 / 2));
        if (contrastColor <= 255 && contrastColor >= 0) {
            return contrastColor;
        } else if (contrastColor > 255) {
            return 255;
        } else {
            return 0;
        }
    }

    private int normalizeColor(int color) {
        if (color > 255) {
            return 255;
        } else if (color < 0) {
            return 0;
        } else {
            return color;
        }
    }
}
