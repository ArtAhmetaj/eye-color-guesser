package modules.shapefinder;

import dagger.Binds;
import dagger.Module;
import impl.shapefinder.CannyEdgeDetector;
import interfaces.shapefinder.EdgeDetector;

import javax.inject.Singleton;

@Module
public interface EdgeDetectorModule {
    @Binds
    @Singleton
    EdgeDetector bindEdgeDetector(CannyEdgeDetector impl);
}