package impl.kmeans;

import interfaces.kmeans.ClusteringAlgorithm;
import interfaces.kmeans.Distance;
import models.kmeans.Centroid;
import models.kmeans.Record;

import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class KMeansAlgorithm implements ClusteringAlgorithm {

    private final Distance distance;

    private static final Random random = new Random();

    @Inject
    public KMeansAlgorithm(Distance distance) {
        this.distance = distance;
    }


    private List<Centroid> relocateCentroids(Map<Centroid, List<Record>> clusters) {
        return clusters
                .entrySet()
                .stream()
                .map(e -> average(e.getKey(), e.getValue()))
                .collect(toList());
    }


    private Centroid average(Centroid centroid, List<Record> records) {
        if (records == null || records.isEmpty()) {
            return centroid;
        }

        Map<String, Double> average = centroid.getCoordinates();

        records
                .stream()
                .flatMap(e -> e
                        .getFeatures()
                        .keySet()
                        .stream())
                .forEach(k -> average.put(k, 0.0));

        for (Record record : records) {
            record
                    .getFeatures()
                    .forEach((k, v) -> average.compute(k, (k1, currentValue) -> {
                        if (currentValue == null) return v;
                        return v + currentValue;
                    }));
        }

        average.forEach((k, v) -> average.put(k, v / records.size()));

        return new Centroid(average);
    }

    private void assignToCluster(Map<Centroid, List<Record>> clusters, Record record, Centroid centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(record);
            return list;
        });
    }


    private Centroid nearestCentroid(Record record, List<Centroid> centroids) {
        double minimumDistance = Double.MAX_VALUE;
        Centroid nearest = null;

        for (Centroid centroid : centroids) {
            double currentDistance = distance.calculate(record.getFeatures(), centroid.getCoordinates());

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearest = centroid;
            }
        }

        return nearest;
    }

    private List<Centroid> randomCentroids(List<Record> records, int k) {

        List<Centroid> centroids = new ArrayList<>();
        Map<String, Double> maxs = new HashMap<>();
        Map<String, Double> mins = new HashMap<>();

        for (Record record : records) {
            record
                    .getFeatures()
                    .forEach((key, value) -> {
                        maxs.compute(key, (k1, max) -> max == null || value > max ? value : max);

                        mins.compute(key, (k1, min) -> min == null || value < min ? value : min);
                    });
        }

        Set<String> attributes = records
                .stream()
                .flatMap(e -> e
                        .getFeatures()
                        .keySet()
                        .stream())
                .collect(toSet());
        for (int i = 0; i < k; i++) {
            Map<String, Double> coordinates = new HashMap<>();
            for (String attribute : attributes) {
                double max = maxs.get(attribute);
                double min = mins.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            }

            centroids.add(new Centroid(coordinates));
        }

        return centroids;
    }

    private void applyPreconditions(List<Record> records, int k, int maxIterations) {
        if (records == null || records.isEmpty()) {
            throw new IllegalArgumentException("The dataset can't be empty");
        }

        if (k <= 1) {
            throw new IllegalArgumentException("It doesn't make sense to have less than or equal to 1 cluster");
        }

        if (distance == null) {
            throw new IllegalArgumentException("The distance calculator is required");
        }

        if (maxIterations <= 0) {
            throw new IllegalArgumentException("Max iterations should be a positive number");
        }
    }

    @Override
    public Map<Centroid, List<Record>> fit(List<Record> records, int k, int maxIterations) {
        applyPreconditions(records, k, maxIterations);

        List<Centroid> centroids = randomCentroids(records, k);
        Map<Centroid, List<Record>> clusters = new HashMap<>();
        Map<Centroid, List<Record>> lastState = new HashMap<>();

        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = i == maxIterations - 1;

            for (Record record : records) {
                Centroid centroid = nearestCentroid(record, centroids);
                assignToCluster(clusters, record, centroid);
            }

            boolean shouldTerminate = isLastIteration || clusters.equals(lastState);
            lastState = clusters;
            if (shouldTerminate) {
                break;
            }

            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();
        }

        return lastState;
    }
}