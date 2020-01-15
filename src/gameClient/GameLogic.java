package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Server.Game_Server;
import Server.game_service;
import Server.robot;
import gameComponent.*;
import graph.algorithms.Graph_Algo;
import graph.dataStructure.*;


public class GameLogic
{

	private DGraph graph;
	private Graph_Algo algo;


	/**
	 * 
	 * @param gameLocig
	 * get graph 
	 * construct Algo_graph
	 * list of robots - updaiting all the time
	 * as well for a list of fruit
	 * and as well U should have an UPDATE method
	 */
	public GameLogic(DGraph graph)
	{
		this.graph   = graph;
		this.algo = algo;

	}

	public LinkedList<Integer> getStartingPoint(LinkedList<Fruit> fruits)
	{
		LinkedList<Integer> ans = new LinkedList<Integer>();
		for (Fruit f: fruits)
			ans.add(f.getEdge().getSrc());
		return ans;
	}

	public int NextNode(LinkedList<Fruit> fruits, Robot r)                                                                                       
	{
		double min = algo.shortestPathDist(r.getDestNode(), fruits.get(0).getEdge().getSrc());
		int index = 0;
		for (int i = 0; i < fruits.size(); i++)
		{
			double dist = algo.shortestPathDist(r.getSrcNode(), fruits.get(i).getEdge().getSrc());
			if(dist < min)
			{
				index = i;
			}
		}
		LinkedList<node_data> WhereToGo = (LinkedList<node_data>) algo.shortestPath(r.getSrcNode(), fruits.get(index).getEdge().getSrc());
		node_data theFirstNode = WhereToGo.get(0);
		int nextNodeToGo = theFirstNode.getKey();
		return nextNodeToGo;
	}
}
