package modules;

import dagger.Binds;
import dagger.Module;
import impl.BaseImageReadWriter;
import impl.CannyEdgeDetector;
import interfaces.EdgeDetector;
import interfaces.ImageReadWriter;

import javax.inject.Singleton;

@Module
public interface EdgeDetectorModule {
    @Binds
    @Singleton
    EdgeDetector bindEdgeDetector(CannyEdgeDetector impl);
}