import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

public class GraphVisualizer {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int PADDING = 50;
    private static final int NODE_RADIUS = 10;

    public static void saveGraphImage(NodeGraph graph, String filename, boolean showDijkstra) {
        Set<GNode> dijkstraNodes;
        if (!showDijkstra){
            dijkstraNodes = null;
        }
        else{
            // Ensure Dijkstra route is calculated
            graph.dijkstraRoute();
            dijkstraNodes = graph.getDijkstraNodes();
        }
        System.out.println("Dijkstra Route: " + dijkstraNodes);

        // Create a buffered image
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Set up the graphics context
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Find bounds of the graph
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (GNode node : graph.getNodes()) {
            minX = Math.min(minX, node.x());
            maxX = Math.max(maxX, node.x());
            minY = Math.min(minY, node.y());
            maxY = Math.max(maxY, node.y());
        }

        // Add padding to bounds
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
        minX -= rangeX * 0.1;
        maxX += rangeX * 0.1;
        minY -= rangeY * 0.1;
        maxY += rangeY * 0.1;

        // Create scaling functions
        double scaleX = (WIDTH - 2.0 * PADDING) / (maxX - minX);
        double scaleY = (HEIGHT - 2.0 * PADDING) / (maxY - minY);

        // Draw coordinate axes
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.0f));

        // Draw X-axis
        int yAxisPos = HEIGHT - PADDING - (int)((0 - minY) * scaleY);
        g2d.drawLine(PADDING, yAxisPos, WIDTH - PADDING, yAxisPos);

        // Draw Y-axis
        int xAxisPos = PADDING + (int)((0 - minX) * scaleX);
        g2d.drawLine(xAxisPos, PADDING, xAxisPos, HEIGHT - PADDING);

        // Draw grid lines and labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        // X-axis grid lines and labels
        for (int x = (int)Math.ceil(minX); x <= (int)Math.floor(maxX); x += 5) {
            int xPos = PADDING + (int)((x - minX) * scaleX);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(xPos, PADDING, xPos, HEIGHT - PADDING);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(x), xPos - 10, yAxisPos + 20);
        }

        // Y-axis grid lines and labels
        for (int y = (int)Math.ceil(minY); y <= (int)Math.floor(maxY); y += 5) {
            int yPos = HEIGHT - PADDING - (int)((y - minY) * scaleY);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(PADDING, yPos, WIDTH - PADDING, yPos);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(y), xAxisPos - 25, yPos + 5);
        }

        // Draw edges (non-Dijkstra paths first)
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1.0f));
        for (GNode from : graph.getNodes()) {
            for (GNode to : graph.getNeighbors(from)) {
                // Skip if this is a Dijkstra path edge (to be drawn later)
                if (dijkstraNodes != null &&
                        dijkstraNodes.contains(from) &&
                        dijkstraNodes.contains(to)) {
                    continue;
                }

                int x1 = PADDING + (int)((from.x() - minX) * scaleX);
                int y1 = HEIGHT - PADDING - (int)((from.y() - minY) * scaleY);
                int x2 = PADDING + (int)((to.x() - minX) * scaleX);
                int y2 = HEIGHT - PADDING - (int)((to.y() - minY) * scaleY);
                g2d.draw(new Line2D.Double(x1, y1, x2, y2));
            }
        }

        // Draw Dijkstra path edges (if any)
        if (dijkstraNodes != null && !dijkstraNodes.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3.0f)); // Thicker line

            // First, draw Dijkstra nodes connections
            for (GNode from : graph.getNodes()) {
                for (GNode to : graph.getNeighbors(from)) {
                    // Only draw if both nodes are in Dijkstra nodes
                    if (dijkstraNodes.contains(from) &&
                            dijkstraNodes.contains(to)) {
                        int x1 = PADDING + (int)((from.x() - minX) * scaleX);
                        int y1 = HEIGHT - PADDING - (int)((from.y() - minY) * scaleY);
                        int x2 = PADDING + (int)((to.x() - minX) * scaleX);
                        int y2 = HEIGHT - PADDING - (int)((to.y() - minY) * scaleY);
                        g2d.draw(new Line2D.Double(x1, y1, x2, y2));
                    }
                }
            }

            // NEW: Draw start node connections to Dijkstra path
            GNode startNode = graph.getStart();
            for (GNode to : graph.getNeighbors(startNode)) {
                // If the neighboring node is in Dijkstra path, draw the connection in red
                if (dijkstraNodes.contains(to)) {
                    int x1 = PADDING + (int)((startNode.x() - minX) * scaleX);
                    int y1 = HEIGHT - PADDING - (int)((startNode.y() - minY) * scaleY);
                    int x2 = PADDING + (int)((to.x() - minX) * scaleX);
                    int y2 = HEIGHT - PADDING - (int)((to.y() - minY) * scaleY);
                    g2d.draw(new Line2D.Double(x1, y1, x2, y2));
                }
            }
        }

        // Draw nodes
        for (GNode node : graph.getNodes()) {
            int x = PADDING + (int)((node.x() - minX) * scaleX);
            int y = HEIGHT - PADDING - (int)((node.y() - minY) * scaleY);

            // Draw node circle
            if (node == graph.getStart()) {
                g2d.setColor(Color.GREEN);
            } else if (node == graph.getEnd()) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLUE);
            }
            g2d.fill(new Ellipse2D.Double(x - NODE_RADIUS, y - NODE_RADIUS,
                    2 * NODE_RADIUS, 2 * NODE_RADIUS));

            // Draw node label
            g2d.setColor(Color.BLACK);
            FontMetrics metrics = g2d.getFontMetrics();
            String label = node.id() + " (" +
                    String.format("%.2f", node.x()) + "," +
                    String.format("%.2f", node.y()) + ")";
            int labelX = x - metrics.stringWidth(label) / 2;
            int labelY = y - NODE_RADIUS - 5;
            g2d.drawString(label, labelX, labelY);
        }

        // Clean up and save
        try {
            ImageIO.write(image, "PNG", new File(filename));
            System.out.println("Graph visualization saved to " + filename);
//            System.out.println(graph);
        } catch (Exception e) {
            System.err.println("Error saving visualization: " + e.getMessage());
        } finally {
            g2d.dispose();
        }
    }

    // Overloaded method to allow calling without Dijkstra nodes
    public static void saveGraphImage(NodeGraph graph, String filename) {
        saveGraphImage(graph, filename, false);
    }

    // Optional: Main method for testing
    public static void main(String[] args) {
        // Example usage
        NodeGraph graph = new NodeGraph();
        graph.ogNodeGraph();
        saveGraphImage(graph, "goodLookingGraph.png", true);
    }
}




