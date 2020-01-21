package gameClient;

import de.micromata.opengis.kml.v_2_2_0.Point;
import graph.dataStructure.DGraph;
import graph.dataStructure.edge_data;
import graph.dataStructure.node_data;
import graph.utils.Point3D;
import graph.utils.Range;

/**
 * this class supposed to handel
 * the manual driving
 */
public class ManualDrive {
    DGraph graph;

    Range rangeX;
    Range rangeY;

    public ManualDrive(DGraph graph) {
        this.graph = graph;
        setRangeX();
        setRangeY();
    }

    /**
     *TODO choose what robot to move
     * should be at the very end if any time
     */
    /**
     * TODO should not be here
     * but I cant find another way
     * @param src
     * @param lastPressed
     * @return
     */
    protected int nextNodeClicked(int src, Point3D lastPressed) {
        int xFrame = (int) lastPressed.x();
        int yFrame = (int) lastPressed.y();
        Point3D clickedLocation = new Point3D(xFrame,yFrame);
        Point3D destLocation;
        for (edge_data edge : graph.getE(src)){
            node_data node = graph.getNode(edge.getDest());
            destLocation = new Point3D(rescaleX(node.getLocation().x()),rescaleY(node.getLocation().y()));
            if (clickedLocation.distance2D(destLocation) < 30){
                lastPressed = new Point3D(0,0);
                return node.getKey();
            }
        }
        return -1;
    }

    /**
     * @param data denote some data to be scaled
     * @param r_min the minimum of the range of your data
     * @param r_max the maximum of the range of your data
     * @param t_min the minimum of the range of your desired target scaling
     * @param t_max the maximum of the range of your desired target scaling
     * @return relative resolution based of given parameters
     */
    private double rescale(double data, double r_min, double r_max, double t_min, double t_max) {
        return ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
    }
    /**
     * @param x,y - location of data
     * @return resolution of x/y with screen setting
     * using rescale method
     */
    private double rescaleX(double x) {
        return rescale(x,rangeX.get_min(),rangeX.get_max(),MyGameGUI.width*0.1,MyGameGUI.width - MyGameGUI.width*0.1);
    }
    private double rescaleY(double y) {
        return MyGameGUI.height - rescale(y,rangeY.get_min(),rangeY.get_max(),MyGameGUI.height*0.1,MyGameGUI.height - MyGameGUI.height*0.1);
    }

    /**
     * set the RangeX of the graph Range[minX,maxX]
     * go over all nodes and find min,max X
     */
    private void setRangeX(){
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for(node_data node : this.graph.getV()) {
            if (node.getLocation().x() > max)
                max = node.getLocation().x();
            if (node.getLocation().x() < min)
                min = node.getLocation().x();
        }

        rangeX = new Range(min,max);
    }
    /**
     * set the RangeY of the graph Range[minY,maxY]
     * go over all nodes and find min,max Y
     */
    private void setRangeY(){
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for(node_data node : this.graph.getV()) {
            if (node.getLocation().y() > max)
                max = node.getLocation().y();
            if (node.getLocation().y() < min)
                min = node.getLocation().y();
        }

        rangeY = new Range(min,max);
    }

}
