package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import gameComponent.Fruit;
import gameComponent.Robot;
import graph.dataStructure.graph;
import graph.dataStructure.node_data;
import graph.utils.Point3D;

public class KML_Logger {


	private String level;
	private StringBuilder content;



	public KML_Logger(String kml_name)
	{
		level = kml_name;
		content = new StringBuilder();
		content.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
						"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
						"  <Document>\r\n" +
						"    <name>	Level " + this.level + "</name>" +
						" <Style id=\"node\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/pushpin/wht-pushpin.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"banana\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/pal5/icon57.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"apple\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/pal5/icon56.png</href>\r\n" +
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


	public void Placemark(String type, Point3D Location)
	{
		Date date = new Date();
		SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
		String time = p.format(date);

		content.append("<Placemark>\r\n" + 
				"      <TimeStamp>\r\n" + 
				"        <when>"+time+"</when>\r\n" + 
				"      </TimeStamp>\r\n" + 
				"      <styleUrl>#"+type+"</styleUrl>\r\n" + 
				"      <Point>\r\n" + 
				"        <coordinates>"+ Location.toString()+ "</coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>"
				);
	}


	public void addNodes(graph g) 
	{
		for (node_data node_data : g.getV()) 
		{
			content.append("<Placemark>\n" + "    <description>" + "place num:").append(node_data.getKey()).append("</description>\n").append("    <Point>\n").append("      <coordinates>").append(node_data.getLocation().x()).append(",").append(node_data.getLocation().y()).append(",0</coordinates>\n").append("    </Point>\n").append("  </Placemark>\n");
		}
	}

	public void addRobot(Point3D Location)
	{
		String type = "#robot";
	}

	public void addFruit(List<Fruit> fruit, String Location)
	{

		String type = "";
		for(int i=0; i< fruit.size(); i++)
		{

			if (fruit.get(i).getType() == 1)
				type = "#banana";
			else
			{
				type = "#apple";
			}
			Placemark(type, fruit.get(i).getLocation());
		}
	}


	public void saveKML() {
		content.append("  </Document>\r\n");
		content.append("</kml>");
		try {
			PrintWriter pw = new PrintWriter(new File("name/" + this.level + ".kml")); 
			pw.write(content.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}
}
