import dagger.Component;
import impl.BaseImageReadWriter;
import impl.CircleShapeFinder;
import modules.EdgeDetectorModule;

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