package graph.Tests;

import graph.algorithms.Graph_Algo;
import graph.dataStructure.DGraph;
import graph.dataStructure.Node;
import graph.dataStructure.node_data;
import graph.utils.Point3D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Testing the functions of Graph_Algo class
 * @authors Lee Fingerhut and Rapheal Gozlan
 */
class Graph_AlgoTest {

    static DGraph graph;
    static DGraph graph2;
    static DGraph graph3;
    static DGraph graph4;
    static Graph_Algo graph_algo1;
    static Graph_Algo graph_algo2;
    static Graph_Algo graph_algo3;
    static Graph_Algo graph_algo4;

    @BeforeAll
    static void start() {

        graph = new DGraph();
        graph2 = new DGraph();
        graph3 = new DGraph();
        graph4 = new DGraph();
        graph_algo1 = new Graph_Algo();
        graph_algo2 = new Graph_Algo();
        graph_algo3 = new Graph_Algo();
        graph_algo4 = new Graph_Algo();

        Node node1 = new Node(1, new Point3D(200, 400));
        Node node2 = new Node(2, new Point3D(200, 300));
        Node node3 = new Node(3, new Point3D(300, 200));
        Node node4 = new Node(4, new Point3D(400, 100));
        Node node5 = new Node(5, new Point3D(500, 100));

        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);

        graph.connect(2, 3, 2);
        graph.connect(1, 2, 3);
        graph.connect(4, 1, 3);

        graph2.addNode(node1);
        graph2.addNode(node2);
        graph2.addNode(node3);
        graph2.addNode(node4);
        graph2.addNode(node5);

        graph2.connect(1, 2, 2.5);
        graph2.connect(1, 3, 2);
        graph2.connect(2, 3, 5);
        graph2.connect(3, 4, 6);
        graph2.connect(4, 2, 6);
        graph2.connect(4, 1, 3);
        graph2.connect(4, 5, 3);
        graph2.connect(5, 1, 3);

        graph4.addNode(node1);
        graph4.addNode(node2);
        graph4.addNode(node3);

        graph4.connect(1, 2, 0.5);
        graph4.connect(1, 3, 8);
        graph4.connect(2, 1, 2);
        graph4.connect(2, 3, 0.5);
        graph4.connect(3, 1, 4);
        graph4.connect(3, 2, 3);

        graph_algo1.init(graph);
        graph_algo2.init(graph2);
        graph_algo3.init(graph3);
        graph_algo4.init(graph4);
    }


    @Test
    void testIsConnected() {

        assertFalse(graph_algo1.isConnected());
        assertTrue(graph_algo2.isConnected());
    }

    @Test
    void shortestPathDist() {
        assertEquals(11, graph_algo2.shortestPathDist(2, 4));
        assertEquals(2.5, graph_algo2.shortestPathDist(1, 2));
        assertEquals(3, graph_algo1.shortestPathDist(4, 1));
    }

    @Test
    void shortestPath() {
        int nodes[] = {2, 3, 4};
        List<node_data> s = graph_algo2.shortestPath(2, 4);
        Iterator<node_data> it = s.iterator();
        for (int i = 0; it.hasNext(); i++) {
            assertEquals(nodes[i], it.next().getKey());
            ;
        }
    }

    @Test
    void TSP() {
        int nodes[] = {1, 2, 3};
        List<Integer> targets = new LinkedList<Integer>();
        targets.add(1);
        targets.add(3);
        List<node_data> path = graph_algo4.TSP(targets);
        Iterator<node_data> it = path.iterator();
        for (int i = 0; it.hasNext(); i++) {
            assertEquals(nodes[i], it.next().getKey());
        }

    }


    @Test
    void copy() {
        graph3 = (DGraph) graph_algo1.copy();
        assertEquals(graph3.nodeSize(), graph.nodeSize());
        for (int i = 1; i <= graph3.nodeSize(); i++) {
            assertEquals(graph3.getNode(i).getInfo(), graph.getNode(i).getInfo());
            assertNotEquals(graph3.getNode(i).getInfo(), graph2.getNode(5).getInfo());

        }

    }

    @Test
    void save() {
        Graph_Algo algo_s = new Graph_Algo();
        Graph_Algo algo_s2 = new Graph_Algo();
        algo_s.init(graph2);
        algo_s.save("graph_1.txt");
        algo_s2.init(graph);
        algo_s2.save("graph_2.txt");
        assertEquals(algo_s.shortestPathDist(2, 4), graph_algo2.shortestPathDist(2, 4));
        assertNotEquals(algo_s2.isConnected(), algo_s.isConnected());
    }


    @Test
    void InitGraphFromFile() {
        Graph_Algo algo1 = new Graph_Algo();
        algo1.init(graph);
        algo1.save("graph_3.txt");
        Graph_Algo algo_2 = new Graph_Algo();
        algo_2.init("graph_3.txt");
        double path1 = algo1.shortestPathDist(1, 3);
        double path2 = algo_2.shortestPathDist(1, 3);
        assertEquals(path1, path2, 2.0);

    }


}