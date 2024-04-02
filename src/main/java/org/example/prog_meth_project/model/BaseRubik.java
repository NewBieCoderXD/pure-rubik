package org.example.prog_meth_project.model;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import org.example.prog_meth_project.application.Notation;
import org.example.prog_meth_project.rendering.Axis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.example.prog_meth_project.config.Config.*;
import static org.example.prog_meth_project.config.Config.CUBELET_DISTANCE;

public abstract class BaseRubik extends Group {

    protected ArrayList<ArrayList<ArrayList<Cubelet>>> rubikObjectMatrix;

    public ArrayList<ArrayList<ArrayList<Cubelet>>> getObjectMatrix(){
        return rubikObjectMatrix;
    }
    public BaseRubik() {
        rubikObjectMatrix = new ArrayList<>(3);
    }

//    abstract protected Point3D getLengthOfCubeletAt(int x, int y, int z);

    abstract protected Cubelet createCubelet(int x, int y, int z);

    public ArrayList<Cubelet> getSideOfNotation(Notation notation){
//        int distance = switch (notation){
//            case R, R_ -> 1;
//            case L, L_ -> -1;
//            case F, F_ -> 1;
//            case B, B_ -> -1;
//            case D, D_ -> 1;
//            case U, U_ -> -1;
//        };
        return getSideAny(notation.direction, notation.axis);
    }
    public ArrayList<Cubelet> getSideAny(int distance, Axis axis){
        return switch(axis){
            case X_AXIS -> getSideX(distance);
            case Y_AXIS -> getSideY(distance);
            case Z_AXIS -> getSideZ(distance);
        };
    }
    abstract public ArrayList<Cubelet> getSideX(int direction);
    abstract public ArrayList<Cubelet> getSideY(int direction);
    abstract public ArrayList<Cubelet> getSideZ(int direction);
    abstract protected void swapFromNotation(Notation notation, Map.Entry<Integer,Integer> source, Map.Entry<Integer,Integer>  target);
    abstract protected void swapCorners(Notation notation);


    abstract protected void swapEdges(Notation notation);

    abstract protected void swapThreeTimes(Notation notation,Map.Entry<Integer,Integer>[] sequentialOrder);


    abstract public void call(Notation notation);

    abstract protected void swapRubikMatrix(int lhsX, int lhsY,int lhsZ, int rhsX, int rhsY,int rhsZ);
}
