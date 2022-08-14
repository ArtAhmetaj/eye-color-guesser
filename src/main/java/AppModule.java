import dagger.Component;
import impl.shapefinder.BaseImageReadWriter;
import impl.shapefinder.CircleShapeFinder;
import modules.shapefinder.EdgeDetectorModule;

import javax.inject.Singleton;

@Singleton
@Component(
        modules = {
                EdgeDetectorModule.class,
        }
)
public interface AppModule {
   CircleShapeFinder shapeFinder();
   BaseImageReadWriter imageReadWriter();
}