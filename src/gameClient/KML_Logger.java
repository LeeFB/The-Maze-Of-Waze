package gameClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Server.game_service;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import gameComponent.Fruit;
import gameComponent.Robot;
import graph.dataStructure.DGraph;
import graph.dataStructure.graph;
import graph.dataStructure.node_data;
import graph.utils.Point3D;

public class KML_Logger {

	private String level;
	private StringBuilder w;



	public void StartKml(String name) {
		this.level = name;
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

	public void addFruit(List<Fruit> fruit)
	{
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

	public void addRobot(List<Robot> robot)
	{
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

	public void saveKML() 
	{
		w.append("  </Document>\r\n");
		w.append("</kml>");
		try {
			PrintWriter pw = new PrintWriter(new File("name/" + this.level + ".kml")); 
			pw.write(w.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}
}
