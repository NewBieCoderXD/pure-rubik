package jula.prog_meth_project.rubik_solver.rendering;

import javafx.geometry.Point3D;

public enum Axis {
    X_AXIS(new Point3D(1,0,0)),
    Y_AXIS(new Point3D(0,1,0)),
    Z_AXIS(new Point3D(0,0,1));
    private final Point3D point3D;
    Axis(Point3D point3D) {
        this.point3D=point3D;
    }
    public Point3D toPoint3D(){
        return this.point3D;
    }
}