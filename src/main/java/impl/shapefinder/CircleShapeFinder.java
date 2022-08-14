package impl.shapefinder;

import helper.shapefinder.ShapeHelper;
import interfaces.shapefinder.EdgeDetector;
import interfaces.shapefinder.ShapeFinder;
import models.shapefinder.Circle;
import models.shapefinder.CircleParameterConfig;
import models.RgbPixel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CircleShapeFinder implements ShapeFinder<Circle> {

    private final EdgeDetector edgeDetector;

    @Inject
    public CircleShapeFinder(EdgeDetector edgeDetector) {
        this.edgeDetector = edgeDetector;
    }

    @Override
    public List<Circle> findShapes(RgbPixel[][] rgbPixels, int width, int height) {
        // some values are hardcoded in config, could be changed dynamically
        CircleParameterConfig circleParameterConfig = new CircleParameterConfig(height, width);
        var points = new ArrayList<double[]>();
        for (int radius = circleParameterConfig.getRadiusMin(); radius <= circleParameterConfig.getRadiusMax(); radius++) {
            var steps = circleParameterConfig.getSteps();
            for (int i = 0; i < steps; i++) {
                points.add(ShapeHelper.computeCirclePropertiesForRange(radius, i, steps));
            }
        }
        var acc = new HashMap<List<Integer>, Integer>();
        var edges = edgeDetector.findEdges(rgbPixels, width, height);
        for (var edge : edges) {
            for (double[] point : points) {
                int radius = (int) point[0];
                int a = (int) (edge.get(0) - point[1]);
                int b = (int) (edge.get(1) - point[2]);
                var roughEdges = List.of(a, b, radius);
                acc.merge(roughEdges, 0, (previous, value) -> previous + value + 1);
            }
        }

        var circles = new ArrayList<Circle>();
        var sortedAcc = ShapeHelper.sortEdgesMap(acc);
        sortedAcc.forEach((k, v) -> {
            if (((double) v / circleParameterConfig.getSteps()) >= circleParameterConfig.getThreshold()
                    && ShapeHelper.isEdgeInAllCircles(k, circles)) {
                circles.add(new Circle(k.get(0), k.get(1), k.get(2)));
            }

        });

        return circles;

    }


}
