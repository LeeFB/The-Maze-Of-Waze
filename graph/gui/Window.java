package gui;

import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Window extends JFrame implements ActionListener, Runnable
{
	/**
	 * this is the screen parameters
	 */
	public final int width = 1000;
	public final int height = 750;
	private Range rangeX = new Range(-100,100);
	private Range rangeY = new Range(-80,80);
	private double scaleX = width / rangeX.get_length();
	private double scaleY = height / rangeY.get_length();

	/**the middle point of the screen**/
	private Point3D midPixel = new Point3D((float)width/2,(float) height /2);

	/**the graph in the GUI**/
	private graph graph;
	private Graph_Algo algo = new Graph_Algo();

	/**INIT GUI with graph**/
	public Window(graph g) {
		this.graph = g;
		this.algo.init(this.graph);
		initGUI();
	}

	/**
	 * INIT the screen with all menu's and parameters
	 */
	private void initGUI() {
		this.setSize(width, height);
		this.setTitle("Graph GUI");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MenuBar menuBar = new MenuBar();
		//Graph Utils
		Menu graph_utils = new Menu("Graph Utils");
		menuBar.add(graph_utils);
		this.setMenuBar(menuBar);


		MenuItem init = new MenuItem("Init");
		init.addActionListener(this);

		MenuItem save = new MenuItem("Save");
		save.addActionListener(this);

		graph_utils.add(init);
		graph_utils.add(save);

		//Graph algorithm
		Menu graph_algo = new Menu("Graph Algorithm");
		menuBar.add(graph_algo);
		this.setMenuBar(menuBar);

		MenuItem isConnected = new MenuItem("Is connected?");
		isConnected.addActionListener(this);

		MenuItem shortestPathDist = new MenuItem("Shortest Path");
		shortestPathDist.addActionListener(this);

		MenuItem shortestPathList = new MenuItem("Shortest Path List");
		shortestPathList.addActionListener(this);

		MenuItem tsp = new MenuItem("TSP");
		tsp.addActionListener(this);

		graph_algo.add(isConnected);
		graph_algo.add(shortestPathDist);
		graph_algo.add(shortestPathList);
		graph_algo.add(tsp);

		Thread thread = new Thread(this);
		thread.start();

		//this.addMouseListener(this);

	}

	/**
	 * paint a representation of a graph
	 * @param g - DGraph
	 */
	public void paint(Graphics g) {
		if(graph.nodeSize() == 0)
			return;
		super.paint(g);

		graph graph = this.graph;

		Graphics2D graphics = (Graphics2D)g;

		for (node_data p : graph.getV()) {
			double xPixel = scaleXCenter(p.getLocation().x());
			double yPixel = scaleYCenter(p.getLocation().y());


			graphics.setColor(Color.BLUE);
			graphics.fillOval((int) xPixel - 5 , (int) yPixel - 7, 15, 15);
			graphics.setColor(Color.black);
			g.setFont(new Font("TimesRoman", Font.BOLD, 15));
			graphics.drawString(String.valueOf(p.getKey()), (int) xPixel  - 2, (int) yPixel - 8);
		}

		for (node_data p : graph.getV()) {
			if(graph.getE(p.getKey())!= null)
			{
				for (edge_data edge : graph.getE(p.getKey())) {

					node_data src = graph.getNode(edge.getSrc());
					node_data dest = graph.getNode(edge.getDest());

					double srcXPixel = scaleXCenter(src.getLocation().x());
					double srcYPixel = scaleYCenter(src.getLocation().y());
					double destXPixel = scaleXCenter(dest.getLocation().x());
					double destYPixel = scaleYCenter(dest.getLocation().y());

					graphics.setColor(Color.RED);
					graphics.setStroke(new BasicStroke(2));

					Line2D line = new Line2D.Float(
							(int) srcXPixel ,
							(int) srcYPixel ,
							(int) destXPixel ,
							(int) destYPixel );

					graphics.setStroke(new BasicStroke((float) 2.5));
					graphics.draw(line);


					graphics.setColor(Color.yellow);
					graphics.fillOval((int) ((srcXPixel * 10 + destXPixel * 2) / 12) - 5 , (int) ((srcYPixel * 10 + destYPixel * 2) / 12) - 6, 10, 10);

					graphics.setColor(Color.DARK_GRAY);
					graphics.drawString(String.valueOf(edge.getWeight()), (int) ((srcXPixel * 2 + destXPixel) / 3) - 5 , (int) ((srcYPixel * 2 + destYPixel ) / 3) - 6);
				}
			}
		}
	}

	/**
	 * this method contain all possible menus item and switching to the correct method
	 * @param e - menus action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();

		switch (str) {
			case "Init":        //NEED HELP
				initCommand();
				break;
			case "Save":        //NEED HELP
				saveCommand();
				break;
			case "Is connected?":
				isConnectedCommand();
				break;
			case "Shortest Path":
				shortestPathCommand();
				break;
			case "Shortest Path List":
				shortestPathListCommand();
				break;
			case "TSP":
				tspCommand();
				break;
		}
	}

	/**
	 * ask for a how many node we want to travel and their ID and return the shortest path to the screen
	 */
	private void tspCommand() {
		if (graph == null)
			throw new RuntimeException("ERR: You need to init the graph ! ");

		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);

		Container container = frame.getContentPane();
		container.setLayout(new FlowLayout());

		LinkedList<Integer> target = new LinkedList<>();
		int numberOfTargets = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the sum of nodes you want to visit in total"));
		if (numberOfTargets <= 0)
		{
			JOptionPane.showMessageDialog(container, "The target list is empty! ");
		}
		else {
			for (int i = 0; i < numberOfTargets; i++)
				target.add(Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the id of the node you want to visit")));

			try
			{
				List<node_data> tsp_list = algo.TSP(target);
				StringBuilder s = new StringBuilder("The path is:\n");
				int i = 0;
				for (node_data node : tsp_list)
				{
					s.append(node.getKey());
					if (i < tsp_list.size() - 1)
						s.append(" -> ");
					i++;
				}
				//System.out.println(s);
				JOptionPane.showMessageDialog(container, s);
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(container, "ERR: No valid path between all targets !");
			}
		}
	}

	/**
	 * ask for 2 nose and return a list of all node in shortest path
	 */
	private void shortestPathListCommand()	{
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);

		Container container = frame.getContentPane();
		container.setLayout(new FlowLayout());

		JLabel labelSrc = new JLabel("Enter source node");
		labelSrc.setPreferredSize(new Dimension(150,25));
		JTextField src = new JTextField();
		src.setPreferredSize(new Dimension(150,25));

		JLabel labelDest = new JLabel("Enter destination node");
		labelDest.setPreferredSize(new Dimension(150,25));
		JTextField dest = new JTextField();
		dest.setPreferredSize(new Dimension(150,25));

		JButton go = new JButton("Go");
		JLabel label = new JLabel("Answer will appear here");
		label.setSize(100,300);

		go.addActionListener(e -> {
			String srcText = src.getText();
			String destText = dest.getText();
			try
			{
				List<node_data> sp_list = algo.shortestPath(Integer.parseInt(srcText),Integer.parseInt(destText));
				StringBuilder s = new StringBuilder("The shortest path from " + srcText + " -> " + destText + " is:\n");
				int i = 0;
				for (node_data node : sp_list)
				{
					s.append(node.getKey());
					if (i < sp_list.size() - 1)
						s.append(" -> ");
					i++;
				}
				//System.out.println(s);
				JOptionPane.showMessageDialog(container, s);
			}
			catch (Exception e2)
			{
				//System.out.println("no path from " + srcText + " -> " + destText);
				label.setText("no path from " + srcText + " -> " + destText);
			}
		});

		container.add(labelSrc);
		container.add(src);
		container.add(labelDest);
		container.add(dest);
		container.add(go);
		container.add(label);

		frame.setVisible(true);
	}

	/**
	 * ask for 2 nose and return the length of shortest path
	 */
	private void shortestPathCommand()	{
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = frame.getContentPane();
		container.setLayout(new FlowLayout());

		JLabel labelSrc = new JLabel("Enter source node");
		labelSrc.setPreferredSize(new Dimension(150,25));
		JTextField src = new JTextField();
		src.setPreferredSize(new Dimension(150,25));

		JLabel labelDest = new JLabel("Enter destination node");
		labelDest.setPreferredSize(new Dimension(150,25));
		JTextField dest = new JTextField();
		dest.setPreferredSize(new Dimension(150,25));

		JButton go = new JButton("Go");
		JLabel label = new JLabel("Input will appear here");

		go.addActionListener(e -> {
			String srcText = src.getText();
			String destText = dest.getText();
			try {
				double ans = algo.shortestPathDist(Integer.parseInt(srcText),Integer.parseInt(destText));
				//System.out.println("The shortest path from " + srcText + " -> " + destText + " is: " + ans);

				label.setText("The shortest path from " + srcText + " -> " + destText + " is: " + ans);
			} catch (Exception e2) {
				//System.out.println("no path from " + srcText + " -> " + destText);
				label.setText("no path from " + srcText + " -> " + destText);
			}
		});
		container.add(labelSrc);
		container.add(src);
		container.add(labelDest);
		container.add(dest);
		container.add(go);
		container.add(label);
		frame.setVisible(true);
	}

	/**
	 * gui for isConnected?
	 */
	private void isConnectedCommand() {
		if (graph == null)
			throw new RuntimeException("ERR: You need to init the graph");

		if (algo.isConnected())
			JOptionPane.showMessageDialog(null,"The graph is connected");
		else
			JOptionPane.showMessageDialog(null,"The graph is NOT connected");
	}

	/**
	 * gui for save file
	 * need to ask what is expected
	 */
	private void saveCommand() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");
		int userSelection = fileChooser.showSaveDialog(this);

		if (userSelection == JFileChooser.APPROVE_OPTION)
		{
			File fileToSave = fileChooser.getSelectedFile();
			try
			{
				algo.save(fileToSave.getAbsolutePath());
				System.out.println("Save as file: " + fileToSave.getAbsolutePath());
			}
			catch (Exception e) {
				System.out.println("there was a problem in save routine!");
			}

		}
	}

	/**
	 * gui to init file
	 **/
	private void initCommand() {
		Graph_Algo algo_init = new Graph_Algo();
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setSelectedFile(new File(""));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		// chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.OPEN_DIALOG) {
			File file = chooser.getSelectedFile();
			algo_init.init(file.getAbsolutePath());
			this.graph = algo_init.copy();
			try {
				this.algo.init(this.graph);
				repaint();
			} catch (Exception e) {
				System.out.println("there was a problem in init routine!");
			}
		}
		else
		{
			throw new RuntimeException("ERR: Not supported file format");
		}
	}

	/**
	 * @param x int (x,y) coordination
	 * @return corresponding pixel to (x,y)
	 */
	private double scaleXCenter(double x){
		if (!rangeX.isIn(x)){
			System.out.println("ERR: Node is out of X scale");
			System.out.println("Rescaling and repainting");
			reScale();
		}
		return (midPixel.x() + (scaleX * x));
	}
	private double scaleYCenter(double y){
		if (!rangeX.isIn(y)) {
			System.out.println("ERR: Node is out of Y scale");
			System.out.println("Rescaling and repainting");
			reScale();
		}

		return (midPixel.y() + (scaleY * y));
	}

	/**This function rescale the range of the screen**/
	private void reScale(){
		rangeX = new Range(rangeX.get_min() * 2,rangeX.get_max() * 2);
		rangeY = new Range(rangeY.get_min() * 2 ,rangeY.get_max() * 2 );

		scaleX = width / rangeX.get_length();
		scaleY = height / rangeY.get_length();

		// the middle point of the screen
		midPixel = new Point3D((float)width/2,(float) height /2);

		System.out.println("new Range is X:["+rangeX.get_min()+","+rangeX.get_max()+"]\n"+
				"			 Y:["+rangeY.get_min()+","+rangeY.get_max()+"]");

		initGUI();


	}

	@Override
	public void run() {
		int indexMC = graph.getMC();

		while (true)
			synchronized (this) {
				if (graph.getMC() != indexMC) {
					repaint();
					indexMC = graph.getMC();
				}
			}
	}

}
