package interfaces.kmeans;

import models.kmeans.Centroid;
import models.kmeans.Record;

import java.util.List;
import java.util.Map;

public interface ClusteringAlgorithm {
    Map<Centroid, List<Record>> fit(List<Record> records, int k,  int maxIterations);

}
