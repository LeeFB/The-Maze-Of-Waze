package gameComponent;


import graph.dataStructure.edge_data;
import graph.utils.Point3D;

import javax.swing.*;
import java.awt.*;

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
		return "value:" + this.value + ", " + "type:" + type + ", " + ", edge:" + this.edge;
	}

	/**
	 * \
	 * @return a JSON String representing thr fruit
	 */
	public String toJSON() {
		String ans = "{\"Fruit\":{\"value\":" + this.value + "," + "\"type\":" + type + "," + "\"pos\":\"" + this.location.toString() + "\"" + "}" + "}";
		return ans;
	}

	public int compare(Fruit anotherFruit){

			return (int) (anotherFruit.getValue() - this.getValue());

	}
}