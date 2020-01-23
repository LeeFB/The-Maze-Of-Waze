package gameClient;

import gameComponent.Fruit;
import gameComponent.Robot;
import graph.algorithms.Graph_Algo;
import graph.dataStructure.DGraph;
import graph.dataStructure.node_data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @authors Lee Fingerhut and Rapheal Gozlan
 */
public class AutoDrive
{
	private DGraph graph;
	private Graph_Algo graphAlgo;
	private HashMap<Integer,List<node_data>> plan;
	private int lastSrc;

	/**
	 * constructor
	 */
	public AutoDrive(DGraph graph){
		this.graph = graph;
		graphAlgo = new Graph_Algo(graph);
		plan = new HashMap<>();
		lastSrc = 0;
	}

	public void updateMap(){


	}

	public int whereMove(Robot r, LinkedList<Fruit> fruits){
		int r_id = r.getID();
		int r_src = r.getSrcNode();
		int ans = -1;

		if (r_id != 1) {
			//at the beginning give a path
			if (!plan.containsKey(r_id)) {
				Fruit f = closestFruit(r_src, fruits);
				System.out.println("Robot_ID: " + r_id + " is going to fruit on edge " + f.getEdge().getSrc() + "->" + f.getEdge().getDest());
				List<node_data> path = graphAlgo.shortestPath(r_src, f.getEdge().getSrc());    //return the list of nosed to fruit
				plan.put(r_id, path);
			}

			//in case of empty path
			if (plan.get(r_id).size() == 0) {
				Fruit f = closestFruit(r_src, fruits);
				List<node_data> path = graphAlgo.shortestPath(r_src, f.getEdge().getSrc());    //return the list of nosed to fruit.
				plan.remove(r_id);
				return findFruit(r_src, fruits);
			}

			if (plan.get(r_id).size() == 1) {
				ans = findFruit(r_src, fruits);
				plan.get(r_id).remove(0);
				return ans;
			}

			ans = plan.get(r_id).get(1).getKey();
			plan.get(r_id).remove(0);
			return ans;
		}
		else
			return nextNodeClose(fruits,r_src);



	}

	private int findFruit(int r_src,List<Fruit> fruits) {
		for (Fruit f : fruits){
			if (f.getEdge().getSrc() == r_src)
				return f.getEdge().getDest();
		}
		return -1;
	}



	/**
	 * Find the the src nodes of all the list fruits
	 * @param fruits - list of fruit in game
	 * @return list of src nodes rated from best value to lowest
	 */
	public LinkedList<Integer> getStartingPoint(LinkedList<Fruit> fruits){
		fruits.sort(Fruit::compare);//Sorting from most valued Fruit to less
		LinkedList<Integer> ans = new LinkedList<>();
		for (Fruit f: fruits) {
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
	public Fruit closestFruit(int src, List<Fruit> fruits){
		double op = Double.MAX_VALUE;
		double dist = 0;
		Fruit ans = null;
		for (Fruit f : fruits) {
			if (!f.getTag())
				dist = graphAlgo.shortestPathDist(src, f.getEdge().getSrc());
			if (dist < op)
				ans = f;
		}
		setOccupied(ans,fruits);
		return ans;
	}

	private void setOccupied(Fruit ans,List<Fruit> fruits) {
		for (Fruit f : fruits) {
			System.out.println();
			if (f.equals(ans))
				f.setTag(true);
		}

	}


	/**
	 *
	 * @param fruits list of fruits
	 * @param src source node
	 * @return if its smarter to go an other edge and get 2 fruits in the return
	 */
	public int goAnotherOne(LinkedList<Fruit> fruits, int src) {
		for (Fruit f : fruits) {
			if (f.getEdge().getDest() == src)
				return f.getEdge().getSrc();
		}
		return -1;
	}




	/**
	 * Find the next node to move the robot in the automatic game by the method shortestPath
	 * @param fruits list of fruits
	 * @param src the source node
	 * @return the src of the next node
	 */
	private int nextNodeClose(LinkedList<Fruit> fruits, int src) {
		Fruit OP_fruit = closestFruit(src, fruits);

		//if Robot is on src of fruit.edge.src go to dest
		if (src == OP_fruit.getEdge().getSrc()) {
			int ans = goAnotherOne(fruits,src);
			if (ans == -1)
				return OP_fruit.getEdge().getDest();
			else
				return ans;
		}


		List<node_data> shortestPath = graphAlgo.shortestPath(src, OP_fruit.getEdge().getSrc());
		return shortestPath.get(1).getKey();
	}


	private int nextNodeValue(LinkedList<Fruit> fruits, int src){
		Fruit OP_fruit = valueDistFruit(fruits,src);
		//if Robot is on src of fruit.edge.src go to dest
		if (src == OP_fruit.getEdge().getSrc()) {
			int ans = goAnotherOne(fruits,src);

			if (ans == -1)
				return OP_fruit.getEdge().getDest();
			else
				return ans;
		}

		List<node_data> shortestPath = graphAlgo.shortestPath(src, OP_fruit.getEdge().getSrc());

		return shortestPath.get(1).getKey();

	}

	public int nextNode(LinkedList<Fruit> fruits, int src){
		int ans;

		if (lastSrc != src)
			ans = nextNodeValue(fruits,  src);
		else
			ans = nextNodeClose(fruits, src);

		lastSrc = src;
		return ans;



	}
	public Fruit valueDistFruit(LinkedList<Fruit> fruits, int src){
		double opFruit =  graphAlgo.shortestPathDist(src, fruits.get(0).getEdge().getSrc()) / fruits.get(0).getValue();
		int index = 0;

		for (int i = 1; i < fruits.size(); i++)	{
			double dist =  graphAlgo.shortestPathDist(src, fruits.get(i).getEdge().getSrc()) / fruits.get(i).getValue();
			if(dist < opFruit){
				index = i;
			}
		}
		return fruits.get(index);


	}







}
