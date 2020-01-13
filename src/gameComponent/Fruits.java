package gameComponent;


import graph.dataStructure.edge_data;
import graph.utils.Point3D;

import javax.swing.*;

public class Fruits implements fruitsINT {

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
    public Fruits(Point3D location, double value, edge_data edge){
        this.location = new Point3D(location);
        this.value = value;
        this.edge = edge;
        if (edge.getSrc() - edge.getDest() > 0) {
            type = 1;
            img = new ImageIcon("C:/Users/ASUS/IdeaProjects/Ex3/src/Utils/icon/banana-64.png"); //Banana
        }
        else{
            type = -1;
            img = new ImageIcon("C:/Users/ASUS/IdeaProjects/Ex3/src/Utils/icon/icons8-apple-64.png"); //Apple
        }
    }

    /**
     * @return - The location of the fruit
     */
    @Override
    public Point3D getLocation() {
        return location;
    }

    /**
     * @return - The value in game of the fruit
     */
    @Override
    public double getValue() {
        return value;
    }

    /**
     * type = -1 - is Apple
     * type = 1 - is Banana
     * @return - The type of the fruit
     */
    @Override
    public int getType() {
        return type;
    }

    /**
     *
     * @return the fruit icon
     */
    public ImageIcon getImg(){
        return img;
    }

    public String toString() {
        return this.toJSON();
    }

    public String toJSON() {
        String ans = "{\"Fruit\":{\"value\":" + this.value + "," + "\"type\":" + type + "," + "\"pos\":\"" + this.location.toString() + "\"" + "}" + "}";
        return ans;
    }

    /**
     *
     * @param r - Robot
     * @param dist - desire length between fruit and robot
     *               in order to grab the fruit
     * @return - the value of the fruit else 0;
     */
    @Override
    public double grap(Robot r, double dist) {
        if (r != null && r.getNextNode() == edge.getDest())
            if(dist > r.getLocation().distance3D(location))
                return value;
        return 0;
    }
}
