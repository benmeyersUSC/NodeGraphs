import org.w3c.dom.Node;

import java.util.*;

//public class NodeGraph {
//    private GNode start;
//    private GNode end;
//    // This will store all nodes in the graph
//    private Set<GNode> nodes;
//    // This maps each node to its neighbors and their edge weights
//    private Map<GNode, Map<String, Map<GNode, Double>>> adjacencyMap;
//
//    public NodeGraph() {
//        this.start = new GNode(99, 99, "Start");
//        this.end = new GNode(0, 0, "End");
//        this.nodes = new HashSet<>();
//        this.adjacencyMap = new HashMap<>();
//
//        // Add initial nodes
//        addNode(start);
//        addNode(end);
//    }
//
//    public GNode getStart(){
//        return start;
//    }
//
//    public void addNode(GNode node) {
//        nodes.add(node);
//        // Initialize the adjacency map for this node if it doesn't exist
//        if (!adjacencyMap.containsKey(node)) {
//            adjacencyMap.put(node, new HashMap<String, Map<GNode, Double>>());
//        }
//    }
//
//    public void addEdge(GNode from, GNode to) {
//        // Ensure both nodes are in the graph
//        addNode(from);
//        addNode(to);
//        double distance = getPotentialNodes(from, to);
//
//        HashMap<GNode, Double> inFromTo = new HashMap<>();
//        inFromTo.put(to, distance);
//        adjacencyMap.get(from).put("to", inFromTo);
//
//        HashMap<GNode, Double> inToFrom = new HashMap<>();
//        inToFrom.put(from, distance);
//        adjacencyMap.get(to).put("from", inToFrom);
//
//    }
//
//    public Set<GNode> getNeighbors(GNode node) {
//        Set<GNode> neighbors = new HashSet<>();
//        try{
//            neighbors.addAll(adjacencyMap.get(node).get("to").keySet());
//        }
//        catch (Exception ignored){
//
//        }
//        try{
//            neighbors.addAll(adjacencyMap.get(node).get("from").keySet());
//        }
//        catch (Exception ignored){
//
//        }
//        return neighbors;
//    }
//
//    public Double getEdgeWeight(GNode from, GNode to) {
//        return getPotentialNodes(from, to);
//    }
//
//    private double getPotential(GNode node) {
//       return getPotentialNodes(node, end);
//    }
//
//    private double getPotentialNodes(GNode a, GNode b) {
//        return Math.sqrt(
//                Math.pow(b.getX() - a.getX(), 2) +
//                        Math.pow(b.getY() - a.getY(), 2)
//        );
//    }
//
//    public String toString(){
//        String s = "";
//        for (GNode n: nodes){
//            s += n + ": " + adjacencyMap.get(n) + "\n";
//        }
//        return s;
//    }
//
//    public static void main (String[] args){
//        NodeGraph graph = new NodeGraph();
//        GNode x = new GNode(-25, 44, "A");
//        GNode y = new GNode(-1, 55, "B");
//        graph.addNode(x);
//        graph.addEdge(graph.getStart(), x);
//        graph.addNode(y);
////        graph.addEdge(graph.getStart(), y);
//
//
//    }
//}

public class NodeGraph {
    public static final double INF = Double.MAX_VALUE;
    private final GNode start;
    private final GNode end;
    private Set<GNode> nodes;
    private Map<GNode, Map<GNode, Double>> adjacencyMap;
    private Set<GNode> dijkstraNodes;

    public NodeGraph(double strtX, double strtY) {
        this.start = new GNode(strtX, strtY, "Start");
        this.end = new GNode(0, 0, "End");
        this.nodes = new HashSet<>();
        this.adjacencyMap = new HashMap<>();

        addNode(start);
        addNode(end);
    }

    public NodeGraph(){
        this(99, 99);
    }

    public GNode getStart() {
        return start;
    }

    public GNode getEnd() {
        return end;
    }

    public void addNode(GNode node) {
        nodes.add(node);
        if (!adjacencyMap.containsKey(node)) {
            adjacencyMap.put(node, new HashMap<>());
        }
    }

