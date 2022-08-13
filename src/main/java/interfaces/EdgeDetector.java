package interfaces;

import models.RgbPixel;

public interface EdgeDetector {

    double[][] findEdges(RgbPixel[][] inputPixels, int width, int height);
}
