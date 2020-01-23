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
import java.util.Iterator;
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

	/**
	 * constructor
	 */
	public DB() {
	}

	/**
	 * the function hold all the info of the all levels game
	 * @param id
	 * @return the string of the info
	 */
	public String getInfo(int id) {
		return userStats(id);
	}

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	//public static void main(String[] args) {
	//int id1 = 317862068;  // "dummy existing ID
	//int level = 0;
	//try {
	//	DB db = new DB(317862068);
	//	} catch (Exception e) {
	// TODO: handle exception
	//}

	//DB db = new DB();
	//String s = db.getInfo(317862068);
	//System.out.println(s);
	//System.out.println(db.toString());
	//String kml = getKML(id1,level);
	//System.out.println("***** KML file example: ******");
	//System.out.println(kml);
	//}
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
	 * the function calculates player statistics in string
	 * @param id
	 * @return the string info of the player statistics
	 */
	private String userStats(int id) {
		String allCustomersQuery = "SELECT levelID FROM Logs;";
		HashMap<Integer, Integer> levels = new HashMap<Integer, Integer>();
		int total_play = 0;
		String ans = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection =
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			//System.out.println("userID: " + id);
			ans += "userID: " + String.valueOf(id) + "\n";
			while(resultSet.next()) {
				int level = resultSet.getInt("levelID");
				if (levels.containsKey(level)) {
					continue;
				}
				levels.put(level, 0);
				internal_stats s = userStatsLevel(id, level);
				if (s != null) {
					total_play += s.times;
					//System.out.println("levelID: " + level + ", score: " + s.score + ", rank: " + s.rank + ", played " + s.times + " times");
					ans += "levelID: " + String.valueOf(level);
					ans += ", score: " + String.valueOf(s.score);
					ans += ", rank: " + String.valueOf(s.rank);
					ans += ", played " + String.valueOf(s.times);
					ans += " times" + "\n";

				}
			}
			//System.out.println("played total of " + total_play + " times in all levels");
			ans += "played total of " + String.valueOf(total_play);
			ans += " times in all levels";
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
		return ans;
	}


	/**
	 * the function calculates player statistics in a Specific stage
	 * @param id - the id of the player
	 * @param level - the stage from the game where I want to calculate the statistics
	 */
	private internal_stats userStatsLevel(int id, int level) {
		// key: id, value: best score for id
		HashMap<Integer, Integer> scores = new HashMap<Integer, Integer>();
		String allCustomersQuery = "SELECT userID,levelID, score FROM Logs where levelID="+level;
		int counter = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection =
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				int curr_id    = resultSet.getInt("UserID");
				int curr_score = resultSet.getInt("score");

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
			return null;
		}
		//sorted the data by score
		ArrayList<internal_user> sorted_scores = new ArrayList<internal_user>();
		for(Map.Entry<Integer,Integer> entry : scores.entrySet()) {
			internal_user u = new internal_user(entry.getValue(), entry.getKey());
			sorted_scores.add(u);
		}
		sorted_scores.sort(new internal_usercomp());

		int i;
		for (i = 0; i < sorted_scores.size(); i++) {
			if (sorted_scores.get(i).id == id)
				break;
		}
		return new internal_stats(sorted_scores.get(i).score, counter, i);
	}
}
/**
 *
 * this class hold data of the player in the games
 *
 */
class internal_stats  {
	public int score;
	public int times;
	public int rank;
	internal_stats(int s, int t, int r) {
		score=s;
		times=t;
		rank=r;
	}
};

/**
 *
 *this class hold data of the player
 */
class internal_user {
	public int score;
	public int id;
	internal_user(int s, int i) {
		score = s;
		id = i;
	}
}

/**
 * sort the users
 */
class internal_usercomp implements Comparator<internal_user> {
	public internal_usercomp(){}
	public int compare(internal_user u1, internal_user u2) {
		if (u1.score - u2.score < 0) {
			return 1;
		}
		else if(u1.score - u2.score > 0) {
			return -1;
		}
		return 0;
	}
}
