package algorithms;

import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @authors Lee Fingerhut and Rapheal Gozlan
 */
public class Graph_Algo implements graph_algorithms, Serializable{

	private graph algo;
	private graph d; // Graph for calculating distances
	private HashMap<Integer, Queue<Integer>> pi;

	/*
	 * Constructors 
	 */
	public Graph_Algo() 
	{
		this.algo = null;
	}
	public Graph_Algo(graph graph_1)
	{
		this.algo = graph_1;
	}
	/*
	 * get the graph.
	 */
	public graph getGraph()
	{
		return this.algo;
	}
	/*
	 * initialize this graph 
	 */
	@Override
	public void init(graph g)
	{
		this.algo = g;
	}

	/**
	 * init graph from file
	 */
	@Override
	public void init(String file_name) 
	{
		try {
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);

			this.algo = (graph) in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized"); 
		} 
		catch(IOException ex) 
		{ 
			throw new RuntimeException("IOException in init routine");
			//System.out.println("IOException is caught"); 
		} 
		catch(ClassNotFoundException ex) 
		{ 
			throw new RuntimeException("ClassNotFoundException in init routine");
			//System.out.println("ClassNotFoundException is caught"); 
		} 

	}
	/**
	 * save graph to file
	 */

	@Override
	public void save(String file_name) {
		try {
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(this.algo);
			out.close();
			file.close();

			System.out.println("Object has been serialized"); 
		}   
		catch(IOException ex) 
		{ 
			throw new RuntimeException("IOException in save routine");
			//System.out.println("IOException is caught"); 
		} 
	}

	/**
	 * Define all the nodes as unvisited
	 */
	private void resetTags()
	{
		Collection<node_data> nodes = algo.getV();
		if (nodes == null)
			return;
		Iterator<node_data> it = nodes.iterator();
		while(it.hasNext())
		{
			it.next().setTag(-1);
		}
	}

	/**
	 * Depth first search - start from key, and check wich nodes are connect to this key.
	 * @param x = key
	 */
	private void dfs(int x)
	{
		algo.getNode(x).setTag(1);

		Collection<edge_data> edges = algo.getE(x);
		if (edges == null)
			return;
		Iterator<edge_data> it = edges.iterator();
		while (it.hasNext())
		{
			edge_data e = it.next();
			int dest = e.getDest();
			if(algo.getNode(dest).getTag() == -1)
				dfs(dest);
		}
		return;
	}
	/**
	 * check if all the nodes visited
	 * @return true
	 */
	private boolean allVisit()
	{
		Collection<node_data> nodes = algo.getV();
		if (nodes == null)
			return true;
		Iterator<node_data> it = nodes.iterator();
		while(it.hasNext())
		{
			if (it.next().getTag() == -1 )
				return false;
		}
		return true;
	}

	/**
	 * check if the graph is Connected
	 */
	@Override
	public boolean isConnected()
	{
		Collection<node_data> nodes = algo.getV();
		if (nodes == null)
			return true; // Paradigm
		Iterator<node_data> it = nodes.iterator();
		while(it.hasNext())
		{
			resetTags();
			dfs(it.next().getKey());
			if (!allVisit())
				return false;
		}
		return true;
	}

	/*
	 * initialize all distances to infinity
	 */
	private void setDistancesToInf()
	{
		Collection<node_data> nodes = algo.getV();
		if (nodes == null)
			return;
		Iterator<node_data> it = nodes.iterator();
		while(it.hasNext())
		{
			d.getNode(it.next().getKey()).setWeight(Double.POSITIVE_INFINITY);
		}
	}

	/**
	 * find the next node to go
	 * go to the node with minimal distance whom we didn't visit
	 * @param src - current node
	 * @return - next node to go
	 */
	private int nextDest(int src)
	{
		double min = Double.POSITIVE_INFINITY;
		int mind = src;
		Collection<node_data> nodes = d.getV();
		Iterator<node_data> it = nodes.iterator();
		while(it.hasNext())
		{
			int key = it.next().getKey();
			if ((algo.getNode(key).getTag() == -1) && d.getNode(key).getWeight() < min)
			{
				min = d.getNode(key).getWeight();
				mind = key;
			}
		}
		if (min == Double.POSITIVE_INFINITY)
			return -1;
		return mind;
	}

	/**
	 * clone a Queue
	 * @param q - Queue to copy
	 * @return
	 */
	private Queue<Integer> cloneQ(Queue<Integer> q)
	{
		Queue<Integer> r = new LinkedList<Integer>();
		Iterator<Integer> it = q.iterator();
		while(it.hasNext())
		{
			r.add(it.next());
		}
		return r;
	}

	private int Dijkstra(int src, boolean keep_track)
	{
		/*
		 * stop condition - when all nodes are visited.
		 */
		if (allVisit())
		{
			return 0;
		}
		/*
		 * set visit in current node to true
		 */
		algo.getNode(src).setTag(1);
		/*
		 * try to get edges of src
		 */
		try {
			Collection<edge_data> edges = algo.getE(src);
			if (edges == null)
			{
				return 0;
			}
			Iterator<edge_data> it = edges.iterator();

			/*
			 * iterate over edges
			 */
			while (it.hasNext())
			{
				/*
				 * get edge weight, and dest weight
				 */
				edge_data e = it.next();
				double we = e.getWeight();
				int dest = e.getDest();
				double wn = algo.getNode(dest).getWeight();

				/*
				 * queue to keep track of path
				 */
				Queue<Integer> q = new LinkedList<Integer>();
				if (keep_track)
					q = pi.get(dest);

				/*
				 * when we find new SP
				 */
				if (d.getNode(dest).getWeight() > d.getNode(src).getWeight() + we + wn)
				{
					if (keep_track)
					{
						/*
						 * new path is: path to src + dest
						 */
						q = cloneQ(pi.get(src));
						q.add(dest);
						pi.put(dest, q);
					}

					/*
					 * new distance = src distance + edge weight + dest weight
					 */
					d.getNode(dest).setWeight(d.getNode(src).getWeight() + we + wn);
				}
			}

			/*
			 * we go the node with minimal distance wich we didn't visit(mind)
			 */
			it = edges.iterator();
			int mind, rc;
			while (it.hasNext())
			{
				mind = nextDest(src);
				rc = Dijkstra(mind, keep_track);
				if (rc == 0)
					return 0;
				else
					algo.getNode(mind).setTag(1);
				it.next();
			}
			return 0;

		} catch (Exception e) {
			/*
			 * we get here when src has no nodes
			 * do nothing
			 */
		}
		return 0;
	}


	/*
	 * get 2 nodes, and find the shortest path by the function Dijkstra.
	 */
	@Override
	public double shortestPathDist(int src, int dest)
	{
		if (algo.getNode(src) == null || algo.getNode(dest) == null)
			throw new RuntimeException("ERR: no path exist");
		resetTags();
		d = this.copy();
		setDistancesToInf();
		d.getNode(src).setWeight(0);
		int rc = Dijkstra(src, false);
		if (rc != 0 || d.getNode(dest).getWeight() == Double.POSITIVE_INFINITY)
			throw new RuntimeException("ERR: no path from " + src + " to " + dest);
		return  d.getNode(dest).getWeight();
	}

	/**
	 * finds the shorts path between src and dest.
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest)
	{
		if (algo.getNode(src) == null || algo.getNode(dest) == null)
			throw new RuntimeException("ERR: no path exist");

		/*
		 * keep track of path from src to each node
		 * pi in 'key' is the path to node 'key'
		 * that path is a queue of nodes
		 */
		pi = new HashMap<Integer, Queue<Integer>>();
		Collection<node_data> nodes = algo.getV();
		Iterator<node_data> it = nodes.iterator();
		while(it.hasNext())
		{
			/*
			 * initiate the queues
			 * for start- each queue contains the src node
			 */
			Queue<Integer> q = new LinkedList<Integer>();
			q.add(src);
			pi.put(it.next().getKey(), q);
		}

		/*
		 * we only need to visit each node once
		 * hence, keep track were we visited
		 */
		resetTags();

		/*
		 * we keep track of the distances to each node.
		 */
		d = this.copy();
		setDistancesToInf();
		d.getNode(src).setWeight(0);


		/*
		 * run Dijakstra and keep track of path
		 */
		int rc = Dijkstra(src, true);
		if (rc != 0 || d.getNode(dest).getWeight() == Double.POSITIVE_INFINITY)
			throw new RuntimeException("ERR: no path from " + src + " to " + dest);
		/*
		 * now queue of dest is the SP from src to dest
		 */
		Queue<Integer> path = pi.get(dest);
		Iterator<Integer> it1 = path.iterator();

		/*
		 * make list of nodes in path
		 */
		List<node_data> Lpath = new LinkedList<node_data>();
		while(it1.hasNext())
		{
			Lpath.add(algo.getNode(it1.next()));
		}
		return Lpath;
	}

	/*
	 * finds some path that connects all the targets(it isn't have to be the shorts path between the targets).
	 * 
	 */
	@Override
	public List<node_data> TSP(List<Integer> targets)
	{
		List<node_data> path = new LinkedList<node_data>();

		if (targets.size() == 0)
			return path;

		if (targets.size() == 1)
		{
			path.add(algo.getNode(targets.get(0)));
			return path;
		}

		path.addAll(shortestPath(targets.get(0), targets.get(1)));
		for (int i = 1; i < targets.size()-1; i++)
		{
			List<node_data> previous_path = shortestPath(targets.get(i), targets.get(i+1));
			previous_path.remove(0);
			path.addAll(previous_path);
		}
		if (path.size() == 0)
			throw new RuntimeException("ERR: no valid path between all targets !");

		return path;
	}

	/**
	 * deep copy other graph
	 */
	@Override
	public graph copy() 
	{
		graph graph_copy = new DGraph();
		Collection<node_data> nodes = algo.getV();
		if(nodes == null)
			return graph_copy;
		Iterator<node_data> it = nodes.iterator();
		while(it.hasNext())
		{
			graph_copy.addNode(new Node(it.next()));
		}
		Iterator<node_data> itn =  nodes.iterator();
		while(itn.hasNext())
		{
			Collection<edge_data> edges = algo.getE(itn.next().getKey());
			if(edges == null)
				continue;
			Iterator<edge_data> ite =  edges.iterator();
			while (ite.hasNext())
			{
				edge_data e = ite.next();
				graph_copy.connect(e.getSrc(), e.getDest(), e.getWeight());
			}
		}
		return graph_copy;
	}

}
