package gameClient;


import Server.Game_Server;
import Server.game_service;
import gameComponent.Fruit;
import gameComponent.Robot;
import graph.dataStructure.DGraph;
import graph.dataStructure.edge_data;
import graph.dataStructure.node_data;
import graph.utils.Point3D;
import graph.utils.Range;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @authors Lee Fingerhut and Rapheal Gozlan
 */
public class MyGameGUI extends JFrame implements ActionListener, MouseListener, Runnable {
	/**
	 * this is the screen parameters
	 */
	private static final int width = 1239;
	private static final int height = 595;
	/**
	 * range of node in game
	 */
	private Range rangeX;
	private Range rangeY;
	/**
	 * the graph in the game
	 **/
	private DGraph graph;
	private GameLogic gameLogic;
	private game_service game;
	private int level;                  //The level we are playing
	private boolean manuel;
	//private double score = 0;
	private BufferedImage gameLayout;   //Buffer for the graph
	/**
	 * LinkList for the robots and fruits in the game
	 */
	private LinkedList<Fruit> fruits = new LinkedList<>();
	private LinkedList<Robot> robots = new LinkedList<>();
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	private Point3D lastPressed = new Point3D(0,0);
	public static KML_Logger kml=null;

	/**
	 * INIT game
	 **/
	public MyGameGUI() {
		initGUI();
	}

	/**
	 * INIT the screen with all menu's and parameters
	 */
	private void initGUI() {
		//set the window parameters
		this.setSize(width, height);
		this.setTitle("My Game");
		this.setLocationRelativeTo(null);
		this.setFocusable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//ask for the level of the game
		level = askForLevel();
		kml = new KML_Logger(level);

		manuel = askForPlayType();
		game = Game_Server.getServer(level); // you have [0,23] game
		//init the game graph
		String graphStr = game.getGraph();
		this.graph = new DGraph();
		this.graph.init(graphStr);
		//kml.addNodes(graph);

		this.gameLogic = new GameLogic(graph, graphStr);
		//set the points range of the graph
		setRangeX();
		setRangeY();
		//get the list of fruits and add them to the game
		getFruits();
		//addRobots
		if (manuel)
			addRobots();
		else
			addRobotsAuto();
		getRobots();
		//kml.addRobot(robots);
		game.startGame();
		Thread gamePlay = new Thread(this);
		gamePlay.start();
		this.addMouseListener(this);
	}

