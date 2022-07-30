package interfaces;

import models.ImageData;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageReadWriter {

    ImageData readImage(String path) throws IOException;
    void saveImage(String path,String suffix,int[][] rgbPixels) throws IOException;
}
