package gameClient;

import Server.Game_Server;
import Server.game_service;
import de.micromata.opengis.kml.v_2_2_0.Link;
import gameComponent.Fruit;
import gameComponent.Robot;
import graph.dataStructure.DGraph;
import graph.dataStructure.edge_data;
import graph.dataStructure.node_data;
import graph.utils.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * this class is where al the gma is define and get updateds
 * from the sever throwout the game
 */
public class PlayGround {

    private static boolean sendKML = false;

    private game_service game;  //game server
    private int level;          //level of game
    protected DGraph graph;     //graph of game
    protected LinkedList<Robot> robots = new LinkedList<>(); //list of robots in game
    protected LinkedList<Fruit> fruits = new LinkedList<>();//list of fruit in game
    private boolean manuel;     //playing method
    //the driving control classes
    private ManualDrive manualDrive;
    private AutoDrive autoDrive;
    //kml
    private KML_Logger kml;
    private DB dataBase;
    Point3D lastPressed = new Point3D(0, 0);

    public DGraph getGraph() {
        return graph;
    }

    public PlayGround(int level, boolean bool, int id) {
        this.level = level;
        Game_Server.login(id);
        game = Game_Server.getServer(level);
        //init the DB
        dataBase = new DB();
        dataBase.userStatsLevel(id, level);
        graph = new DGraph();
        graph.init(game.getGraph());
        kml = new KML_Logger(level);
        manuel = bool;
        if (bool)
            manualDrive = new ManualDrive(graph);
        else
            autoDrive = new AutoDrive(graph);
    }

