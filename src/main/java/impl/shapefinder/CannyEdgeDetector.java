package impl.shapefinder;

import interfaces.shapefinder.EdgeDetector;
import models.RgbPixel;

import javax.inject.Inject;
import java.util.ArrayList;
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

    private static final int LOWER_EDGE_BOUND = 10;

    private static final int HIGHER_EDGE_BOUND = 40;

    @Inject
    public CannyEdgeDetector() {

    }

    @Override
    public List<List<Integer>> findEdges(RgbPixel[][] inputPixels, int width, int height) {
        // check into offloading this somewhere else
        var grayScaled = computeGrayScale(inputPixels, width, height);
        var blurred = computeBlur(grayScaled, width, height);
        var gradientAndDirection = computeGradient(blurred, width, height);
        var filteredGradient = filterOutNonMaximum(gradientAndDirection[0], gradientAndDirection[1], width, height);
        return filterOutEdges(filteredGradient, width, height, LOWER_EDGE_BOUND, HIGHER_EDGE_BOUND);
    }

    private int clampClip(int x, int u) {
        return x < IMAGE_LOWER_EDGE ? IMAGE_LOWER_EDGE : Math.min(x, u);
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
        var offset = (int) Math.sqrt(GAUSSIAN_KERNEL.length);
        var blurred = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double acc = 0;
                for (int a = 0; a < GAUSSIAN_KERNEL.length; a++) {
                    for (int b = 0; b < GAUSSIAN_KERNEL[0].length; b++) {
                        var xn = clampClip((int) (x + a - offset), width - 1);
                        var yn = clampClip((int) (y + b - offset), height - 1);
                        acc += inputPixels[xn][yn] * GAUSSIAN_KERNEL[a][b];
                    }
                    blurred[x][y] = (int) acc;
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
                var angle = direction[x][y] >= 0 ? direction[x][y] : direction[x][y] + Math.PI;
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
    private List<List<Integer>> filterOutEdges(double[][] gradient, int width, int height, int low, int high) {
        var keptSet = new HashSet<List<Integer>>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gradient[x][y] > high) {
                    keptSet.add(List.of(x, y));
                }

            }
        }
        var lastIteration = keptSet;
        while (lastIteration.size() != 0) {
            var newKeptSet = new HashSet<List<Integer>>();
            for (List<Integer> values : lastIteration) {
                for (int[] pixelEdge : PIXEL_EDGES) {
                    if (gradient[(int) (values.get(0) + pixelEdge[0])][(int) (values.get(1) + pixelEdge[1])] > low &&
                            !(keptSet.contains(List.of(values.get(0) + pixelEdge[0], values.get(1) + pixelEdge[1])))) {
                        newKeptSet.add(List.of(values.get(0) + pixelEdge[0], values.get(1) + pixelEdge[1]));
                    }
                }
            }
            keptSet.addAll(newKeptSet);
            lastIteration = newKeptSet;
        }

        return new ArrayList<>(keptSet);

    }


}
