package interfaces.shapefinder;

import models.RgbPixel;

import java.util.List;

public interface EdgeDetector {

    List<List<Integer>> findEdges(RgbPixel[][] inputPixels, int width, int height);
}
