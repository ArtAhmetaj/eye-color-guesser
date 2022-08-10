import dagger.Component;
import impl.CircleShapeFinder;
import interfaces.EdgeDetector;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args){
        var appModule = DaggerAppModule.create();
        var shapeFinder = appModule.shapeFinder();
        var imageReader = appModule.imageReadWriter();

        try {

            var result = imageReader.readImage( "./test-images/art_eye.jpg");
           var circles =  shapeFinder.findShapes(result.getRgbPixels(),result.getHeight(),result.getWidth());
            System.out.println(circles);
        } catch (IOException e) {
         e.printStackTrace();
        }
    }
}
