package impl;

import interfaces.EdgeDetector;
import models.RgbPixel;

import java.util.HashSet;
import java.util.List;

public class CannyEdgeDetector implements EdgeDetector {

    private static final int[][] GAUSSIAN_KERNEL = {
            {1 / 256, 4 / 256, 6 / 256, 4 / 256, 1 / 256},
            {4 / 256, 16 / 256, 24 / 256, 16 / 256, 4 / 256},
            {6 / 256, 24 / 256, 36 / 256, 24 / 256, 6 / 256},
            {4 / 256, 16 / 256, 24 / 256, 16 / 256, 4 / 256},
            {1 / 256, 4 / 256, 6 / 256, 4 / 256, 1 / 256}
    };

    private static final int[][] PIXEL_EDGES = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

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
        var offset = GAUSSIAN_KERNEL.length / 2;
        var blurred = new double[width][height];
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                double acc = 0;
                for (int i = 0; i <= GAUSSIAN_KERNEL.length; i++) {
                    for (int j = 0; j <= GAUSSIAN_KERNEL.length; j++) {
                        var xn = clampClip(x + i - offset, 0, width - 1);
                        var yn = clampClip(y + j - offset, 0, height - 1);
                        acc += (inputPixels[xn][yn]) * GAUSSIAN_KERNEL[i][j];
                    }
                    blurred[x][y] = acc;
                }
            }
        }
        return blurred;
    }

    //TODO: bad idea to do a 3d matrix, tuple would be a better fit
    private double[][][] computeGradient(int[][] inputPixels, int height, int width) {
        var gradient = new double[width][height];
        var direction = new double[width][height];
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                if ((x > 0 && x < width - 1) && (y > 0 && y < height - 1)) {
                    var magx = inputPixels[x + 1][y] - inputPixels[x - 1][y];
                    var magy = inputPixels[x][y + 1] - inputPixels[x][y - 1];
                    gradient[x][y] = Math.sqrt(Math.pow(magx, 2) + Math.pow(magy, 2));
                    direction[x][y] = Math.atan2(magy, magx);

                }
            }
        }
        return new double[][][]{gradient, direction};
    }

    private double[][] filterOutNonMaximum(double[][] gradient, double[][] direction, int width, int height) {
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                var angle = direction[x][y] > 0 ? direction[x][y] : direction[x][y] + Math.PI;
                var rangle = Math.round(angle / (Math.PI / 4));
                var mag = gradient[x][y];
                if ((rangle == 0 || rangle == 4) && (gradient[x - 1][y] > mag || gradient[x + 1][y] > mag)
                        || (rangle == 1 && (gradient[x - 1][y - 1] > mag) || gradient[x + 1][y + 1] > mag)
                        || (rangle == 2 && (gradient[x][y - 1] > mag || gradient[x][y + 1] > mag))
                        || (rangle == 3 && gradient[x + 1][y - 1] > mag || gradient[x - 1][y + 1] > mag)
                ) {
                    gradient[x][y] = 0;
                }


            }
        }
        return gradient;

    }

    //TODO: fix differences between int and double arrays, maybe replace with generic List<?>
    private int[][] filterOutEdges(double[][] gradient, int width, int height, int low, int high) {
        var keptSet = new HashSet<double[]>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gradient[x][y] > high) {
                    keptSet.add(new double[]{x, y});
                }
                var lastIteration = keptSet;
                for (double[] values : lastIteration) {
                    var newKeptSet = new HashSet<double[]>();
                    for (int[] pixelEdge : PIXEL_EDGES) {
                        if (gradient[(int) values[0] + pixelEdge[0]][(int) values[1] + pixelEdge[1]] > low && !(keptSet.contains(new double[]{(int) values[0] + pixelEdge[0], (int) values[1] + pixelEdge[1]}))) {
                            newKeptSet.add(new double[]{(int) values[0] + pixelEdge[0], (int) values[1] + pixelEdge[1]});
                        }
                    }

                    keptSet.addAll(newKeptSet);
                    lastIteration = newKeptSet;
                }
            }
        }
        return (int[][]) keptSet.toArray();
    }


}
