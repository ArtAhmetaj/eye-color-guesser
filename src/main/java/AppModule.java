import dagger.Component;
import impl.colormapper.CssColorMapper;
import impl.kmeans.KMeansAlgorithm;
import impl.shapefinder.BaseImageReadWriter;
import impl.shapefinder.CircleShapeFinder;
import interfaces.colormapper.ColorMapper;
import modules.kmeans.DistanceModule;
import modules.shapefinder.EdgeDetectorModule;

import javax.inject.Singleton;

@Singleton
@Component(
        modules = {
                EdgeDetectorModule.class,
                DistanceModule.class
        }
)
public interface AppModule {
   CircleShapeFinder shapeFinder();
   BaseImageReadWriter imageReadWriter();
   KMeansAlgorithm clusteringAlgorithm();
   CssColorMapper colorMapper();
}