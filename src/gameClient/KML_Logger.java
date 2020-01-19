package gameClient;

import gameComponent.Fruit;
import gameComponent.Robot;
import graph.dataStructure.graph;
import graph.dataStructure.node_data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KML_Logger {

	private int level;
	private StringBuilder w;

	public KML_Logger()
	{

	}

	/**
	 *Constructor to create file
	 * @param level
	 */
	public KML_Logger(int level) {

		this.level = level;
		w = new StringBuilder();
		w.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
						"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
						"  <Document>\r\n" +
						"    <name>	Level " + this.level + "</name>" +
						" <Style id=\"node\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/paddle/grn-blank.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"banana\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"apple\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"robot\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/pal4/icon57.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>\r\n"
				);
	}

	/**
	 * 
	 * @param g
	 */
	public void addNodes(graph g)
	{
		for (node_data data : g.getV() ) {
			w.append(	"<Placemark>\n"+
					"<description>"+"Node num"+data.getKey()+"</description>\n"+
					"<Point>\n"+
					"<coordinates>"+data.getLocation().x() +","+data.getLocation().y()+","+data.getLocation().z()+"</coordinates>\n"+
					"</Point>\n"+
					"</Placemark>\n"
					);
		}

	}
	/**
	 * add the fruits of the game to file
	 * @param fruit
	 */
	public void addFruit(List<Fruit> fruit) {
		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String hour  = new SimpleDateFormat("HH:mm:ss").format(new Date());
		String time = date+"T"+hour+"Z";
		String type_f;
		for (int i = 0; i < fruit.size(); i++) {
			if(fruit.get(i).getType() == 1)
				type_f = "banna";
			else
			{
				type_f  = "apple";
			}
			w.append("<Placemark>\r\n" + 
					"      <TimeStamp>\r\n" + 
					"        <when>"+time+"</when>\r\n" + 
					"      </TimeStamp>\r\n" + 
					"      <styleUrl>#"+type_f+"</styleUrl>\r\n" + 
					"      <Point>\r\n" + 
					"          <coordinates>"+  fruit.get(i).getLocation().y() +","+fruit.get(i).getLocation().x()+"</coordinates>\n" +
					"      </Point>\r\n" +
					"    </Placemark>"
					);
		}
	}

	/**
	 * add the robots of the game to file
	 * @param robot
	 */
	public void addRobot(List<Robot> robot) {
		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String hour = new SimpleDateFormat("HH:mm:ss").format(new Date());
		String time = date+"T"+hour+"Z";
		String type_f = "robot";
		for (int i = 0; i < robot.size(); i++) {

			w.append("<Placemark>\r\n" + 
					"      <TimeStamp>\r\n" + 
					"        <when> "+time+"</when>\r\n" + 
					"      </TimeStamp>\r\n" + 
					"      <styleUrl>#"+type_f+"</styleUrl>\r\n" + 
					"      <Point>\r\n" + 
					"          <coordinates>"+  robot.get(i).getLocation().y() +","+robot.get(i).getLocation().x()+"</coordinates>\n" +
					"      </Point>\r\n" + 
					"    </Placemark>"
					);
		}
	}

	public void save1ToKML()
	{
		w.append("  </Document>\r\n");
		w.append("</kml>");
		try {
			File file = new File(this.level + ".kml");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(w.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	public void saveToKML()
	{
		w.append("  </Document>\r\n");
		w.append("</kml>");
		try {
			PrintWriter pw = new PrintWriter(new File("name/" + this.level + ".kml"));

			pw.write(w.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	 */
}
