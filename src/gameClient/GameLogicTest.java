package gameClient;

import Server.Game_Server;
import Server.game_service;
import gameComponent.Fruit;
import graph.dataStructure.DGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

class GameLogicTest {
    private static int scenario_num;
    private static DGraph graph;
    private static LinkedList<Fruit> fruits = new LinkedList<>();
    private static GameLogic gameLogic;



    @BeforeAll
    static void main(){
        scenario_num = 5;
        game_service game = Game_Server.getServer(scenario_num);
        String graphSTR = game.getGraph();
        graph = new DGraph();
        graph.init(graphSTR);
        gameLogic = new GameLogic(graph,graphSTR);
        String info = game.toString();
        fruits.clear();
        for (String s : game.getFruits()) {
            fruits.add(new Fruit(s));
        }
    }

    @Test
    void getStartingPoint() {
        System.out.println("Test getStartingPoint: \n" +
                "The best positioning is on thous node_ID by order of most valuable fruit on edge");
        System.out.println(gameLogic.getStartingPoint(fruits));

    }

    @Test
    void closestFruit() {
        System.out.println("Test closestFruit: \n" +
                "Calculate the optimal fruit from node_ID by order of most valuable fruit on graph");

        System.out.println(gameLogic.closestFruit(fruits,1));
        System.out.println(gameLogic.closestFruit(fruits,7));
        System.out.println(gameLogic.closestFruit(fruits,12));
        System.out.println(gameLogic.closestFruit(fruits,1));
        System.out.println(gameLogic.closestFruit(fruits,9));
        System.out.println(gameLogic.closestFruit(fruits,3));
    }

    @Test
    void nextNode() {
        System.out.println("Test NextNode: \n" +
                "Calculate the optimal node_ID the robot_ID should go");

        System.out.println(gameLogic.NextNode(fruits,1));
        System.out.println(gameLogic.NextNode(fruits,7));
        System.out.println(gameLogic.NextNode(fruits,12));
        System.out.println(gameLogic.NextNode(fruits,7));
        System.out.println(gameLogic.NextNode(fruits,12));
        System.out.println(gameLogic.NextNode(fruits,1));
        System.out.println(gameLogic.NextNode(fruits,9));
        System.out.println(gameLogic.NextNode(fruits,3));
    }


}