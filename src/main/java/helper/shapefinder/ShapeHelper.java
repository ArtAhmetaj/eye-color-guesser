package helper.shapefinder;

import models.shapefinder.Circle;

import java.util.*;

public class ShapeHelper {

    public static double[] computeCirclePropertiesForRange(double radius, int range, int steps) {
        double x = radius * Math.cos(2 * Math.PI * range / steps);
        double y = radius * Math.sin(2 * Math.PI * range / steps);
        return new double[]{radius, x, y};
    }

    public static Map<List<Integer>, Integer> sortEdgesMap(Map<List<Integer>, Integer> edgesMap) {
        List<Map.Entry<List<Integer>, Integer>> listOfEntries =
                new ArrayList<Map.Entry<List<Integer>, Integer>>(edgesMap.entrySet());
        listOfEntries.sort((l, r) -> {
            return r.getValue() - l.getValue();
        });

        Map<List<Integer>, Integer> sortedMap = new LinkedHashMap<List<Integer>, Integer>();
        for (Map.Entry<List<Integer>, Integer> entry : listOfEntries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    //TODO: check on better naming
    public static boolean isEdgeInAllCircles(List<Integer> edge, List<Circle> circles) {

        return circles.stream().allMatch(
                c -> Math.pow(edge.get(0) - c.getX(), 2) + Math.pow(edge.get(1) - c.getY(), 2) > Math.pow(c.getRadius(), 2));
    }

    ;
}

