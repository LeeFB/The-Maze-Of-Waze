package gameComponent;


import graph.utils.Point3D;

import java.io.Serializable;

public interface robotINT extends Serializable {
    int getID();

    boolean setNextNode(int var1);

    int getNextNode();

    Point3D getLocation();

    boolean isMoving();

    boolean move();

    double getMoney();

    double getBatLevel();

    void randomWalk();

    double getSpeed();

    void setSpeed(double var1);

    double doubleSpeedWeight();

    double turboSpeedWeight();

    void setDoubleSpeedWeight(double var1);

    void setTurboSpeedWeight(double var1);

    void addMoney(double var1);

    int getSrcNode();
}
