package impl;

import dagger.Component;
import interfaces.EdgeDetector;
import models.RgbPixel;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class CannyEdgeDetector implements EdgeDetector {

    private static final double[][] GAUSSIAN_KERNEL = {
            {1 / 256.0, 4 / 256.0, 6 / 256.0, 4 / 256.0, 1 / 256.0},
            {4 / 256.0, 16 / 256.0, 24 / 256.0, 16 / 256.0, 4 / 256.0},
            {6 / 256.0, 24 / 256.0, 36 / 256.0, 24 / 256.0, 6 / 256.0},
            {4 / 256.0, 16 / 256.0, 24 / 256.0, 16 / 256.0, 4 / 256.0},
            {1 / 256.0, 4 / 256.0, 6 / 256.0, 4 / 256.0, 1 / 256.0}
    };

    private static final int[][] PIXEL_EDGES = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    private static final int IMAGE_LOWER_EDGE = 0;

    private static final int LOWER_EDGE_BOUND = 25;

    private static final int HIGHER_EDGE_BOUND = 40;

    @Inject
    public CannyEdgeDetector() {

    }

    @Override
    public double[][] findEdges(RgbPixel[][] inputPixels, int width, int height) {
        // check into offloading this somewhere else
        var grayScaled = computeGrayScale(inputPixels, width, height);
        var blurred = computeBlur(grayScaled, width, height);
        var gradientAndDirection = computeGradient(blurred, width, height);
        var filteredGradient = filterOutNonMaximum(gradientAndDirection[0], gradientAndDirection[1], width, height);
        return filterOutEdges(filteredGradient, width, height, LOWER_EDGE_BOUND, HIGHER_EDGE_BOUND);
    }

    private int clampClip(int x, int u) {
        return Math.min(u, Math.max(IMAGE_LOWER_EDGE, x));
    }


    //TODO: should do an image processor instead, easier testing than reflection with private methods
    private double[][] computeGrayScale(RgbPixel[][] rgbPixels, int width, int height) {

        var grayScaledPixels = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grayScaledPixels[i][j] = rgbPixels[i][j].sumOfPixels() / 3.0;
            }
        }
        return grayScaledPixels;
    }

    private double[][] computeBlur(double[][] inputPixels, int width, int height) {
        var offset = Math.sqrt(GAUSSIAN_KERNEL.length);
        var blurred = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double acc = 0;
                for (int a = 0; a < GAUSSIAN_KERNEL.length; a++) {
                    for (int b = 0; b < GAUSSIAN_KERNEL.length; b++) {
                        var xn = clampClip((int) (x + a - offset), width - 1);
                        var yn = clampClip((int) (y + b - offset), height - 1);
                        acc += (inputPixels[xn][yn]) * GAUSSIAN_KERNEL[a][b];
                    }
                    blurred[x][y] = acc;
                }
            }
        }
        return blurred;
    }

    //TODO: bad idea to do a 3d matrix, tuple would be a better fit
    private double[][][] computeGradient(double[][] inputPixels, int width, int height) {
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

    private double[][] filterOutNonMaximum(double[][] gradient, double[][] direction, int width, int height) {
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

    /*

def filter_strong_edges(gradient, width, height, low, high):
    # Keep strong edges
    keep = set()
    for x in range(width):
        for y in range(height):
            if gradient[x, y] > high:
                keep.add((x, y))

    # Keep weak edges next to a pixel to keep
    lastiter = keep
    while lastiter:
        newkeep = set()
        for x, y in lastiter:
            for a, b in ((-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)):
                if gradient[x + a, y + b] > low and (x+a, y+b) not in keep:
                    newkeep.add((x+a, y+b))
        keep.update(newkeep)
        lastiter = newkeep

    return list(keep)
     */
    //TODO: fix differences between int and double arrays, maybe replace with generic List<?>
    private double[][] filterOutEdges(double[][] gradient, int width, int height, int low, int high) {
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
                            //TODO: change to arraylist, to not have issues with array equalness on hashset
                            if (gradient[(int) values[0] + pixelEdge[0]][(int) values[1] + pixelEdge[1]] > low &&
                                    !(keptSet.contains(new double[]{(int) values[0] + pixelEdge[0], (int) values[1] + pixelEdge[1]}))) {
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
