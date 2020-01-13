package gameComponent;

import org.json.JSONObject;

import graph.utils.Point3D;

public class Robot implements robotINT 
{
	private int src;
	private Point3D pos;
	private int id;
	private int dest;
	private int value;
	private double speed;
	private double money;

	public Robot()
	{
		this.src = 0;
		this.pos = null;
		this.id = -1;
		this.dest = 0;
		this.value = 0;
		this.speed = 0;
		this.money = 0;
	}

	public Robot(int src, Point3D pos, int id, int dest, int value, double speed, double money)
	{
		this.src = src;
		this.pos = new Point3D(pos);
		this.id = id;
		this.dest = dest;
		this.value = value;
		this.speed = speed;
		this.money = money;
	}

	@Override
	public int getID() 
	{
		return id;
	}

	@Override
	public boolean setNextNode(int var1)
	{
		return false;
	}

	@Override
	public int getNextNode() 
	{
		return 0;
	}

	@Override
	public Point3D getLocation() 
	{
		Point3D location = new Point3D(pos);
		return location;
	}

	@Override
	public boolean isMoving() 
	{
		return false;
	}

	@Override
	public boolean move()
	{
		return false;
	}

	@Override
	public double getMoney() 
	{
		return money;
	}

	@Override
	public double getBatLevel() 
	{
		return 0;
	}

	@Override
	public void randomWalk() 
	{

	}

	@Override
	public double getSpeed()
	{
		return speed;
	}

	@Override
	public void setSpeed(double var1)
	{
		this.speed = var1;
	}

	@Override
	public double doubleSpeedWeight() {
		return 0;
	}

	@Override
	public double turboSpeedWeight() {
		return 0;
	}

	@Override
	public void setDoubleSpeedWeight(double var1)
	{

	}

	@Override
	public void setTurboSpeedWeight(double var1)
	{

	}

	@Override
	public void addMoney(double var1) 
	{

	}

	@Override
	public int getSrcNode() 
	{
		return src;
	}

	public void init(String jsonSTR) 
	{
		if(!jsonSTR.isEmpty())
		{
			Robot robotFromJson = new Robot();
			try{
				JSONObject obj      = new JSONObject(jsonSTR);
				JSONObject robot    = obj.getJSONObject("Robot");
				robotFromJson.id    = robot.getInt("id");
				robotFromJson.src   = robot.getInt("src");
				robotFromJson.dest  = robot.getInt("dest");
				String pos          = robot.getString("pos");
				robotFromJson.pos   = new Point3D(pos);
				robotFromJson.value = robot.getInt("value");
				robotFromJson.speed = robot.getDouble("speed");

			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public void toJSON(String toJSON)
	{
		try 
		{
			JSONObject obj   = new JSONObject(toJSON);
			JSONObject robot = obj.getJSONObject("Robot");
			this.src         = robot.getInt("src");
			String pos       = robot.getString("pos");
			this.pos         = new Point3D(pos);
			this.id          = robot.getInt("id");
			this.dest        = robot.getInt("dest");
			this.value       = robot.getInt("value");
			this.speed       = robot.getDouble("speed");

		} catch (Exception e) 

		{
			e.printStackTrace();
		}
	}
}