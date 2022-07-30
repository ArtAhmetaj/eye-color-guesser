package interfaces;

import models.RgbPixel;

import java.util.List;

public interface EdgeDetector {

    List<Integer> findEdges(RgbPixel[][] inputPixels, int height, int width);
}
