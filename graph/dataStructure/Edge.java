package dataStructure;

import java.io.Serializable;

public class Edge implements edge_data, Serializable {
    private int src;
    private int dest;
    private double weight;
    private int tag;

    public Edge(int src, int dest, double weight){
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.tag = -1;
    }

    public Edge(Edge edge){
        this.src = edge.src;
        this.dest = edge.dest;
        this.weight = edge.weight;
        this.tag = edge.tag;
    }


    /**
     *
     * @return src ID
     */
    public int getSrc() {
        return src;
    }

    /**
     *
     * @int getDest()
     */
    public int getDest() {
        return dest;
    }

    /**
     *
     * @return weight of the edge
     */
    public double getWeight() {
        return weight;
    }

    /**
     *
     * @return the remark (meta data) associated with this edge
     */
    public String getInfo() {
        return "Src: " + src +", Dest: " + dest +
                ", Weight: " + weight + ", Tag: " + tag;
    }

    /**
     *
     * @return string of the edge
     */
    public String toString() {
        return "Src: " + src + "\n" + "Dest: " + dest + "\n" + "Weight: " + weight + "\n" + "Tag: " + tag;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s - String of an Edge
     */
    public void setInfo(String s) {
        String[] info = s.split(",||:");
        this.src = Integer.parseInt(info[1]);
        this.dest = Integer.parseInt(info[3]);
        this.weight = Double.parseDouble(info[5]);
        this.tag = Integer.parseInt(info[7]);
    }

    /**
     *
     * @return tag of edge
     */
    public int getTag() {
        return tag;
    }

    /**``
     * @v
     */
    public void setTag(int t) {
        this.tag = t;
    }
}
