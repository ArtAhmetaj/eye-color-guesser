package impl.kmeans;

import interfaces.kmeans.Distance;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;

public class EuclideanDistance implements Distance {

    @Inject
    public EuclideanDistance(){

    }
    @Override
    public double calculate(Map<String, Double> f1, Map<String, Double> f2) {
        Objects.requireNonNull(f1);
        Objects.requireNonNull(f2);
        if (f1.values().stream().anyMatch(Objects::isNull) || f2.values().stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
        if (f1.size() != f2.size()) throw new IllegalArgumentException("Features must be of same size");

        double sum = 0;
        for (String key : f1.keySet()) {
            Double v1 = f1.get(key);
            Double v2 = f2.get(key);
            sum+=Math.pow(v1-v2,2);
        }
        return Math.sqrt(sum);

    }
}
