package impl;

import interfaces.EdgeDetector;
import models.RgbPixel;

import java.util.List;

public class CannyEdgeDetector implements EdgeDetector {

    private static final int[][] gaussianKernel = {
            {1 / 256, 4 / 256, 6 / 256, 4 / 256, 1 / 256},
            {4 / 256, 16 / 256, 24 / 256, 16 / 256, 4 / 256},
            {6 / 256, 24 / 256, 36 / 256, 24 / 256, 6 / 256},
            {4 / 256, 16 / 256, 24 / 256, 16 / 256, 4 / 256},
            {1 / 256, 4 / 256, 6 / 256, 4 / 256, 1 / 256}
    };

    @Override
    public List<Integer> findEdges(RgbPixel[][] inputPixels, int height, int width) {
        return null;
    }

    private int clampClip(int x, int l, int u) {
        return Math.min(u, Math.max(l, x));
    }


    //TODO: should do an image processor instead, easier testing than reflection with private methods
    private int[][] computeGrayScale(RgbPixel[][] rgbRgbPixels) {

        var grayScaledPixels = new int[rgbRgbPixels.length][rgbRgbPixels[0].length];
        for (int i = 0; i < rgbRgbPixels.length; i++) {
            for (int j = 0; j < rgbRgbPixels[0].length; j++) {
                grayScaledPixels[i][j] = rgbRgbPixels[i][j].sumOfPixels() / 3;
            }
        }
        return grayScaledPixels;
    }

    private double[][] computeBlur(int[][] inputPixels, int height, int width) {
        var offset = gaussianKernel.length / 2;
        var blurred = new double[width][height];
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                double acc = 0;
                for (int i = 0; i <= gaussianKernel.length; i++) {
                    for (int j = 0; j <= gaussianKernel.length; j++) {
                        var xn = clampClip(x + i - offset, 0, width - 1);
                        var yn = clampClip(y + j - offset, 0, height - 1);
                        acc += (inputPixels[xn][yn]) * gaussianKernel[i][j];
                    }
                    blurred[x][y] =  acc;
                }
            }
        }
        return blurred;
    }

//TODO: bad idea to do a 3d matrix, tuple would be a better fit
    private double[][][] computeGradient(int[][] inputPixels, int height, int width) {
        var gradient = new double[width][height];
        var  direction = new double[width][height];
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                if ((x > 0 && x < width - 1) && (y > 0 && y < height - 1)) {
                    var magx = inputPixels[x + 1][y] - inputPixels[x - 1][y];
                    var magy = inputPixels[x][y + 1] - inputPixels[x][y - 1];
                    gradient[x][y] =  Math.sqrt(Math.pow(magx, 2) + Math.pow(magy, 2));
                    direction[x][y] =  Math.atan2(magy,magx);

                }
            }
        }
        return new double[][][]{gradient,direction};
    }

    private double[][] filterOutNonMaximum(double[][] gradient, double[][] direction,int width, int height) {
//        for(int x = 1;x<width;x++){
//            for(int y =1; y < height;y++){
//                var angle =  direction[x][y] > 0 ? direction[x][y] : direction[x][y] + Math.PI;
//                var rangle = Math.round(angle/(Math.PI/4));
//                var mag = gradient[x][y];
//
//            }
//        }
        throw new UnsupportedOperationException();

    }

    private int[][] filterOutEdges(RgbPixel[][] inputPixels, int height, int width) {
    throw new UnsupportedOperationException();
    }


}
