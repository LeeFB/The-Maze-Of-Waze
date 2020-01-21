package gameClient;

import Server.Game_Server;
import Server.game_service;
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

public class PlayGround{
    private game_service game;
    private int level;
    protected DGraph graph;
    protected LinkedList<Robot> robots= new LinkedList<>();
    protected LinkedList<Fruit> fruits= new LinkedList<>();
    private boolean manuel;
    private ManualDrive manualDrive;
    private AutoDrive autoDrive;
    private KML_Logger kml;
    Point3D lastPressed = new Point3D(0,0);

    public DGraph getGraph() {
        return graph;
    }

    public PlayGround(int level, boolean bool,int id){
        this.level = level;
        Game_Server.login(999);
        game = Game_Server.getServer(level);
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
                                dest =  manualDrive.nextNodeClicked(src,lastPressed);
                            else
                                dest = autoDrive.NextNode(fruits,src);

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
    protected void getFruits() {
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
    protected void getRobots(){
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
        kml.EndAndSave_KML();
    }

    public String timeToEndStr() {
       return String.valueOf(game.timeToEnd()/1000);
    }

    public String getLevel() {
        return String.valueOf(level);
    }

    /**
     *
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

    protected void login(int id){

    }
}