    /**
     * Moves each of the robots along the edge,
     * in case the robot is on a node the next destination (next edge) is chosen (randomly).
     */
    protected void moveRobots() {
        for (Robot r : robots){
            int src = r.getSrcNode();
            int dest = r.getDestNode();
            if (dest == -1)
                do {
                    if (manuel)
                        dest = manualDrive.nextNodeClicked(src, lastPressed);
                    else {
                        dest = autoDrive.whereMove(r,fruits);

                    }

                    System.out.println("Robot_ID:" + r.getID() + " was order to go from " + src + " to " + dest);
                } while (dest == -1);
            game.chooseNextEdge(r.getID(), dest);
        }
//        List<String> log = game.move();
//        if (log != null) {
//            for (String robot_json : log) {
//                try {
//                    JSONObject line = new JSONObject(robot_json);
//                    JSONObject ttt = line.getJSONObject("Robot");
//                    int rid = ttt.getInt("id");
//                    int src = ttt.getInt("src");
//                    int dest = ttt.getInt("dest");
//
//                    if (dest == -1) {
//                        do {
//                            if (manuel)
//                                dest = manualDrive.nextNodeClicked(src, lastPressed);
//                            else {
//                                dest = autoDrive.nextNode(fruits, src);
//                            }
//
////                            System.out.println("Robot_ID:" + rid + " was order to go from " + src + " to " + dest);
//                        } while (dest == -1);
//                        game.chooseNextEdge(rid, dest);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    /**
     * ask for number of Robots in game and added them to the server manually
     */
    protected void addRobots() {
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
    protected void addRobotsAuto() {
        String info = game.toString();

        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("robots");
            LinkedList<Integer> list = autoDrive.getStartingPoint(fruits);
            for (int i = 0; i < rs; i++) {
                if (i <= list.size()) {
                    game.addRobot(list.get(i));
                } else {
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
    protected void pullFruits() {
        for (String s : game.getFruits()) {
            Fruit f = new Fruit(s);
            if (f.getType() == 1) {
                kml.PlaceMark("apple", f.getLocation());
            } else {
                kml.PlaceMark("banana", f.getLocation());
            }
            f.setEdge(findEdge(f.getLocation(), f.getType()));
            fruits.add(f);

        }
    }

    protected void pullRobots() {
        robots.clear();
        int i = 0;
        for (String s : game.getRobots()) {
            robots.add(new Robot(s));
            kml.PlaceMark("robot", robots.get(i).getLocation());
            i++;
        }
    }


    /**
     * @return - return the current grade in server
     * in the form of String
     */
    protected String getGrade() {
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject obj = line.getJSONObject("GameServer");
            return String.valueOf(obj.getDouble("grade"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return path to map Image in utils
     */
    protected String getMap() {
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

    public void startGame() {
        game.startGame();
    }

    public boolean isRunning() {
        return game.isRunning();
    }

    public void stopGame() {
        game.stopGame();

        String results = game.toString();
        System.out.println(results);
        if (sendKML)
            game.sendKML(kml.EndAndSave_KML());
    }

    public String timeToEndStr() {
        return String.valueOf(game.timeToEnd() / 1000);
    }

    public String getLevel() {
        return String.valueOf(level);
    }

    /**
     * @param point - is the last pressed mouse position
     */
    public void lastPressed(Point3D point) {
        lastPressed = point;
    }

    public String getMoveNumber() {
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject obj = line.getJSONObject("GameServer");
            return String.valueOf(obj.getDouble("moves"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPlayedGamesNum() {
        if (dataBase != null)
            return dataBase.getTimes();
        else
            throw new RuntimeException("ERR: The Database is not connected");
    }

    public String getMaxUserLevel() {
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject obj = line.getJSONObject("GameServer");
            return String.valueOf(obj.getDouble("max_user_level"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param p    - a point location on the graph
     * @param type - the type of the fruit we want to associate with the edge
     * @return - the corresponding edge
     */
    private edge_data findEdge(Point3D p, int type) {
        for (node_data node : graph.getV()) {                    //go over all nodes on graph
            for (edge_data edge : graph.getE(node.getKey())) {    //go over all edges corresponding node

                Point3D src = graph.getNode(edge.getSrc()).getLocation();
                Point3D dst = graph.getNode(edge.getDest()).getLocation();

                double edge_len = src.distance3D(dst);            //the edge len
                double pTop_len = p.distance3D(src) + p.distance3D(dst);    //the len between each node in graph and the @param p
                double EPS = 0.00000000000001;

                if (Math.abs(edge_len - pTop_len) < EPS)
                    if ((type == 1 && edge.getSrc() < edge.getDest()))
                        return edge;
                    else if (type == -1 && edge.getSrc() > edge.getDest())
                        return edge;
            }
        }
        System.out.println("No corresponding edge");
        return null;
    }

    public void updateFruits() {

        LinkedList<Fruit> temp = new LinkedList<>();
        for (Fruit f : fruits){
            System.out.print(f.getTag() +", ");
        }

        System.out.println();

        //temp List
        for (String s : game.getFruits()) {
            Fruit f = new Fruit(s);
            if (!fruits.contains(f))
                f.setEdge(findEdge(f.getLocation(),f.getType()));
            temp.add(f);
        }
        //TODO fix tag
        for (Fruit f: temp){
            if (fruits.contains(f)){
                //TODO ther is the problam
                System.out.println("hgffhgfghfhgfgh");
                f.setTag(fruits.get(fruits.indexOf(f)).getTag());
            }
        }


        fruits = temp;
        for (Fruit f : fruits){
            System.out.print(f.getTag() +", ");
        }
        System.out.println();


//
//        Fruit[] tempArr = new Fruit[fruits.size()];
//        tempArr = fruits.toArray(new Fruit[0]);
//
//        for (Fruit f : tempArr){
//            if (fruits.contains(f))
//                temp.remove(f);
//
//        }
//
//        fruits.removeIf(f -> !temp.contains(f));
//
//        for (Fruit f : temp) {
//            if (f.getType() == 1)
//                kml.PlaceMark("apple", f.getLocation());
//            else
//                kml.PlaceMark("banana", f.getLocation());
//
//            f.setEdge(findEdge(f.getLocation(), f.getType()));
//            fruits.add(f);
//
//        }

    }

    public void updateRobots() {
        robots.clear();

        List<String> log = game.move();
        if (log != null) {
            for (String robot_json : log) {
                Robot r = new Robot(robot_json);
                robots.add(r);
            }
        }
    }

    public boolean getManuel() {
        return this.manuel;
    }
}
