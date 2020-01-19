package gameComponent;

import graph.utils.Point3D;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

/**
 * @authors Lee Fingerhut and Rapheal Gozlan
 */
public class Robot {

	private int src;
	private int dest;
	private Point3D pos;
	private int id;
	private double value;
	private double speed;
	private ImageIcon img;

	/**
	 * constructors
	 */
	public Robot(){
		this.src = 0;
		this.pos = null;
		this.id = -1;
		this.dest = 0;
		this.value = 0;
		this.speed = 0;
		this.img = new ImageIcon("src/Utils/icon/icon.png");
	}

	public Robot(int src, Point3D pos, int id, int dest, double value, double speed){
		this.src = src;
		this.pos = new Point3D(pos);
		this.id = id;
		this.dest = dest;
		this.value = value;
		this.speed = speed;
		this.img = new ImageIcon("src/Utils/icon/icon.png");
	}

	/**
	 * The function create object of Robot From at string of json
	 * @param jsonSTR - string type if json
	 */
	public Robot(String jsonSTR){
		try{
			JSONObject obj      = new JSONObject(jsonSTR);
			JSONObject robot    = obj.getJSONObject("Robot");
			this.id    = robot.getInt("id");
			this.src   = robot.getInt("src");
			this.dest  = robot.getInt("dest");
			this.pos   = new Point3D(robot.getString("pos"));
			this.pos   = new Point3D(pos);
			this.value = robot.getInt("value");
			this.speed = robot.getDouble("speed");
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		this.img = new ImageIcon("src/Utils/icon/icon.png");
	}

	/**
	 * @return a JSON String representing the Robot
	 */
	public String toString(){
		return toJSON();
	}

	/**
	 * @return a JSON String representing the Robot
	 */
	public String toJSON() {
		return  "{\"Robot\":{\"id\":" + this.id + "," + "\"value\":" + this.value + "," + "\"src\":" + src + "," + "\"dest\":" + dest + "," + "\"speed\":" + this.getSpeed() + "," + "\"pos\":\"" + this.pos.toString() + "\"" + "}" + "}";
	}

	/**
	 * 
	 * @return - the ID of the Robot
	 */
	public int getID()
	{
		return id;
	}

	/**
	 * 
	 * @return - the Point3D location of the Robot
	 */
	public Point3D getLocation(){
		return this.pos;
	}

	/**
	 * 
	 * The function set the location of the Robot
	 */
	public void setLocation (Point3D loc)
	{
		this.pos = loc;
	}

	/**
	 * 
	 * @return - the value of the Robot
	 */
	public double getValue()
	{
		return value;
	}


	/**
	 * 
	 * The function set the value of the Robot
	 */
	public void setValue(double value)
	{
		this.value = value;
	}

	/**
	 * 
	 * @return - the speed of the Robot
	 */
	public double getSpeed()
	{
		return speed;
	}

	/**
	 * 
	 * @return - the src node of the edge the Robot on
	 */
	public int getSrcNode()
	{
		return src;
	}

	/**
	 * The function set the src of the Robot
	 */
	public void setSrcNode(int src)
	{
		this.src = src;
	}

	/**
	 * 
	 * @return - the dest node of the edge the robot on
	 */
	public int getDestNode()
	{
		return dest;
	}

	/**
	 * 
	 * The function set the dest of the Robot
	 */
	public void setDestNode(int dest)
	{
		this.dest = dest;
	}

	/**
	 * 
	 * @return - the icom image of the Robot
	 */
	public Image getImg() {
		return img.getImage();
	}
}