package impl;

import dagger.Component;
import interfaces.EdgeDetector;
import models.RgbPixel;

import javax.inject.Inject;
import javax.inject.Singleton;
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

    private static final int IMAGE_LOWER_EDGE = 0;

    private static final int LOWER_EDGE_BOUND = 25;

    private static final int HIGHER_EDGE_BOUND = 40;

    @Inject
    public CannyEdgeDetector() {

    }

    @Override
    public double[][] findEdges(RgbPixel[][] inputPixels, int height, int width) {
        // check into offloading this somewhere else
        var grayScaled = computeGrayScale(inputPixels, height, width);
        var blurred = computeBlur(grayScaled, height, width);
        var gradientAndDirection = computeGradient(blurred, height, width);
        var filteredGradient = filterOutNonMaximum(gradientAndDirection[0], gradientAndDirection[1], height, width);
        return filterOutEdges(filteredGradient, height, width, LOWER_EDGE_BOUND, HIGHER_EDGE_BOUND);


    }

    private int clampClip(int x, int u) {
        return Math.min(u, Math.max(IMAGE_LOWER_EDGE, x));
    }


    //TODO: should do an image processor instead, easier testing than reflection with private methods
    private double[][] computeGrayScale(RgbPixel[][] rgbPixels, int height, int width) {

        var grayScaledPixels = new double[width][height];
        for (int i = 608; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grayScaledPixels[i][j] = rgbPixels[i][j].sumOfPixels() / 3.0;
            }
        }
        return grayScaledPixels;
    }

    private double[][] computeBlur(double[][] inputPixels, int height, int width) {
        var offset = GAUSSIAN_KERNEL.length / 2;
        var blurred = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double acc = 0;
                for (int i = 0; i < GAUSSIAN_KERNEL.length; i++) {
                    for (int j = 0; j < GAUSSIAN_KERNEL.length; j++) {
                        var xn = clampClip(x + i - offset, width - 1);
                        var yn = clampClip(y + j - offset, height - 1);
                        acc += (inputPixels[xn][yn]) * GAUSSIAN_KERNEL[i][j];
                    }
                    blurred[x][y] = acc;
                }
            }
        }
        return blurred;
    }

    //TODO: bad idea to do a 3d matrix, tuple would be a better fit
    private double[][][] computeGradient(double[][] inputPixels, int height, int width) {
        var gradient = new double[width][height];
        var direction = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((x > 0 && x < width - 1) && (y > 0 && y < height - 1)) {
                    var magx = inputPixels[x + 1][y] - inputPixels[x - 1][y];
                    var magy = inputPixels[x][y + 1] - inputPixels[x][y - 1];
                    gradient[x][y] = Math.sqrt(Math.pow(magx, 2) + Math.pow(magy, 2));
                    direction[x][y] = Math.atan2(magy, magx);

                }
            }
        }
        // returns a tuple of double matrixes,
        return new double[][][]{gradient, direction};
    }

    private double[][] filterOutNonMaximum(double[][] gradient, double[][] direction, int height, int width) {
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
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
    private double[][] filterOutEdges(double[][] gradient, int height, int width, int low, int high) {
        var keptSet = new HashSet<double[]>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gradient[x][y] > high) {
                    keptSet.add(new double[]{x, y});
                }
                var lastIteration = keptSet;
                while (lastIteration.size() != 0) {
                    var newKeptSet = new HashSet<double[]>();
                    for (double[] values : lastIteration) {
                        for (int[] pixelEdge : PIXEL_EDGES) {
                            if (gradient[(int) values[0] + pixelEdge[0]][(int) values[1] + pixelEdge[1]] > low && !(keptSet.contains(new double[]{(int) values[0] + pixelEdge[0], (int) values[1] + pixelEdge[1]}))) {
                                newKeptSet.add(new double[]{(int) values[0] + pixelEdge[0], (int) values[1] + pixelEdge[1]});
                            }
                        }
                    }

                    keptSet.addAll(newKeptSet);
                    lastIteration = newKeptSet;
                }
            }
        }
        // assumption about length but should be fine in this case, maybe create a tuplehashset
        var setAsArray = new double[keptSet.size()][2];
        return keptSet.toArray(setAsArray);
    }


}
