package gameComponent;

import graph.utils.Point3D;

import java.io.Serializable;

public interface fruitsINT extends Serializable {
    Point3D getLocation();

    double getValue();

    double grap(Robot var1, double var2);

    int getType();
}