//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.geom.Ellipse2D;
//import java.awt.geom.Line2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.Set;
//
//public class GraphVisualizer {
//    private static final int WIDTH = 800;
//    private static final int HEIGHT = 800;
//    private static final int PADDING = 50;
//    private static final int NODE_RADIUS = 10;
//
//    public static void saveGraphImage(NodeGraph graph, String filename, boolean showDijkstra) {
//        Set<GNode> dijkstraNodes;
//        if (!showDijkstra){
//            dijkstraNodes = null;
//        }
//        else{
////            try {
//            dijkstraNodes = graph.getDijkstraNodes();
//            if (dijkstraNodes == null){
//                graph.dijkstraRoute();
//                dijkstraNodes = graph.getDijkstraNodes();
//
//            }
//        }
//        System.out.println("Dijkstra Route: " + dijkstraNodes);
//
//
//        // Create a buffered image
//        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2d = image.createGraphics();
//
//        // Set up the graphics context
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setColor(Color.WHITE);
//        g2d.fillRect(0, 0, WIDTH, HEIGHT);
//
//        // Find bounds of the graph
//        double minX = Double.POSITIVE_INFINITY;
//        double maxX = Double.NEGATIVE_INFINITY;
//        double minY = Double.POSITIVE_INFINITY;
//        double maxY = Double.NEGATIVE_INFINITY;
//
//        for (GNode node : graph.getNodes()) {
//            minX = Math.min(minX, node.x());
//            maxX = Math.max(maxX, node.x());
//            minY = Math.min(minY, node.y());
//            maxY = Math.max(maxY, node.y());
//        }
//
//        // Add padding to bounds
//        double rangeX = maxX - minX;
//        double rangeY = maxY - minY;
//        minX -= rangeX * 0.1;
//        maxX += rangeX * 0.1;
//        minY -= rangeY * 0.1;
//        maxY += rangeY * 0.1;
//
//        // Create scaling functions
//        double scaleX = (WIDTH - 2.0 * PADDING) / (maxX - minX);
//        double scaleY = (HEIGHT - 2.0 * PADDING) / (maxY - minY);
//
//        // Draw coordinate axes
//        g2d.setColor(Color.LIGHT_GRAY);
//        g2d.setStroke(new BasicStroke(1.0f));
//
//        // Draw X-axis
//        int yAxisPos = HEIGHT - PADDING - (int)((0 - minY) * scaleY);
//        g2d.drawLine(PADDING, yAxisPos, WIDTH - PADDING, yAxisPos);
//
//        // Draw Y-axis
//        int xAxisPos = PADDING + (int)((0 - minX) * scaleX);
//        g2d.drawLine(xAxisPos, PADDING, xAxisPos, HEIGHT - PADDING);
//
//        // Draw grid lines and labels
//        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
//
//        // X-axis grid lines and labels
//        for (int x = (int)Math.ceil(minX); x <= (int)Math.floor(maxX); x += 5) {
//            int xPos = PADDING + (int)((x - minX) * scaleX);
//            g2d.setColor(Color.LIGHT_GRAY);
//            g2d.drawLine(xPos, PADDING, xPos, HEIGHT - PADDING);
//            g2d.setColor(Color.BLACK);
//            g2d.drawString(String.valueOf(x), xPos - 10, yAxisPos + 20);
//        }
//
//        // Y-axis grid lines and labels
//        for (int y = (int)Math.ceil(minY); y <= (int)Math.floor(maxY); y += 5) {
//            int yPos = HEIGHT - PADDING - (int)((y - minY) * scaleY);
//            g2d.setColor(Color.LIGHT_GRAY);
//            g2d.drawLine(PADDING, yPos, WIDTH - PADDING, yPos);
//            g2d.setColor(Color.BLACK);
//            g2d.drawString(String.valueOf(y), xAxisPos - 25, yPos + 5);
//        }
//
//        // Draw edges (non-Dijkstra paths first)
//        g2d.setColor(Color.GRAY);
//        g2d.setStroke(new BasicStroke(1.0f));
//        for (GNode from : graph.getNodes()) {
//            for (GNode to : graph.getNeighbors(from)) {
//                // Skip if this is a Dijkstra path edge (to be drawn later)
//                if (dijkstraNodes != null &&
//                        dijkstraNodes.contains(from) &&
//                        dijkstraNodes.contains(to)) {
//                    continue;
//                }
//
//                int x1 = PADDING + (int)((from.x() - minX) * scaleX);
//                int y1 = HEIGHT - PADDING - (int)((from.y() - minY) * scaleY);
//                int x2 = PADDING + (int)((to.x() - minX) * scaleX);
//                int y2 = HEIGHT - PADDING - (int)((to.y() - minY) * scaleY);
//                g2d.draw(new Line2D.Double(x1, y1, x2, y2));
//            }
//        }
//
//        // Draw Dijkstra path edges (if any)
//        if (dijkstraNodes != null && !dijkstraNodes.isEmpty()) {
//            g2d.setColor(Color.RED);
//            g2d.setStroke(new BasicStroke(3.0f)); // Thicker line
//            for (GNode from : graph.getNodes()) {
//                for (GNode to : graph.getNeighbors(from)) {
//                    // Only draw if both nodes are in Dijkstra nodes
//                    if (dijkstraNodes.contains(from) &&
//                            dijkstraNodes.contains(to)) {
//                        int x1 = PADDING + (int)((from.x() - minX) * scaleX);
//                        int y1 = HEIGHT - PADDING - (int)((from.y() - minY) * scaleY);
//                        int x2 = PADDING + (int)((to.x() - minX) * scaleX);
//                        int y2 = HEIGHT - PADDING - (int)((to.y() - minY) * scaleY);
//                        g2d.draw(new Line2D.Double(x1, y1, x2, y2));
//                    }
//                }
//            }
//        }
//
//        // Draw nodes
//        for (GNode node : graph.getNodes()) {
//            int x = PADDING + (int)((node.x() - minX) * scaleX);
//            int y = HEIGHT - PADDING - (int)((node.y() - minY) * scaleY);
//
//            // Draw node circle
//            if (node == graph.getStart()) {
//                g2d.setColor(Color.GREEN);
//            } else if (node == graph.getEnd()) {
//                g2d.setColor(Color.RED);
//            } else {
//                g2d.setColor(Color.BLUE);
//            }
//            g2d.fill(new Ellipse2D.Double(x - NODE_RADIUS, y - NODE_RADIUS,
//                    2 * NODE_RADIUS, 2 * NODE_RADIUS));
//
//            // Draw node label
//            g2d.setColor(Color.BLACK);
//            FontMetrics metrics = g2d.getFontMetrics();
//            String label = node.id() + " (" + node.x() + "," + node.y() + ")";
//            int labelX = x - metrics.stringWidth(label) / 2;
//            int labelY = y - NODE_RADIUS - 5;
//            g2d.drawString(label, labelX, labelY);
//        }
//
//        // Clean up and save
//        try {
//            ImageIO.write(image, "PNG", new File(filename));
//            System.out.println("Graph visualization saved to " + filename);
//            System.out.println(graph);
//        } catch (Exception e) {
//            System.err.println("Error saving visualization: " + e.getMessage());
//        } finally {
//            g2d.dispose();
//        }
//    }
//
//    // Overloaded method to allow calling without Dijkstra nodes
//    public static void saveGraphImage(NodeGraph graph, String filename) {
//        saveGraphImage(graph, filename, false);
//    }
//}