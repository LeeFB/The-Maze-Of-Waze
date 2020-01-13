package gameComponent;

import Server.robot;
import graph.utils.Point3D;

import java.io.Serializable;

public interface fruitsINT extends Serializable {
    Point3D getLocation();

    double getValue();

    double grap(robot var1, double var2);

    int getType();
}
