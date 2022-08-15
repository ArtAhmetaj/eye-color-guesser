import dagger.Component;
import impl.kmeans.KMeansAlgorithm;
import impl.shapefinder.BaseImageReadWriter;
import impl.shapefinder.CircleShapeFinder;
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
}