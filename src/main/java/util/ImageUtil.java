package util;

import models.ImageData;
import models.RgbPixel;
import models.shapefinder.Shape;

import java.util.ArrayList;
import java.util.List;

public class ImageUtil {
    public static List<RgbPixel> cropImageBasedOnShape(ImageData image, Shape shape){
        var edges = shape.getShapeBounds();
        // calculate based on shape bounds
        var returnedPixels = new ArrayList<RgbPixel>();
        var currentPixels = image.getRgbPixels();
        for(int i =0;i<currentPixels.length;i++){
            for(int j =0;j<image.getRgbPixels()[0].length;j++){
                if((i>= edges.get(0)[0] || i<= edges.get(0)[1]) && ( j>=edges.get(0)[0] || j<= edges.get(0)[1])){
                    returnedPixels.add(currentPixels[i][j]);
                }


            }
        }
        return returnedPixels;

    }

}
