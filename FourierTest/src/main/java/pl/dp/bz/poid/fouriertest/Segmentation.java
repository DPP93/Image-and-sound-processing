/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.bz.poid.fouriertest;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Daniel
 */
public class Segmentation {

    private BufferedImage image;

    private int imageWidth;
    private int imageHeight;

    private SegmentationFlag[][] segmentationMask;

    private double edge = 30;

    private static double[] maxImageValue = {255};
    private static double[] minImageValue = {0};
    public Segmentation(BufferedImage image) {
        this.image = image;

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();
        this.segmentationMask = new SegmentationFlag[imageHeight][imageWidth];
        for (int x = 0; x < imageHeight; x++) {
            for (int y = 0; y < imageWidth; y++) {
                segmentationMask[x][y] = SegmentationFlag.UNVISITED_SEGMENTATION;
            }
        }
    }

    public double getEdge() {
        return edge;
    }

    public void setEdge(double edge) {
        this.edge = edge;
    }

    public BufferedImage getImage() {
        for (int x = 0; x < imageHeight; x++) {
            for (int y = 0; y < imageWidth; y++) {
                if (segmentationMask[x][y] == SegmentationFlag.IN_SEGMENTATION) {
                    image.getRaster().setPixel(x, y, maxImageValue);
                }
            }
        }
        return image;
    }

    public void computeSegmentation(int startX, int startY) {

        if (startX < imageHeight && startY < imageWidth && startX >= 0 && startY >= 0) {
            int counter = 0;

            segmentationMask[startX][startY] = SegmentationFlag.IN_SEGMENTATION;
            double average = getPixelValue(startY, startY);
            boolean isNotGettingBigger = false;
            while (true) {
                isNotGettingBigger = true;
                for (int x = 1; x < imageHeight - 1; x++) {
                    for (int y = 1; y < imageWidth - 1; y++) {
                        if (isInNeighbourhoodOfSegment(x, y)) {
                            for (int i = x - 1; i < x + 1; i++) {
                                for (int j = y - 1; j < y + 1; j++) {
                                    {
                                        //Nie testuj wartości, która już jest oznaczona
                                        if (i == x && j == y || segmentationMask[i][j] == SegmentationFlag.IN_SEGMENTATION) {
                                            continue;
                                        }
                                        double avg = computeAverageFromNeighbours(i, j);
//                                        System.out.println("Avg "+avg +" : "+average);
                                        if (avg >= average - edge && avg <= average + edge) {
                                            segmentationMask[i][j] = SegmentationFlag.IN_SEGMENTATION;
                                            average = computeSegmentationAverage();
                                            isNotGettingBigger = false;
                                        } else {
                                            segmentationMask[i][j] = SegmentationFlag.OUT_SEGMENTATION;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
//                System.out.println(counter);
                counter++;
                if (isNotGettingBigger) {
                    break;
                }
                if (counter == 5000) {
                    System.out.println("SHAME ON YOU");
                    break;
                }
            }

        }
    }

    public void computeSegmentationInFasterWay(int startX, int startY) {
        if (startX < imageHeight && startY < imageWidth && startX >= 0 && startY >= 0) {
            int counter = 0;

            segmentationMask[startX][startY] = SegmentationFlag.IN_SEGMENTATION;
            boolean isNotGettingBigger = false;
            double sum = getPixelValue(startY, startY);
            double markedElements = 1;

            while (true) {
                isNotGettingBigger = true;
                for (int x = 1; x < imageHeight - 1; x++) {
                    for (int y = 1; y < imageWidth - 1; y++) {
                        if (isInNeighbourhoodOfSegment(x, y)) {
                            double val = getPixelValue(x, y);
                            double average = sum / markedElements;
                            if (val >= average - edge && val <= average + edge) {
//                                System.out.println("X "+x +" Y "+y);
                                segmentationMask[x][y] = SegmentationFlag.IN_SEGMENTATION;
                                isNotGettingBigger = false;
                                sum += val;
                                markedElements++;
                            } else {
                                segmentationMask[x][y] = SegmentationFlag.OUT_SEGMENTATION;
                            }
                        }
                    }
                }
//                System.out.println(counter);
                counter++;
                if (isNotGettingBigger) {
                    break;
                }
                if (counter == 5000) {
                    System.out.println("SHAME ON YOU");
                    break;
                }
            }

        }
    }

    private boolean isInNeighbourhoodOfSegment(int centerX, int centerY) {
        if (segmentationMask[centerX][centerY] == SegmentationFlag.UNVISITED_SEGMENTATION) {
            for (int x = centerX - 1; x <= centerX + 1; x++) {
                for (int y = centerY - 1; y <= centerY + 1; y++) {
                    if (x == centerX && y == centerY) {
                        continue;
                    }
                    if (segmentationMask[x][y] == SegmentationFlag.IN_SEGMENTATION) {
//                        System.out.println("For "+centerX+":"+centerY+" found "+x+" : "+y);
                        return true;
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }

    private double computeSegmentationAverage() {
        double result = 0;
        double coutner = 0;
        for (int x = 0; x < imageHeight; x++) {
            for (int y = 0; y < imageWidth; y++) {
                if (segmentationMask[x][y] == SegmentationFlag.IN_SEGMENTATION) {
                    result += getPixelValue(x, y);
                    coutner++;
                }

            }
        }
        return result / coutner;
    }

    private double getPixelValue(int x, int y) {
        double[] t = new double[1];
        return image.getRaster().getPixel(x, y, t)[0];
    }

    private double computeAverageFromNeighbours(int startX, int startY) {
        double result = 0;
        double coutner = 0;
        for (int x = startX - 1; x < startX + 1; x++) {
            for (int y = startY - 1; y < startY + 1; y++) {
                if (isInImage(x, y)) {
                    result += getPixelValue(x, y);
                    coutner++;
                }

            }
        }
        return result / coutner;
    }

    private boolean isInImage(int x, int y) {
        return x >= 0 && x <= imageHeight && y >= 0 && y <= imageWidth;
    }
}
