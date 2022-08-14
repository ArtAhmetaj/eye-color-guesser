import exceptions.TooManyCirclesFoundException;
import util.ImageUtil;

import java.io.IOException;

public class Main {


    public static void main(String[] args) {
        var appModule = DaggerAppModule.create();
        var shapeFinder = appModule.shapeFinder();
        var imageReader = appModule.imageReadWriter();

        try {
            var imageData = imageReader.readImage("./test-images/art_eye.jpg");
            var circles = shapeFinder.findShapes(imageData.getRgbPixels(), imageData.getWidth(), imageData.getHeight());
            var croppedPixels  = ImageUtil.cropImageBasedOnShape(imageData,circles.get(0));
            // use kmeans

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
