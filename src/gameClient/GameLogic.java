package gameClient;

import java.util.ArrayList;
import Server.Game_Server;
import Server.game_service;
import gameComponent.*;
import graph.dataStructure.*;


public class GameLogic
{
	game_service game;
	static graph graph;
	Thread t;
	ArrayList <Robot> robots = new ArrayList <Robot>(); 
	ArrayList <Fruit> fruits = new ArrayList <Fruit>();
}
