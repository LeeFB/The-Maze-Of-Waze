//package gameClient.Test;
//
//import Server.Game_Server;
//import Server.game_service;
//import gameClient.AutoDrive;
//import gameClient.PlayGround;
//import gameComponent.Fruit;
//import graph.dataStructure.DGraph;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.LinkedList;
//
//class AutoDriveTest {
//    private static LinkedList<Fruit> fruits = new LinkedList<>();
//    private static AutoDrive autoDrive;
//
//    @BeforeAll
//    static void main(){
//        int scenario_num = 5;
//        game_service game = Game_Server.getServer(scenario_num);
//        String graphSTR = game.getGraph();
//        DGraph graph = new DGraph();
//        graph.init(graphSTR);
//        autoDrive = new AutoDrive();
//        String info = game.toString();
//        fruits.clear();
//        for (String s : game.getFruits()) {
//            fruits.add(new Fruit(s));
//        }
//        System.out.println(fruits.size());
//    }
//
//    @Test
//    void getStartingPoint() {
//        System.out.println("gameClient.Test getStartingPoint: \n" +
//                "The best positioning is on thous node_ID by order of most valuable fruit on edge");
//        System.out.println(autoDrive.getStartingPoint(fruits));
//
//    }
//
//    @Test
//    void closestFruit() {
//        System.out.println("gameClient.Test closestFruit: \n" +
//                "Calculate the optimal fruit from node_ID by order of most valuable fruit on graph");
//
//        System.out.println(autoDrive.closestFruit(1, fruits));
//        System.out.println(autoDrive.closestFruit(7, fruits));
//        System.out.println(autoDrive.closestFruit(12, fruits));
//        System.out.println(autoDrive.closestFruit(1, fruits));
//        System.out.println(autoDrive.closestFruit(9, fruits));
//        System.out.println(autoDrive.closestFruit(3, fruits));
//    }
//
//    @Test
//    void nextNode() {
//        System.out.println("gameClient.Test NextNode: \n" +
//                "Calculate the optimal node_ID the robot_ID should go");
//
//        System.out.println(autoDrive.valueDistFruit(fruits,1));
//        System.out.println(autoDrive.valueDistFruit(fruits,7));
//        System.out.println(autoDrive.valueDistFruit(fruits,12));
//        System.out.println(autoDrive.valueDistFruit(fruits,7));
//        System.out.println(autoDrive.valueDistFruit(fruits,12));
//        System.out.println(autoDrive.valueDistFruit(fruits,1));
//        System.out.println(autoDrive.valueDistFruit(fruits,9));
//        System.out.println(autoDrive.valueDistFruit(fruits,3));
//    }
//
//
//}