import impl.colormapper.CssColorMapper;
import impl.kmeans.KMeansAlgorithm;
import impl.shapefinder.BaseImageReadWriter;
import impl.shapefinder.CircleShapeFinder;
import interfaces.colormapper.ColorMapper;
import models.kmeans.Centroid;
import models.kmeans.Record;
import models.shapefinder.Circle;
import models.RgbPixel;
import util.ImageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    //TODO: offload to constant class
    private static final int MAXIMUM_CIRCLES_THRESHOLD = 5;

    private static final String CURRENT_PATH = "./test-images/art_eye.jpg";

    private static final List<RgbPixel> EYE_COLORS = List.of(
            new RgbPixel(99, 78, 52),
            new RgbPixel(46, 83, 111),
            new RgbPixel(61, 103, 29),
            new RgbPixel(28, 120, 71),
            new RgbPixel(73, 118, 101)
    );

    public static void main(String[] args) {
        var appModule = DaggerAppModule.create();
        CircleShapeFinder shapeFinder = appModule.shapeFinder();
        KMeansAlgorithm kMeansAlgorithm = appModule.clusteringAlgorithm();
        BaseImageReadWriter imageReader = appModule.imageReadWriter();
        CssColorMapper colorMapper = appModule.colorMapper();

        try {
            var imageData = imageReader.readImage(CURRENT_PATH);
            var circles = shapeFinder.findShapes(imageData.getRgbPixels(), imageData.getWidth(), imageData.getHeight());
            var circlesPixelsMapping = new HashMap<Circle, List<RgbPixel>>();
            for (Circle circle : circles) {
                circlesPixelsMapping.put(circle, ImageUtil.cropImageBasedOnShape(imageData, circle));
            }

            List<List<Centroid>> centroidsPerCircle = new ArrayList<>();
            circlesPixelsMapping.forEach((k, v) -> {
                List<Record> records = v.stream().
                        map(e -> new Record(e.toString(),
                                Map.of("red", (double) e.getRed(),
                                        "green", (double) e.getGreen(),
                                        "blue", (double) e.getBlue())))
                        .collect(Collectors.toList());
                var centroids = kMeansAlgorithm.fit(records, 3, 1000);
                centroidsPerCircle.add(new ArrayList<>(centroids.keySet()));
            });

            int maximumMatching = Integer.MIN_VALUE;
            List<Centroid> closestCentroids = new ArrayList<>();
            for (var centroids : centroidsPerCircle) {
                int centroidMatching = 0;
                for (var centroid : centroids) {
                    var coordinates = centroid.getCoordinates();
                    var centroidColor = new RgbPixel(
                            coordinates.get("red").intValue(),
                            coordinates.get("green").intValue(),
                            coordinates.get("blue").intValue());
                    var matchingColors = colorMapper.isColorCloseToGroupOfColors(centroidColor, EYE_COLORS);
                    centroidMatching++;

                }
                if(centroidMatching > maximumMatching){
                    maximumMatching = centroidMatching;
                    closestCentroids = centroids;
                }
            }

            System.out.println( getEyeColorFormatted(closestCentroids,colorMapper));


        } catch (IOException e) {

            System.out.printf("Error during IO with message: %s", e.getMessage());
        }
    }

    private static String getEyeColorFormatted(List<Centroid> centroids, ColorMapper colorMapper){
        List<RgbPixel> colors = new ArrayList<>();
        for(var centroid : centroids){
            //TODO: centroid - color mapping helper
            var coordinates = centroid.getCoordinates();
            colors.add(new RgbPixel(
                    coordinates.get("red").intValue(),
                    coordinates.get("green").intValue(),
                    coordinates.get("blue").intValue()));

        }
        var secondaryColors = colors.subList(1,colors.size()-1).stream()
                .map(colorMapper::getNameOfColor).collect(Collectors.toList());

        return String.format("Your eye color is mainly %s with shades of: %s",
                colorMapper.getNameOfColor(colors.get(0)),String.join(", ", secondaryColors));
    }
}