    public void addEdge(GNode from, GNode to) {
        // Ensure both nodes are in the graph
        addNode(from);
        addNode(to);
        double distance = getPotential(from, to);

        adjacencyMap.computeIfAbsent(from, k -> new HashMap<>()).put(to, distance);
        adjacencyMap.computeIfAbsent(to, k -> new HashMap<>()).put(from, distance);

    }

    public Set<GNode> getNeighbors(GNode node) {
        return new HashSet<>(adjacencyMap.get(node).keySet());
    }

    private double getPotential(GNode a, GNode b) {
        return Math.sqrt(
                Math.pow(b.x() - a.x(), 2) +
                        Math.pow(b.y() - a.y(), 2)
        );
    }

    public Set<GNode> getNodes() {
        return nodes;
    }

    public Set<GNode> getDijkstraNodes(){return dijkstraNodes;}

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (GNode n : nodes) {
            s.append(n).append(":\n\t").append(adjacencyMap.get(n)).append("\n\n");
        }
        return s.toString();
    }

    public void ogNodeGraph(){


        GNode a = new GNode(35, 63, "A");
        GNode b = new GNode(-1, 55, "B");
        GNode c = new GNode(15, 30, "C");
        GNode d = new GNode(-10, 20, "D");
        GNode ff = new GNode(18, 90, "ff");
        GNode tt = new GNode(54, 72, "tt");
        GNode hh = new GNode(33, 81, "hh");

        GNode ben = new GNode(45, 45, "Z");
        this.addNode(ben);
        this.addEdge(b, ben);
        this.addEdge(ben, this.getEnd());

        GNode xx = new GNode(81, 81, "XX");
        this.addNode(xx);
        this.addEdge(this.getStart(), xx);

        GNode zz = new GNode(63, 54, "zz");
        this.addNode(zz);
        this.addEdge(xx, zz);
        this.addEdge(zz, b);

        GNode cc = new GNode(99, 27, "cc");
        this.addNode(cc);
        this.addEdge(cc, zz);
        this.addEdge(cc, ben);

        GNode dd = new GNode(45, 18, "dd");
        this.addNode(dd);
        this.addEdge(dd, this.getEnd());

        GNode rr = new GNode(54, 9, "rr");
        this.addNode(rr);
        this.addEdge(rr, cc);
        this.addEdge(rr, dd);
        this.addEdge(rr, a);
        this.addEdge(ff, b);
        this.addEdge(ff, start);


        this.addNode(a);
        this.addNode(b);
        this.addNode(c);
        this.addNode(d);

        this.addEdge(this.getStart(), hh);
        this.addEdge(hh, a);

        this.addEdge(this.getStart(), tt);
        this.addEdge(tt, cc);
        this.addEdge(a, b);
        this.addEdge(b, c);
        this.addEdge(c, d);
        this.addEdge(d, this.getEnd());
    }

    public Map<String, Object> dijkstraRoute(){

        // "route": List<Node>, "distance": double
        Map<String, Object> route = new HashMap<>();
        // queue of nodes to be visited
        PriorityQueue<DijkstraQueueNode> queue = new PriorityQueue<>(new DistanceComparator());
        // Dijkstra Address Book
        Map<GNode, DijkstraQueueNode> addrs = new HashMap<>();

        // start node has distance of 0.0
        DijkstraQueueNode strt = new DijkstraQueueNode(start, 0.0, null);
        // add to queue
        queue.add(strt);

        addrs.put(start, new DijkstraQueueNode(start, 0.0));
        // for each node that isn't start, add its map to queue
        for (GNode n: nodes){ // nodes is a Set
            if (!n.equals(start)){
                DijkstraQueueNode nd = new DijkstraQueueNode(n, INF);
                queue.add(nd);
                addrs.put(n, nd);
            }
        }

        // take the top Node (should be start to start)
        DijkstraQueueNode top = queue.poll();
        while (!top.node.equals(end)){ // until we see the end
            for (GNode nd: getNeighbors(top.node)){
                DijkstraQueueNode neighb = addrs.get(nd);
                double neighbFromStart = adjacencyMap.get(top.node).get(nd) + top.distance;
                if (neighbFromStart < neighb.distance){
                    queue.remove(neighb);
                    neighb.distance = neighbFromStart;
                    neighb.from = top.node;
                    queue.add(neighb);
                }
            }
            top = queue.poll();
        }

        dijkstraNodes = new HashSet<>();
        List<GNode> rt = new ArrayList<>();
        GNode cur = end;
        double dst = 0.0;
        while (!cur.equals(start)){
            dijkstraNodes.add(cur);
            rt.add(cur);
            dst += adjacencyMap.get(cur).get(addrs.get(cur).from);
            cur = addrs.get(cur).from;
        }
        rt.add(start);
        dijkstraNodes.add(start);

        route.put("route", rt.reversed());
        route.put("distance", dst);
        return route;
    }

    public static NodeGraph randomNodeGraph(int numNodes) {
        Random r = new Random();
        final double MAX_X = 100;
        final double MAX_Y = 100;
        final double MIN_X = 50;
        final double MIN_Y = 50;

        NodeGraph graph = new NodeGraph(MAX_X, MAX_Y);
        GNode startNode = graph.getStart();
        GNode endNode = graph.getEnd();

        if (numNodes < 15) numNodes = 15;
        List<GNode> addedNodes = new ArrayList<>();
        addedNodes.add(startNode);
        addedNodes.add(endNode);

        // Create edge nodes first
        GNode topEdge = new GNode(75, MAX_Y - 5, "TopEdge");
        GNode rightEdge = new GNode(MAX_X - 5, 75, "RightEdge");
        GNode cornerEdge = new GNode(MAX_X - 10, MAX_Y - 10, "CornerEdge");

        graph.addNode(topEdge);
        graph.addNode(rightEdge);
        graph.addNode(cornerEdge);

        graph.addEdge(startNode, cornerEdge);
        graph.addEdge(cornerEdge, topEdge);
        graph.addEdge(cornerEdge, rightEdge);

        addedNodes.addAll(Arrays.asList(topEdge, rightEdge, cornerEdge));

        // Add nodes in different regions
        while (addedNodes.size() < numNodes) {
            GNode newNode;
            double x, y;

            // Choose a region pattern
            switch (r.nextInt(4)) {
                case 0: // Top edge region
                    x = MIN_X + r.nextDouble() * (MAX_X - MIN_X);
                    y = MAX_Y - r.nextDouble() * 15;
                    break;
                case 1: // Right edge region
                    x = MAX_X - r.nextDouble() * 15;
                    y = MIN_Y + r.nextDouble() * (MAX_Y - MIN_Y);
                    break;
                case 2: // Bottom region
                    x = MIN_X + r.nextDouble() * (MAX_X - MIN_X);
                    y = MIN_Y + r.nextDouble() * 15;
                    break;
                default: // Scattered away from y=x
                    if (r.nextBoolean()) {
                        x = MIN_X + r.nextDouble() * 20;
                        y = MIN_Y + r.nextDouble() * (MAX_Y - MIN_Y);
                    } else {
                        x = MIN_X + r.nextDouble() * (MAX_X - MIN_X);
                        y = MAX_Y - r.nextDouble() * 20;
                    }
            }

            newNode = new GNode(x, y, "Node_" + addedNodes.size());
            graph.addNode(newNode);

            // Connect to 2-3 existing nodes
            int connections = r.nextInt(2) + 2;
            List<GNode> shuffledNodes = new ArrayList<>(addedNodes);
            Collections.shuffle(shuffledNodes);

            for (int i = 0; i < connections && i < shuffledNodes.size(); i++) {
                graph.addEdge(newNode, shuffledNodes.get(i));
            }

            addedNodes.add(newNode);
        }

        // Add some strategic cross-connections
        for (int i = 0; i < numNodes / 4; i++) {
            GNode node1 = addedNodes.get(r.nextInt(addedNodes.size()));
            GNode node2 = addedNodes.get(r.nextInt(addedNodes.size()));

            if (!node1.equals(node2) &&
                    !graph.getNeighbors(node1).contains(node2)) {
                graph.addEdge(node1, node2);
            }
        }

        return graph;
    }

    public static void main(String[] args) {

//        NodeGraph graph = NodeGraph.randomNodeGraph(10);
        NodeGraph graph = new NodeGraph();
        graph.ogNodeGraph();

        GraphVisualizer.saveGraphImage(graph, "graph.png", true);


    }

}
