package interfaces;

import dagger.Component;
import models.RgbPixel;

import java.util.List;

public interface ShapeFinder<T> {
    public List<T> findShapes(RgbPixel[][] rgbPixels, int width, int height);

}
