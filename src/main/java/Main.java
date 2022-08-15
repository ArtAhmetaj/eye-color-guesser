import impl.kmeans.KMeansAlgorithm;
import impl.shapefinder.BaseImageReadWriter;
import impl.shapefinder.CircleShapeFinder;
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

    public static void main(String[] args) {
        var appModule = DaggerAppModule.create();
        CircleShapeFinder shapeFinder = appModule.shapeFinder();
        KMeansAlgorithm kMeansAlgorithm = appModule.clusteringAlgorithm();
        BaseImageReadWriter imageReader = appModule.imageReadWriter();

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

            System.out.println(centroidsPerCircle.get(1));


        } catch (IOException e) {

            System.out.printf("Error during IO with message: %s", e.getMessage());
        }
    }
}
