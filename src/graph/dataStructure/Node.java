package graph.dataStructure;

import graph.utils.Point3D;

import java.io.Serializable;

public class Node implements node_data, Serializable {
    private int key;
    private Point3D location;
    private double weight;
    private int tag;

    public Node(int key, Point3D location, double weight, int tag) {
        this.key = key;
        this.location = new Point3D(location);
        this.weight = weight;
        this.tag = tag;
    }

    public Node(int key, Point3D location, double weight) {
        this.key = key;
        this.location = new Point3D(location);
        this.weight = weight;
    }

    public Node(int key, Point3D location) {
        this.key = key;
        this.location = new Point3D(location);
        weight = 0;
        tag = -1;
    }

    public Node(node_data other) {
        this(other.getKey(), other.getLocation(), other.getWeight(), other.getTag());
    }

    /**
     * @return the key of node
     */
    public int getKey() {
        return key;
    }

    /**
     * @return location of node in from of Point3D
     */
    public Point3D getLocation() {
        return location;
    }

    /**
     * @param p - new new location  (position) of this node.
     */
    public void setLocation(Point3D p) {
        location = new Point3D(p);
    }

    /**
     * @return the weight of node
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param w - the new weight
     */
    public void setWeight(double w) {
        weight = w;
    }

    /**
     * @return the info of the node in form of String
     */
    public String getInfo() {
        return "Key: " + key + ", Location: " + location.toString() +
                ", Weight: " + weight + ", Tag: " + tag;
    }

    /**
     * @return String reprinting the node
     */
    public String toString() {
        return "Key: " + key + "\n" + "Location: " + location.toString() + "\n" + "Weight: " + weight + "\n" + "Tag: " + tag;
    }

    /**
     * @param s - set the node to data in String
     *          String has to be of the form of set Info and toString
     */
    public void setInfo(String s) {
        String[] info = s.split("[,:]");
        key = Integer.parseInt(info[1]);
        location = new Point3D(info[3]);
        weight = Double.parseDouble(info[5]);
        tag = Integer.parseInt(info[7]);
    }

    /**
     * @return tag of node
     */
    public int getTag() {
        return tag;
    }

    /**
     * @param t - the new value of the tag
     */
    public void setTag(int t) {
        tag = t;
    }
}
