import dagger.Component;
import interfaces.ShapeFinder;
import models.Circle;

import java.util.ArrayList;
import java.util.List;

@Component
public class CircleShapeFinder implements ShapeFinder<Circle> {

    @Override
    public List<Circle> findShapes(int[][] rgbPixels) {
      return null;

    }


}
