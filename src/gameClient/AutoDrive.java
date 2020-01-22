package gameClient;

import gameComponent.Fruit;
import graph.algorithms.Graph_Algo;
import graph.dataStructure.DGraph;
import graph.dataStructure.edge_data;
import graph.dataStructure.node_data;
import graph.utils.Point3D;

import java.util.LinkedList;
import java.util.List;

/**
 * @authors Lee Fingerhut and Rapheal Gozlan
 */
public class AutoDrive
{
	private DGraph graph;
	private Graph_Algo algo;

	/** 
	 * constructor 
	 */
	public AutoDrive(DGraph graph){
		this.graph = graph;
		algo = new Graph_Algo(graph);
	}

	/**
	 * Find the the src nodes of all the list fruits 
	 * @param fruits - list of fruit in game
	 * @return list of src nodes rated from best value to lowest
	 */
	public LinkedList<Integer> getStartingPoint(LinkedList<Fruit> fruits){
		setEdgedToFruits(fruits);
		fruits.sort(Fruit::compare);//Sorting from most valued Fruit to less
		LinkedList<Integer> ans = new LinkedList<>();
		for (Fruit f: fruits) {
			System.out.println(f.getValue() + ",");
			ans.add(f.getEdge().getSrc());//add the fruit src to the list
		}
		return ans;//return ans list
	}

	/**
	 * Checks which node is the closet to the robot 
	 * @param fruits the list of fruit in game
	 * @param src the nodeID the robot is on
	 * @return the valuable/closest Fruit in graph
	 */
	public Fruit closestFruit(LinkedList<Fruit> fruits, int src){
		setEdgedToFruits(fruits);
		fruits.sort(Fruit::compare);
		double opFruit = algo.shortestPathDist(src, fruits.get(0).getEdge().getSrc());
		int index = 0;
		for (int i = 1; i < fruits.size(); i++)	{
			double dist =  algo.shortestPathDist(src, fruits.get(i).getEdge().getSrc());
			if(dist < opFruit){
				index = i;
			}
		}
		return fruits.get(index);
	}

	/**
	 * Find the next node to move the robot in the automatic game by the method shrotestPath
	 * @param fruits 
	 * @param src
	 * @return the src of the next node
	 */
	public int NextNode(LinkedList<Fruit> fruits, int src) {
		setEdgedToFruits(fruits);
		Fruit OP_fruit = closestFruit(fruits,src);
		fruits.sort(Fruit::compare);//sort the list by Value
		//if Robot is on src of fruit.edge.src go to dest
		if (src == OP_fruit.getEdge().getSrc())
			return OP_fruit.getEdge().getDest();
		List<node_data> shortestPath = algo.shortestPath(src, OP_fruit.getEdge().getSrc());
		return shortestPath.get(1).getKey();
	}

	/**
	 * @param p - a point location on the graph
	 * @param type - the type of the fruit we want to associate with the edge
	 * @return - the corresponding edge
	 */
	private edge_data findEdge(Point3D p, int type) {
		for (node_data node : graph.getV()) {					//go over all nodes on graph
			for (edge_data edge : graph.getE(node.getKey())) {	//go over all edges corresponding node

				Point3D src = graph.getNode(edge.getSrc()).getLocation();
				Point3D dst = graph.getNode(edge.getDest()).getLocation();

				double edge_len = src.distance3D(dst);			//the edge len
				double pTop_len = p.distance3D(src) + p.distance3D(dst);	//the len between each node in graph and the @param p
				double EPS = 0.00000000000001;

				if (Math.abs(edge_len - pTop_len) < EPS)
					if ((type == 1 && edge.getSrc() < edge.getDest()))
						return edge;
					else if(type == -1 && edge.getSrc() > edge.getDest())
						return edge;
			}
		}
		System.out.println("No corresponding edge");
		return null;
	}

	/**
	 * @param fruits - set the list of fruit in game to a current edge
	 *in graph
	 */
	private void setEdgedToFruits(LinkedList<Fruit> fruits){
		for (Fruit f : fruits)
			if (f.getEdge() == null)
				f.setEdge(findEdge(f.getLocation(),f.getType()));
	}
}
