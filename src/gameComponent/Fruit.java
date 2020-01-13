package gameComponent;


import graph.dataStructure.edge_data;
import graph.utils.Point3D;
import oop_dataStructure.oop_edge_data;
import oop_elements.OOP_Edge;
import oop_utils.OOP_Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;

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
    public Fruit(Point3D location, double value, edge_data edge){
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

    public Fruit(String info){
        try {
            JSONObject fruit = new JSONObject(info);
            location = new Point3D((String) fruit.get("pos"));
            value = fruit.getDouble("value");
            type = fruit.getInt("type");
            edge = null;
            //findEdge();
            if (edge.getSrc() - edge.getDest() > 0) {
                type = 1;
                img = new ImageIcon("C:/Users/ASUS/IdeaProjects/Ex3/src/Utils/icon/banana-64.png"); //Banana
            }
            else{
                type = -1;
                img = new ImageIcon("C:/Users/ASUS/IdeaProjects/Ex3/src/Utils/icon/icons8-apple-64.png"); //Apple
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

    public static void main(String[] a) {
        double v = 10.0D;
        oop_edge_data e = new OOP_Edge(5, 3, 2.0);
        OOP_Point3D p = new OOP_Point3D(1.0, 2.0, 3.0
        );
        Server.Fruit f = new Server.Fruit(v, p, e);
        String s = f.toJSON();
        System.out.println(s);
    }
}
