package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;
/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class DB {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";
	private int times;
	private int score;
	private int rank;

	public DB(int times, int score, int rank) {
		this.times = times;
		this.score = score;
		this.rank = rank;
	}
	public DB() {
		this(0, 0, 0);
	}

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	/*public static void main(String[] args) {
		int id1 = 203156963;  // "dummy existing ID  
		int level = 0;
		//allUsers();
		//printLog();
		try {
			String s = getRank(203156963, 0);
		} catch (Exception e) {
			// TODO: handle exception
		}

		//String kml = getKML(id1,level);
		//System.out.println("***** KML file example: ******");
		//System.out.println(kml);
	}*/
	/** simply prints all the games as played by the users (in the database).
	 * 
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while(resultSet.next())
			{
				System.out.println("Id: " + resultSet.getInt("UserID")+","+resultSet.getInt("levelID")+","+resultSet.getInt("moves")+","+resultSet.getDate("time")+", "+resultSet.getInt("score"));
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				ans = resultSet.getString("kml_"+level);
			}
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	/**
	 * for q 3.b
	 * @param id
	 * @param level
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void userStatsLevel(int id, int level) {
		// key: id, value: best score for id
		HashMap<Integer, Integer> scores = new HashMap<Integer, Integer>();
		String allCustomersQuery = "SELECT * FROM Logs;";
		int counter = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				int curr_level = resultSet.getInt("levelID");
				int curr_id    = resultSet.getInt("UserID");
				int curr_score = resultSet.getInt("score");

				if(curr_level == level) {
					if( ! scores.containsKey(curr_id) ) {
						scores.put(curr_id, curr_score);
					}
					else {
						int prev_score = scores.get(curr_id);
						if (curr_score > prev_score) {
							scores.put(curr_id, curr_score);
						}
					}

					if (curr_id == id) {
						counter++;
					}
				}
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
			//throw sqle;
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
			//throw e;
		}

		if (!scores.containsKey(id)) {
			//throw new RuntimeException("user with id " + id + " doesn't exist!");
			System.out.println("user with id " + id + " doesn't exist!");
		}

		ArrayList<_user_> sorted_scores = new ArrayList<_user_>();
		for(Map.Entry<Integer,Integer> entry : scores.entrySet()) {
			_user_ u = new _user_(entry.getValue(), entry.getKey());
			sorted_scores.add(u);
		}
		sorted_scores.sort(new _user_comp_());

		int i;
		for (i = 0; i < sorted_scores.size(); i++) {
			if (sorted_scores.get(i).id == id)
				break;
		}
		score = sorted_scores.get(i).score;
		times = counter;
		rank = i;
	}

	public int getScore() {
		return score;
	}
	public int getTimes() {
		return times;
	}
	public int getRank() {
		return rank;
	}
}

class _user_ {
	int score;
	int id;
	_user_(int s, int i) {
		score = s;
		id = i;
	}
	int getScore() {
		return score;
	}
	int getId() {
		return id;
	}
}

class _user_comp_ implements Comparator<_user_> {
	public _user_comp_(){}
	public int compare(_user_ u1, _user_ u2) {
		if (u1.getScore() - u2.getScore() < 0) {
			return 1;
		}
		else if(u1.getScore() - u2.getScore() > 0) {
			return -1;
		}
		return 0;
	}
}
