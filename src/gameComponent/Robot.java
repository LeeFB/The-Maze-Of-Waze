package gameComponent;

import graph.utils.Point3D;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class Robot {

	private int src;
	private int dest;
	private Point3D pos;
	private int id;
	private double value;
	private double speed;
	private ImageIcon img;

	public Robot(){
		this.src = 0;
		this.pos = null;
		this.id = -1;
		this.dest = 0;
		this.value = 0;
		this.speed = 0;
		this.img = new ImageIcon("src/Utils/icon/noun_penis_340913.jpg");
	}

	public Robot(int src, Point3D pos, int id, int dest, double value, double speed){
		this.src = src;
		this.pos = new Point3D(pos);
		this.id = id;
		this.dest = dest;
		this.value = value;
		this.speed = speed;
		this.img = new ImageIcon("src/Utils/icon/noun_penis_340913.jpg");
	}

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
		this.img = new ImageIcon("src/Utils/icon/noun_penis_340913.jpg");

	}

	public String toString(){
		return toJSON();
	}

	public String toJSON() {
		return  "{\"Robot\":{\"id\":" + this.id + "," + "\"value\":" + this.value + "," + "\"src\":" + src + "," + "\"dest\":" + dest + "," + "\"speed\":" + this.getSpeed() + "," + "\"pos\":\"" + this.pos.toString() + "\"" + "}" + "}";
	}

	public int getID()
	{
		return id;
	}

	public Point3D getLocation(){
		return this.pos;
	}

	public void setLocation (Point3D loc)
	{
		this.pos = loc;
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	public double getSpeed()
	{
		return speed;
	}


	public int getSrcNode()
	{
		return src;
	}

	public void setSrcNode(int src)
	{
		this.src = src;
	}

	public int getDestNode()
	{
		return dest;
	}

	public void setDestNode(int dest)
	{
		this.dest = dest;
	}

	public Image getImg() {
		return img.getImage();
	}
}
