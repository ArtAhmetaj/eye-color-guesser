import impl.shapefinder.BaseImageReadWriter;
import impl.shapefinder.CircleShapeFinder;
import models.shapefinder.Circle;
import models.RgbPixel;
import util.ImageUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {
    //TODO: offload to constant class
    private static final int MAXIMUM_CIRCLES_THRESHOLD = 5;

    private static final String CURRENT_PATH = "/test-images/art_eye.jpg";

    public static void main(String[] args) {
        var appModule = DaggerAppModule.create();
        CircleShapeFinder shapeFinder = appModule.shapeFinder();
        BaseImageReadWriter imageReader = appModule.imageReadWriter();

        try {
            var imageData = imageReader.readImage(CURRENT_PATH);
            var circles = shapeFinder.findShapes(imageData.getRgbPixels(), imageData.getWidth(), imageData.getHeight());
            var circlesPixelsMapping = new HashMap<Circle,List<RgbPixel>>();
            for(Circle circle : circles){
                circlesPixelsMapping.put(circle,ImageUtil.cropImageBasedOnShape(imageData,circle));
            }


        } catch (IOException e) {

            System.out.printf("Error during IO with message: %s", e.getMessage());
        }
    }
}
