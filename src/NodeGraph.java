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
//        System.out.println(graph);
//    }
//}

public class NodeGraph {
    public static final double INF = Double.MAX_VALUE;
    private final GNode start;
    private final GNode end;
    private Set<GNode> nodes;
    private Map<GNode, Map<GNode, Double>> adjacencyMap;

    public NodeGraph() {
        this.start = new GNode(99, 99, "Start");
        this.end = new GNode(0, 0, "End");
        this.nodes = new HashSet<>();
        this.adjacencyMap = new HashMap<>();

        addNode(start);
        addNode(end);
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

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (GNode n : nodes) {
            s.append(n).append(":\n\t").append(adjacencyMap.get(n)).append("\n\n");
        }
        return s.toString();
    }


    public Map<String, Object> dijkstra_route(){
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
        System.out.println(top.node);
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

        List<GNode> rt = new ArrayList<>();
        GNode cur = end;
        double dst = 0.0;
        while (!cur.equals(start)){
            rt.add(cur);
            dst += adjacencyMap.get(cur).get(addrs.get(cur).from);
            cur = addrs.get(cur).from;
        }
        rt.add(start);

        route.put("route", rt.reversed());
        route.put("distance", dst);
        return route;
    }

    public static void main(String[] args) {
        NodeGraph graph = new NodeGraph();
        GNode a = new GNode(-25, 44, "A");
        GNode b = new GNode(-1, 55, "B");
        GNode c = new GNode(15, 30, "C");
        GNode d = new GNode(-10, 20, "D");

        GNode ben = new GNode(27, 27, "Z");
        graph.addNode(ben);
        graph.addEdge(b, ben);
        graph.addEdge(ben, graph.getEnd());

        GNode xx = new GNode(81, 81, "XX");
        graph.addNode(xx);
        graph.addEdge(graph.getStart(), xx);

        GNode zz = new GNode(63, 54, "zz");
        graph.addNode(zz);
        graph.addEdge(xx, zz);
        graph.addEdge(zz, b);

        GNode cc = new GNode(72, 33, "cc");
        graph.addNode(cc);
        graph.addEdge(cc, zz);
        graph.addEdge(cc, ben);

        GNode dd = new GNode(45, 18, "dd");
        graph.addNode(dd);
        graph.addEdge(dd, graph.getEnd());

        GNode rr = new GNode(54, 9, "rr");
        graph.addNode(rr);
        graph.addEdge(rr, cc);
        graph.addEdge(rr, dd);
        graph.addEdge(rr, a);


        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);

        graph.addEdge(graph.getStart(), b);
        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(c, d);
        graph.addEdge(d, graph.getEnd());

        GraphVisualizer.saveGraphImage(graph, "/Users/benmeyers/Desktop/Fun/A_Star/src/graph.png");

        System.out.println(graph.dijkstra_route());

    }

}