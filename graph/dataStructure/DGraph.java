package dataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DGraph implements graph, Serializable{
	private HashMap<Integer, node_data> nodes;					//HashMap representing the nodeId and the node
	private HashMap<Integer,HashMap<Integer,edge_data>> edge;	//HashMap representing the nodeID of src which point to another HAshMap of nodeID of dest and Edge
	private int edgeSize;
	private int MC;
	/**
	 * empty constructor
	 */
	public DGraph()
	{
		nodes    = new HashMap<Integer, node_data>();
		edge     = new HashMap<Integer,HashMap<Integer,edge_data>>();
		edgeSize = 0;
		MC       = 0;
	}

	/**
	 * construct a graph with a collection of nodes
	 */
	public DGraph(Collection<node_data> nodes)
	{
		this();
		if (nodes.size() == 0)
			return;

		Iterator<node_data> it = nodes.iterator();
		while(it.hasNext())
		{
			this.addNode(it.next());
		}
	}

	/**
	 *
	 * @param key - the node_id
	 * @return the node associated with the key
	 */
	public node_data getNode(int key)
	{
		if (nodes.containsKey(key))
			return nodes.get(key);
		return null;
	}

	/**
	 *
	 * @param src - nodeID of src
	 * @param dest - nodeID of desr
	 * @return Edge (src,dest)
	 */
	public edge_data getEdge(int src, int dest)
	{
		if (nodes.containsKey(src) && edge.containsKey(src) && edge.get(src).containsKey(dest))
			return edge.get(src).get(dest);

		return null;
	}

	/**
	 *
	 * @param n - nodeID
	 * add the node to the graph
	 */
	public void addNode(node_data n)
	{
		if (nodes.containsKey(n.getKey()))
			throw new RuntimeException("ERR: node allready exists");

		nodes.put(n.getKey(), n);
		MC++;
	}

	/**
	 *
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 * creat an edge and add to graph
	 */
	public void connect(int src, int dest, double w)
	{
		if (!nodes.containsKey(src) || !nodes.containsKey(dest))
			throw new RuntimeException("ERR: One or both nose srcID/destID exist");

		if (!edge.containsKey(src))
			edge.put(src, new HashMap<Integer,edge_data>());

		edge.get(src).put(dest, new Edge(src, dest, w));
		edgeSize++;
		MC++;
	}

	/**
	 * return a collation over nodes in graph
	 */
	public Collection<node_data> getV()
	{
		if (nodeSize() == 0)
			return null;
		return nodes.values();
	}

	/**
	 * @param node_id
	 * @return  a collation over edges in graph
	 */
	public Collection<edge_data> getE(int node_id)
	{
		if (!edge.containsKey(node_id))
			return null;

		return edge.get(node_id).values();
	}

	/**
	 * @param key node_id
	 * @return deleted node_data
	 */
	@Override
	public node_data removeNode(int key)
	{
		if (!nodes.containsKey(key))
			throw new RuntimeException("ERR: Node doesn't exist");

		for (Integer a: edge.keySet()) {
			if (edge.get(a).containsKey(key)){
				edge.get(a).remove(key);
				edgeSize--;
			}
		}
		node_data deletedNode = nodes.get(key);
		if (edge.containsKey(key)){
			edgeSize -= edge.get(key).size();
			edge.remove(key);
		}
		nodes.remove(key);
		MC++;
		return deletedNode;
	}

	/**
	 *
	 * @param src - node_id
	 * @param dest - node_id
	 * @return the deleted edge from graph
	 */
	public edge_data removeEdge(int src, int dest)
	{
		if (!edge.containsKey(src) || !edge.get(src).containsKey(dest))
			throw new RuntimeException("ERR: edge doesn't exist");

		edge_data returnedEdge = new Edge((Edge) edge.get(src).get(dest));
		edge.get(src).remove(dest);

		edgeSize--;
		MC++;
		return returnedEdge;
	}

	/**
	 *
	 * @return size of node in graph
	 */
	public int nodeSize()
	{
		return this.nodes.size();
	}

	/**
	 *
	 * @return size of edged in graph
	 */
	public int edgeSize()
	{
		return edgeSize;
	}

	/**
	 * @return MC - number of changes in class
	 */
	public int getMC()
	{
		return MC;
	}

	/**
	 * @return String representing the graph
	 */
	public String toString()
	{
		String s = new String();
		s += "Nodes: {\n";
		for (Integer key: nodes.keySet()) 
		{	
			s += "[\n";
			s += nodes.get(key).toString() + "\n";
			s += "]\n";
		}
		s += "}\n";

		s += "Edges: {\n";
		for (Integer key: edge.keySet()) 
		{	
			for (Integer keyinternalkey: edge.get(key).keySet()) 
			{
				s += "[\n";
				s += edge.get(key).get(keyinternalkey).toString() + "\n";
				s += "]\n";
			}
		}
		s += "}\n";
		return s;
	}
}
