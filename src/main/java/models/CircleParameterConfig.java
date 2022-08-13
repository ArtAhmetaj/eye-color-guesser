package models;

public class CircleParameterConfig {

    // rmin and rmax should be in between 10% and 40%
    private final int radiusMin;
    private final int radiusMax;
    private final int steps;

    public int getRadiusMin() {
        return radiusMin;
    }

    public int getRadiusMax() {
        return radiusMax;
    }

    public int getSteps() {
        return steps;
    }

    public double getThreshold() {
        return threshold;
    }

    private final double threshold;

    public CircleParameterConfig(int height, int width) {
        // 10 percent for min and 40 percent for max
        this.radiusMin = 50;
        this.radiusMax = 200;
        this.steps = 100;
        this.threshold = 0.4;
    }



}