	@Override
	public void run() {
		while (game.isRunning() ) {

			long dt =120;
			try{
				getRobots();
				getFruits();
				moveRobots();
				repaint();
				Thread.sleep(dt);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		game.stopGame();
		String results = game.toString();
		System.out.println(results);
		kml.kmlEndAndSave();

		endGame();
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private void endGame() {
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			JOptionPane.showMessageDialog(
					frame,
					"Your grade is" + getGrade() + "\n",
					"Game Over",
					JOptionPane.INFORMATION_MESSAGE
			);

	}

	/**
	 * Moves each of the robots along the edge,
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 */
	private void moveRobots() {
		List<String> log = game.move();
		if (log != null) {
			long t = game.timeToEnd();
			for (String robot_json : log) {
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					int value = ttt.getInt("value");
					if (dest == -1) {
						do {
							//getFruits();
							if (manuel)
								dest = nextNodeClicked(src);
							else
								dest = gameLogic.NextNode(fruits,src);

							System.out.println("Robot_ID:" + rid +" was order to go from " + src + " to " + dest);
						} while (dest == -1);
						game.chooseNextEdge(rid, dest);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//need to improve more then 1 robot control
	private int nextNodeClicked(int src) {
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

	//return the time in a String format
	private String longToStr(long time){
		return Long.toString(time / 1000);
	}

	public void paint(Graphics g){
		if (gameLayout == null){
			paintComponent(g);
		}
		g.drawImage(gameLayout,0,0,this);

		//Show time to End and Grade
		Font font = new Font("Bold", Font.BOLD,20);
		g.setFont(font);
		g.drawString("Time to End: " + longToStr(game.timeToEnd()),width - width/6, height / 9);
		g.drawString("Grade: " + getGrade(),width - width/6  , height / 9 + 25);

		for (Fruit f : fruits)
			g.drawImage(f.getImg(),(int)rescaleX(f.getLocation().x()) - 8 ,(int)(rescaleY(f.getLocation().y())) - 8 ,this);
		for (Robot r : robots)
			g.drawImage(r.getImg(),(int)rescaleX(r.getLocation().x()) - 8 ,(int)(rescaleY(r.getLocation().y())) - 8  ,this);

	}

	/**
	 * paint a representation of a graph
	 *
	 * //@param g - DGraph
	 */
	public void paintComponent(Graphics g) {
		if (graph.nodeSize() == 0)
			return;
		gameLayout = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		DGraph graph = this.graph;
		Graphics2D graphics = gameLayout.createGraphics();
		graphics.drawImage(new ImageIcon(getMap()).getImage(),0,0,this);
		for (node_data p : graph.getV()) {
			double xPixel = rescaleX(p.getLocation().x());
			double yPixel = rescaleY(p.getLocation().y());
			graphics.setColor(Color.BLUE);
			graphics.fillOval((int) xPixel, (int) yPixel, 18, 18);
			graphics.setColor(Color.black);
			graphics.drawRect((int) xPixel, (int) yPixel, 18, 18);
			graphics.setFont(new Font("TimesRoman", Font.BOLD, 15));
			graphics.drawString(String.valueOf(p.getKey()), (int) xPixel - 2, (int) yPixel - 8);
		}
		for (node_data p : graph.getV()) {
			if (graph.getE(p.getKey()) != null) {
				for (edge_data edge : graph.getE(p.getKey())) {
					node_data src = graph.getNode(edge.getSrc());
					node_data dest = graph.getNode(edge.getDest());
					double srcXPixel = rescaleX(src.getLocation().x());
					double srcYPixel = rescaleY(src.getLocation().y());
					double destXPixel = rescaleX(dest.getLocation().x());
					double destYPixel = rescaleY(dest.getLocation().y());
					graphics.setColor(Color.RED);
					graphics.setStroke(new BasicStroke(2));
					Line2D line = new Line2D.Float(
							(int) srcXPixel + 8,
							(int) srcYPixel + 8,
							(int) destXPixel + 8,
							(int) destYPixel + 8);
					graphics.setStroke(new BasicStroke((float) 2.5));
					graphics.draw(line);
					graphics.setColor(Color.yellow);
					graphics.fillOval((int) ((srcXPixel * 10 + destXPixel * 2) / 12) + 3, (int) ((srcYPixel * 10 + destYPixel * 2) / 12) + 3 , 10, 10);
					graphics.setColor(Color.DARK_GRAY);
					graphics.drawString(String.valueOf(df2.format(edge.getWeight())), (int) ((srcXPixel * 2 + destXPixel) / 3) , (int) ((srcYPixel * 2 + destYPixel) / 3));
				}
			}
		}
		Graphics2D layoutCan = (Graphics2D)g;
		layoutCan.drawImage(gameLayout,null,0,0);


	}

	/**
	 * ask from player what level he wants to play
	 * @return int number of chosen level to play
	 */
	private int askForLevel() {
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon("src/Utils/icon/penis.png");
		Image image = icon.getImage(); // transform it
		Image newimg = image.getScaledInstance(60, 60,  Image.SCALE_SMOOTH); // scale it the smooth way
		icon = new ImageIcon(newimg);  // transform it back
		Object[] possibilities = {
				"0","1","2","3","4","5","6",
				"7","8","9","10","11","12",
				"13","14","15","16","17","18",
				"19","20","21","22","23"};
		String s;
		do {
			s = (String)JOptionPane.showInputDialog(
					frame,
					"Choose level to play:\n",
					"Level",
					JOptionPane.PLAIN_MESSAGE,
					icon,
					possibilities,
					"0");
			if (s == null)
				JOptionPane.showMessageDialog(this, "Please Choose a Level Number",
						"ERROR", JOptionPane.ERROR_MESSAGE);
		}while (s == null);
		return Integer.parseInt(s);
	}

	/**
	 * ask from player if he wants to play manually or automatic
	 * @return true if chosen to play manually
	 */
	private boolean askForPlayType() {
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon("src/Utils/icon/icon.png");
		Image image = icon.getImage(); // transform it
		Image newimg = image.getScaledInstance(60, 60,  Image.SCALE_SMOOTH); // scale it the smooth way
		icon = new ImageIcon(newimg);  // transform it back
		Object[] possibilities = {"Manually","Automatic"};
		String s;
		do {
			s = (String)JOptionPane.showInputDialog(
					frame,
					"Choose method to play level:" + level +"\n",
					"Level" ,
					JOptionPane.PLAIN_MESSAGE,
					icon,
					possibilities,
					"0");
			if (s == null)
				JOptionPane.showMessageDialog(this, "Please Choose a method to play",
						"ERROR", JOptionPane.ERROR_MESSAGE);
		}while (s == null);
		return s.equals("Manually");
	}

	/**
	 * ask for number of Robots in game and added them to the server manually
	 */
	private void addRobots() {
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			for (int a = 0; a < rs; a++) {
				game.addRobot(src_node + a);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ask for number of Robots in game and added them to the server using best positioning
	 */
	private void addRobotsAuto() {
		String info = game.toString();

		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			LinkedList<Integer> list = gameLogic.getStartingPoint(fruits);
			for (int i = 0; i < rs; i++){
				if (i <= list.size()){
					game.addRobot(list.get(i));
				}
				else{
					Random rand = new Random();
					game.addRobot(rand.nextInt(graph.nodeSize()));
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get info from server in JSON format and construct
	 * a list of Fruits and Robots in game
	 */
	private void getFruits() {
		fruits.clear();
		int i = 0;
		for (String s : game.getFruits()) {
			fruits.add(new Fruit(s));
			if (fruits.get(i).getType() == 1) {
				kml.PlaceMark("apple", fruits.get(i).getLocation());
			} else
				kml.PlaceMark("banana", fruits.get(i).getLocation());

			i++;
		}

	}
	private void getRobots(){
		robots.clear();
		int i = 0;
		for (String s : game.getRobots()) {
			robots.add(new Robot(s));
			kml.PlaceMark("robot", robots.get(i).getLocation());
			i++;
		}
	}

	/**
	 * @return path to map Image in utils
	 */
	private String getMap() {
		String info = game.toString();

		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject obj = line.getJSONObject("GameServer");
			String map = obj.getString("graph");
			map = "src/Utils/" + map + ".png";
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return - return the current grade in server
	 * in the form of String
	 */
	private String getGrade() {
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject obj = line.getJSONObject("GameServer");
			String grade = String.valueOf(obj.getDouble("grade"));
			return grade;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
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
		return rescale(x,rangeX.get_min(),rangeX.get_max(),width*0.1,width - width*0.1);
	}
	private double rescaleY(double y) {
		return height - rescale(y,rangeY.get_min(),rangeY.get_max(),height*0.1,height - height*0.1);
	}

	/**
	 * set the RangeX of the graph Range[minX,maxX]
	 * go over all nodes and find min,max X
	 */
	private void setRangeX(){
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		for(node_data node : graph.getV()) {
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
		for(node_data node : graph.getV()) {
			if (node.getLocation().y() > max)
				max = node.getLocation().y();
			if (node.getLocation().y() < min)
				min = node.getLocation().y();
		}

		rangeY = new Range(min,max);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		lastPressed = new Point3D(e.getX(),e.getY());

	}

	@Override
	public void mousePressed(MouseEvent e){
		lastPressed = new Point3D(e.getX(),e.getY());

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
