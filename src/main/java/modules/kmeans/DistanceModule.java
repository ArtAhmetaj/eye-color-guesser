package modules.kmeans;

import dagger.Binds;
import dagger.Module;
import impl.kmeans.EuclideanDistance;
import impl.shapefinder.CannyEdgeDetector;
import interfaces.kmeans.Distance;
import interfaces.shapefinder.EdgeDetector;

import javax.inject.Singleton;

@Module
public interface DistanceModule {
    @Binds
    @Singleton
    Distance bindDistance(EuclideanDistance impl);
}