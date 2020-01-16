package gameComponent;


import graph.dataStructure.DGraph;
import graph.dataStructure.edge_data;
import graph.dataStructure.graph;
import graph.dataStructure.node_data;
import graph.utils.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class Fruit  {

	private Point3D location;           //The location of the fruit
	private double value;               //The value in game of the fruit
	private edge_data edge;             //The edge the fruit is on
	private int type;                   //The type of the fruit [-1,Banana][1,Apple]
	private ImageIcon img;              //The icon of the fruit

	/**
	 * constructor of Fruit
	 * @param location - The location of the fruit
	 * @param value    - The value in game of the fruit
	 * @param edge     - The edge the fruit is on
	 */
	public Fruit(Point3D location, int type, double value, edge_data edge){
		this.location = new Point3D(location);
		this.value = value;
		this.edge = edge;
		this.type = type;
		if (type ==  -1)
			img = new ImageIcon("src/Utils/icon/banana-32.png"); //Banana
		else
			img = new ImageIcon("src/Utils/icon/icons8-apple-32.png"); //Apple

	}

	/**
	 * @return - The location of the fruit
	 */
	public Point3D getLocation() {
		return location;
	}

	/**
	 * @return - The value in game of the fruit
	 */
	public double getValue() {
		return value;
	}

	/**
	 * type = -1 - is Apple
	 * type = 1 - is Banana
	 * @return - The type of the fruit
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the fruit icon
	 */
	public Image getImg(){
		return img.getImage();
	}

	/**
	 * @return edge fruit is on
	 */
	public edge_data getEdge() {
		return this.edge;
	}

	/**
	 * @return a JSON String representing thr fruit
	 */
	public String toString() {
		return this.toJSON();
	}

	public String toJSON() {
		String ans = "{\"Fruit\":{\"value\":" + this.value + "," + "\"type\":" + type + "," + "\"pos\":\"" + this.location.toString() + "\"" + "}" + "}";
		return ans;
	}

	/**
	 * Finds the edge of the fruit
	 * @param usr_graph - the graph 
	 * @return the edge that the  of fruit
	 */

	public edge_data edge(DGraph usr_graph) 
	{
		// startibng with infinity for comparison
		double min = Double.POSITIVE_INFINITY;
		// starting with no final edge
		edge_data final_egde = null;
		// sinitiating iterator for vertices
		Iterator<node_data> It = usr_graph.getV().iterator();
		// while we still have vertices to check
		while(It.hasNext())
		{
			int key = It.next().getKey();
			try {
				// initiate edges iterator and check all edges
				Iterator<edge_data> Ite = usr_graph.getE(key).iterator();
				while(Ite.hasNext()) 
				{
					edge_data e = Ite.next();
					double usr_dist = usr_graph.getNode(e.getSrc()).getLocation().distance2D(usr_graph.getNode(e.getDest()).getLocation());
					double src_dist = this.getLocation().distance2D(usr_graph.getNode(e.getSrc()).getLocation());
					double dst_dist = this.getLocation().distance2D(usr_graph.getNode(e.getDest()).getLocation());
					double dist = Math.abs((src_dist + dst_dist) - usr_dist);
					if( dist < min)
					{
						final_egde = e;
						min = dist;
					}
				}
			} catch(NullPointerException e) {}
		}
		double final_egde_src = final_egde.getSrc();
		double final_egde_dst = final_egde.getDest();
		if (( (final_egde_src - final_egde_dst > 0) && (this.type == 1) ) || ( (final_egde_src - final_egde_dst) < 0 && (this.type != 1) ))
		{
			return final_egde;
		}
		return usr_graph.getEdge(final_egde.getDest(), final_egde.getSrc());
	}
}