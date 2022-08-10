package impl;

import dagger.Component;
import helper.CircleShapeHelper;
import interfaces.EdgeDetector;
import interfaces.ShapeFinder;
import models.Circle;
import models.CircleParameterConfig;
import models.RgbPixel;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Singleton
public class CircleShapeFinder implements ShapeFinder<Circle> {

    @Inject
    CannyEdgeDetector cannyEdgeDetector;

    @Override
    public List<Circle> findShapes(RgbPixel[][] rgbPixels, int width, int height) {
        // some values are hardcoded in config, could be changed dynamically
        CircleParameterConfig circleParameterConfig = new CircleParameterConfig(height, width);
        var points = new ArrayList<double[]>();
        for (int r = circleParameterConfig.getRadiusMin(); r <= circleParameterConfig.getRadiusMax(); r++) {
            var steps = circleParameterConfig.getSteps();
            for (int i = 0; i < steps; i++) {
                points.add(CircleShapeHelper.computeCirclePropertiesForRange(r, i, steps));
            }
        }
        var acc = new HashMap<double[], Integer>();
        for (double[] edges : cannyEdgeDetector.findEdges(rgbPixels, height, width)) {
            for (double[] point : points) {
                var radius = point[0];
                var a = edges[0] - point[1];
                var b = edges[1] - point[2];
                var roughEdges = new double[]{a, b, radius};
                acc.computeIfAbsent(new double[]{a, b, radius}, k -> 0);
                acc.merge(roughEdges, 1, Integer::sum);
            }
        }

        var circles = new ArrayList<Circle>();
        var sortedAcc = CircleShapeHelper.sortEdgesMap(acc);
        sortedAcc.forEach((k, v) -> {
            //TODO: horrible fix with 1.0, put in a double variable and clean up code
            if ((v * 1.0 / circleParameterConfig.getSteps()) >= circleParameterConfig.getThreshold()
                    && CircleShapeHelper.isEdgeInAllCircles(k, circles)) {
                circles.add(new Circle(k[0], k[1], k[2]));
            }

        });

        return circles;

    }


}
