//import java.util.Objects;
//
//public class GNode {
//    private final double x;
//    private final double y;
//    private final String id;
//
//    // Constructor with coordinates
//    public GNode(double x, double y, String id) {
//        this.id = id;
//        this.x = x;
//        this.y = y;
//    }
//
//    // Getters and setters
//    public double getX() {
//        return x;
//    }
//
//    public double getY() {
//        return y;
//    }
//
//    public String getId(){
//        return id;
//    }
//
//    // Override equals and hashCode for proper HashMap functionality
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        GNode gNode = (GNode) o;
//        return Double.compare(gNode.x, x) == 0 &&
//                Double.compare(gNode.y, y) == 0 &&
//                gNode.getId().equalsIgnoreCase(getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(x, y, id);
//    }
//
//    // Optional: toString method for debugging
//    @Override
//    public String toString() {
//        return "GNode (" + id + ")";
//    }
//}
//

import java.util.Objects;

public record GNode(double x, double y, String id) {

    // Override equals and hashCode for proper HashMap functionality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GNode gNode = (GNode) o;
        return Double.compare(gNode.x, x) == 0 &&
                Double.compare(gNode.y, y) == 0 &&
                gNode.id().equalsIgnoreCase(id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, id);
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "GNode (" + id + ")";
    }
}