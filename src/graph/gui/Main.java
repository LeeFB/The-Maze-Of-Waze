package graph.gui;

import graph.dataStructure.DGraph;
import graph.dataStructure.Node;
import graph.utils.Point3D;

public class Main {

    public static void main(String[] args) {

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

        Window window = new Window(graph);
        window.setVisible(true);

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


    }
}
