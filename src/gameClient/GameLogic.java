package gameClient;

import gameComponent.Fruit;
import graph.algorithms.Graph_Algo;
import graph.dataStructure.DGraph;
import graph.dataStructure.node_data;

import java.util.LinkedList;
import java.util.List;

public class GameLogic
{

	private DGraph graph;
	private Graph_Algo algo;



	/**
	 *
	 * @param
	 * graphStr graph
	 * construct Algo_graph
	 * list of robots - updaiting all the time
	 * as well for a list of fruit
	 * and as well U should have an UPDATE method
	 */
	public GameLogic(DGraph graph,String graphStr){
		this.graph = new DGraph();
		this.graph.init(graphStr);
		this.algo = new Graph_Algo(graph);

	}

	public LinkedList<Integer> getStartingPoint(LinkedList<Fruit> fruits){
		fruits.sort(Fruit::compare); //Sorting from most valued Fruit to less
		LinkedList<Integer> ans = new LinkedList<>();

		for (Fruit f: fruits)
			ans.add(f.getEdge().getSrc());		//add the fruit src to the list

		//System.out.println(ans);
		return ans;								//return ans list
	}

	/**
	 *
	 * @param fruits the list of fruit in game
	 * @param src the nodeID the robot is on
	 * @return the valuable/closest Fruit in graph
	 */
	public Fruit closestFruit(LinkedList<Fruit> fruits, int src){
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
	public int NextNode(LinkedList<Fruit> fruits, int src) {
		Fruit OP_fruit = closestFruit(fruits,src);
		System.out.println("the closet fruit is " + OP_fruit);

		fruits.sort(Fruit::compare);		//sort the list by Value

		//if Robot is on src of fruit.edge.src go to dest
		if (src == OP_fruit.getEdge().getSrc())
				return OP_fruit.getEdge().getDest();

		List<node_data> shortestPath = algo.shortestPath(src, OP_fruit.getEdge().getSrc());
			return shortestPath.get(1).getKey();
	}

//	private int get_fruit_dest(int src, int index, List<Fruit> fruit)	{
//		int _src = fruit.get(index).getEdge().getSrc();
//		int _dst = fruit.get(index).getEdge().getDest();
//
//		if(src == _src)
//			return _dst;
//
//		return _src;
//	}


//	private int NextNodeToGo(int src, List<Fruit> fruit){
//		double min = Double.POSITIVE_INFINITY;
//		int fruit_dest, fruit_index, fruit_to_go;
//		fruit_dest = fruit_index = fruit_to_go = 0;
//
//		for (int i = 0; i < fruit.size(); i++) {
//			if(fruit.get(i).edge(this.graph).getTag() == 0) && (this.algo.shortestPathDist(src, fruit.get(i).edge(this.graph).getSrc()) < min) ) {
//				fruit_to_go = 1;
//				fruit_index = i;
//				min = this.algo.shortestPathDist(src, fruit.get(i).edge(this.graph).getSrc());
//				fruit_dest = get_fruit_dest(src, i);
//			}
//		}
//		if(fruit_to_go == 0) {
//			fruit_dest = get_fruit_dest(src, 0);
//		}
//		fruit.get(fruit_index).edge(this.graph).setTag(1);
//		graph.getEdge(fruit.get(fruit_index).edge(this.graph).getDest(), fruit.get(fruit_index).edge(this.graph).getSrc()).setTag(1);
//		return this.algo.shortestPath(src, fruit_dest).get(1).getKey();
//	}

}
