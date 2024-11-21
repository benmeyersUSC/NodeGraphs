import java.util.Comparator;

public class DistanceComparator implements Comparator<DijkstraQueueNode> {
    @Override
    public int compare(DijkstraQueueNode dj1, DijkstraQueueNode dj2) {
        // Safely extract distance values
        double distance1 = dj1.distance;
        double distance2 = dj2.distance;

        // Compare distances
        return Double.compare(distance1, distance2);
    }
}