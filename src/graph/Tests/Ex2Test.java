package graph.Tests;

import graph.algorithms.Graph_Algo;
import graph.algorithms.graph_algorithms;
import graph.dataStructure.DGraph;
import graph.dataStructure.Node;
import graph.dataStructure.graph;
import graph.gui.Window;
import graph.utils.Point3D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * EX2 Structure test:
 * 1. make sure your code compile with this dummy test (DO NOT Change a thing in this test).
 * 2. the only change require is to run your GUI window (see line 64).
 * 3. EX2 auto-test will be based on such file structure.
 * 4. Do include this test-case in you submitted repository, in folder Tests (under src).
 * 5. Do note that all the required packages are imported - do NOT use other
 *
 * @author boaz.benmoshe
 */
class Ex2Test {
    private static graph _graph;
    private static graph_algorithms _alg;
    public static final double EPS = 0.001;
    private static Point3D min = new Point3D(0, 0, 0);
    private static Point3D max = new Point3D(100, 100, 0);

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        _graph = tinyGraph();
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testConnectivity() {
        _alg = new Graph_Algo(_graph);
        boolean con = _alg.isConnected();
        if (!con) {
            fail("shoulbe be connected");
        }
    }

    @Test
    void testgraph() {
        boolean ans = drawGraph(_graph);
        assertTrue(ans);
    }

    private static graph tinyGraph() {
        graph ans = new DGraph();
        return ans;
    }

    boolean drawGraph(graph g) {

        Node node1 = new Node(1, new Point3D(0, 0));
        Node node2 = new Node(2, new Point3D(10, -5));
        Node node3 = new Node(3, new Point3D(15, -45));
        Node node4 = new Node(4, new Point3D(-58, 10));
        Node node5 = new Node(5, new Point3D(-91, 15));
        Node node6 = new Node(6, new Point3D(37, 50));
        Node node7 = new Node(7, new Point3D(20, 10));
        Node node8 = new Node(8, new Point3D(50, 21));
        Node node9 = new Node(9, new Point3D(30, 18));

        DGraph graph = new DGraph();
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);
        graph.addNode(node5);
        graph.addNode(node6);
        graph.addNode(node7);
        graph.addNode(node8);
        graph.addNode(node9);

        graph.connect(1, 2, 10);
        graph.connect(2, 1, 2.5);
        graph.connect(3, 1, 5);
        graph.connect(3, 4, 59.6);
        graph.connect(4, 3, 9.6);
        graph.connect(6, 8, 56.2);
        graph.connect(5, 8, 89.7);
        graph.connect(9, 7, 124.8);
        graph.connect(7, 1, 125.6);
        graph.connect(9, 3, 21.5);
        graph.connect(1, 5, 28);
        graph.connect(2, 3, 9.6);
        graph.connect(3, 8, 56.2);
        graph.connect(4, 8, 89.7);
        graph.connect(5, 7, 124.8);
        graph.connect(6, 1, 125.6);
        graph.connect(9, 3, 21.5);
        graph.connect(1, 5, 28);


        Window window = new Window(graph);
        window.setVisible(true);
        return true;

    }

}