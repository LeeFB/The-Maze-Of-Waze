package gameClient;

import de.micromata.opengis.kml.v_2_2_0.Link;
import gameComponent.Fruit;
import gameComponent.Robot;
import graph.algorithms.Graph_Algo;
import graph.dataStructure.DGraph;
import graph.dataStructure.node_data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @authors Lee Fingerhut and Rapheal Gozlan
 */
public class AutoDrive
{
	private DGraph graph;
	private Graph_Algo graphAlgo;
	private HashMap<Integer,List<node_data>> plan;

	/**
	 * constructor
	 */
	public AutoDrive(DGraph graph){
		graphAlgo = new Graph_Algo(graph);
		plan = new HashMap<>();
		this.graph = graph;
	}
	public void updateMap(){

	}

	public void setPath(Robot r, LinkedList<Fruit> fruits){
		int r_id = r.getID();
		int r_src = r.getSrcNode();
		Fruit f;


		f = closestFruit(r.getSrcNode(),fruits);

		List<node_data> path = graphAlgo.shortestPath(r_src, f.getEdge().getSrc());    //return the list of nosed to fruit
		plan.put(r_id,path);

	}
	public boolean shouldGo(int r_dest ,List<Fruit> fruits){
		return  findFruit(r_dest,fruits) != -1;

	}
	public int nextNode(Robot r, LinkedList<Fruit> fruits){
		int r_id = r.getID();
		int r_src = r.getSrcNode();
		int ans = -1;

		//at the beginning give a path
		if (!plan.containsKey(r_id)) {
			setPath(r,fruits);
		}



		//in case of empty path
		if (plan.get(r_id).isEmpty()) {
			setPath(r,fruits);
		}

		List<node_data> myPath = plan.get(r_id);
		int myDst = myPath.get(myPath.size() - 1).getKey();
		if (!shouldGo(myDst,fruits))
			setPath(r,fruits);

		if (plan.get(r_id).size() == 1) {
			ans = findFruit(r_src, fruits);
			if (ans != -1) {
				plan.get(r_id).remove(0);
				return ans;
			}
			else{
				setPath(r, fruits);
			}
		}



		ans = plan.get(r_id).get(1).getKey();
		plan.get(r_id).remove(0);
		return ans;
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
	 * @param r the robot
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


		assert ans != null;
		ans.setTag(true);
		return ans;
	}


	/**
	 * Checks which node is the closet to the robot
	 * @param fruits the list of fruit in game
	 * @param src the nodeID the robot is on
	 * @return the valuable/closest Fruit in graph
	 */
	public Fruit opFruit(int src, List<Fruit> fruits){
		double op = 0;
		double dist = 0;
		Fruit ans = null;

		for (Fruit f : fruits) {
			if (!f.getTag())
				dist = graphAlgo.shortestPathDist(src, f.getEdge().getSrc()) / f.getValue();
			if (dist > op)
				ans = f;
		}

		assert ans != null;
		ans.setTag(true);
		//setOccupied(ans,fruits);
		return ans;
	}


	private void setOccupied(Fruit ans,List<Fruit> fruits) {
		for (Fruit f : fruits) {
				if (f.getEdge().getSrc() == ans.getEdge().getSrc() &&
				    f.getEdge().getDest() == ans.getEdge().getDest()){
					f.setTag(true);
				}
		}

	}
//
//	/**
//	 *
//	 * @param fruits list of fruits
//	 * @param src source node
//	 * @return if its smarter to go an other edge and get 2 fruits in the return
//	 */
//	public int goAnotherOne(LinkedList<Fruit> fruits, int src) {
//		for (Fruit f : fruits) {
//			if (f.getEdge().getDest() == src)
//				return f.getEdge().getSrc();
//		}
//		return -1;
//	}

//	/**
//	 * Find the next node to move the robot in the automatic game by the method shortestPath
//	 * @param fruits list of fruits
//	 * @param src the source node
//	 * @return the src of the next node
//	 */
//	public int nextNodeClose(LinkedList<Fruit> fruits, int src) {
//		Fruit OP_fruit = closestFruit(src, fruits);
//
//		//if Robot is on src of fruit.edge.src go to dest
//		if (src == OP_fruit.getEdge().getSrc()) {
//			int ans = goAnotherOne(fruits,src);
//			if (ans == -1)
//				return OP_fruit.getEdge().getDest();
//			else
//				return ans;
//		}
//
//
//		List<node_data> shortestPath = graphAlgo.shortestPath(src, OP_fruit.getEdge().getSrc());
//		return shortestPath.get(1).getKey();
//	}
//
//	public int nextNodeValue(LinkedList<Fruit> fruits, int src){
//		Fruit OP_fruit = valueDistFruit(fruits,src);
//		//if Robot is on src of fruit.edge.src go to dest
//		if (src == OP_fruit.getEdge().getSrc()) {
//			int ans = goAnotherOne(fruits,src);
//
//			if (ans == -1)
//				return OP_fruit.getEdge().getDest();
//			else
//				return ans;
//		}
//
//		List<node_data> shortestPath = graphAlgo.shortestPath(src, OP_fruit.getEdge().getSrc());
//
//		return shortestPath.get(1).getKey();
//
//	}
//
//	public Fruit valueDistFruit(LinkedList<Fruit> fruits, int src){
//		double opFruit =  graphAlgo.shortestPathDist(src, fruits.get(0).getEdge().getSrc()) / fruits.get(0).getValue();
//		int index = 0;
//
//		for (int i = 1; i < fruits.size(); i++)	{
//			double dist =  graphAlgo.shortestPathDist(src, fruits.get(i).getEdge().getSrc()) / fruits.get(i).getValue();
//			if(dist < opFruit){
//				index = i;
//			}
//		}
//		return fruits.get(index);
//
//
//	}
}
