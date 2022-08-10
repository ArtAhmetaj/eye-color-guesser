package helper;

import models.Circle;

import java.util.*;

public class CircleShapeHelper {

    public static double[] computeCirclePropertiesForRange(double radius, int range, int steps) {
        double x = radius * Math.cos(2 * Math.PI * range / steps);
        double y = radius * Math.sin(2 * Math.PI * range / steps);
        return new double[]{radius, x, y};
    }

    public static Map<double[], Integer> sortEdgesMap(Map<double[], Integer> edgesMap) {
        List<Map.Entry<double[], Integer>> listOfEntries =
                new ArrayList<Map.Entry<double[], Integer>>(edgesMap.entrySet());
        listOfEntries.sort((l, r) -> {
            return l.getValue() - r.getValue();
        });

        Map<double[], Integer> sortedMap = new LinkedHashMap<double[], Integer>();
        for (Map.Entry<double[], Integer> entry : listOfEntries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    //TODO: check on better naming
    public static boolean isEdgeInAllCircles(double[] edge, List<Circle> circles) {
        return circles.stream().allMatch(
                c -> Math.pow(edge[0] - c.getX(), 2) + Math.pow(edge[1] - c.getY(), 2) > Math.pow(c.getRadius(), 2));
    }

    ;
    //all((x - xc) ** 2 + (y - yc) ** 2 > rc ** 2 for xc, yc, rc in circles):
}

