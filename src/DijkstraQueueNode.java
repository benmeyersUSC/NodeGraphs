/**
 * heiovk3r
 *
 * @author Ben Meyers
 * email: bemeyers@usc.edu
 * ITP 265, Fall 2024, Tea
 * Date Created: 11/20/24
 */
public class DijkstraQueueNode {
    public GNode node;
    public double distance;
    public GNode from;

    DijkstraQueueNode(GNode node, double distance, GNode from){
        this.node = node; this.distance = distance; this.from = from;
    }

    DijkstraQueueNode(GNode node, double distance){
        this(node, distance, null);
    }

    @Override
    public String toString() {
        return "DijkstraQueueNode{" +
                "node=" + node +
                ", distance=" + distance +
                ", from=" + from +
                '}';
    }
}
