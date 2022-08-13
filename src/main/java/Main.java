import java.io.IOException;

public class Main {


    public static void main(String[] args){
        var appModule = DaggerAppModule.create();
        var shapeFinder = appModule.shapeFinder();
        var imageReader = appModule.imageReadWriter();

        try {

            var result = imageReader.readImage( "./test-images/art_eye.jpg");
           var circles =  shapeFinder.findShapes(result.getRgbPixels(),result.getWidth(),result.getHeight());
            System.out.println(circles);
        } catch (IOException e) {
         e.printStackTrace();
        }
    }
}
