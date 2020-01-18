package gameComponent;


import graph.dataStructure.edge_data;
import graph.utils.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

/**
 * @authors Lee Fingerhut and Rapheal Gozlan
 */
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
	 * @param type     - The type the fruit is on
	 */
	public Fruit(Point3D location, int type, double value){
		this.location = new Point3D(location);
		this.value = value;
		this.edge = null;
		this.type = type;
		if (type ==  -1)
			img = new ImageIcon("src/Utils/icon/vagina(1).png"); //Banana
		else
			img = new ImageIcon("src/Utils/icon/vagina(2).png"); //Apple

	}

	/**
	 * @param jsonSTR - a String reprsenting a Fruit in
	 *                JSON format and constructor of Fruit
	 */
	public Fruit(String jsonSTR){
		JSONObject obj;
		try {
			obj = new JSONObject(jsonSTR);
			JSONObject fruit = obj.getJSONObject("Fruit");
			this.value = fruit.getDouble("value");
			this.location = new Point3D(fruit.getString("pos"));
			this.type = fruit.getInt("type");
			this.edge = null;
			if (type ==  -1)
				img = new ImageIcon("src/Utils/icon/vagina(1).png"); //Banana
			else
				img = new ImageIcon("src/Utils/icon/vagina(2).png"); //Apple
		}
		catch (JSONException e) {
			e.printStackTrace();
		}


	}

	/**
	 * @param edge - the fruit set its edge to the param
	 * @return - the new edge
	 */
	public edge_data setEdge(edge_data edge){
		this.edge = edge;
		return this.edge;
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
		return "value:" + this.value + ", " + "type:" + type + ", " + ", location:" + this.location +
				", edge:" + this.edge;
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