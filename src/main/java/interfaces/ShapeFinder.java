package interfaces;

import java.util.List;

public interface ShapeFinder<T> {
    public List<T> findShapes(int[][] rgbPixels);

}
