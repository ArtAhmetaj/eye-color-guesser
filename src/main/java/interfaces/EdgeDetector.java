package interfaces;

import dagger.Component;
import models.RgbPixel;

import java.util.List;

public interface EdgeDetector {

    double[][] findEdges(RgbPixel[][] inputPixels, int height, int width);
}
