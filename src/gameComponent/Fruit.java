package gameComponent;


import graph.dataStructure.edge_data;
import graph.utils.Point3D;
import oop_dataStructure.oop_edge_data;
import oop_elements.OOP_Edge;
import oop_utils.OOP_Point3D;

import javax.swing.*;
import java.awt.*;

public class Fruit implements fruitsINT {

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
        if (type == 1)
            img = new ImageIcon("src/Utils/icon/vagina(1).png"); //Banana
        else
            img = new ImageIcon("src/Utils/icon/vagina(2).png"); //Apple

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

    public String toString() {
        return this.toJSON();
    }

    public String toJSON() {
        String ans = "{\"Fruit\":{\"value\":" + this.value + "," + "\"type\":" + type + "," + "\"pos\":\"" + this.location.toString() + "\"" + "}" + "}";
        return ans;
    }

	public edge_data getEdge()
	{
		return this.edge;
	}

}
