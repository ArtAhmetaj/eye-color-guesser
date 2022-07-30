package util;

import models.RgbPixel;

public class ImageProcessingUtil {
    // uses average method of calculating grayscale, so it is being summed and divided by 3
    public static int[][] convertToGrayscale(RgbPixel[][] rgbRgbPixels) {

        var grayScaledPixels = new int[rgbRgbPixels.length][rgbRgbPixels[0].length];
        for (int i = 0; i < rgbRgbPixels.length; i++) {
            for (int j = 0; j < rgbRgbPixels[0].length; j++) {
                grayScaledPixels[i][j] = rgbRgbPixels[i][j].sumOfPixels() / 3;
            }
        }
        return grayScaledPixels;
    }

}



