package jula.prog_meth_project.rubik_simulator_3d.model;

import javafx.geometry.Point3D;
import jula.prog_meth_project.rubik_simulator_3d.application.Notation;
import jula.prog_meth_project.rubik_simulator_3d.config.Config;
import jula.prog_meth_project.rubik_simulator_3d.rendering.Axis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MirrorRubik extends BaseRubik {

    public MirrorRubik() {
        rubikObjectMatrix = new ArrayList<>(3);
        for (int y = 0; y < 3 ; y++){
            rubikObjectMatrix.add(new ArrayList<>(3));
            for(int x=0;x<3;x++){
                rubikObjectMatrix.get(y).add(new ArrayList<>(Collections.nCopies(3, null)));
            }
        }
        for (int z = -1; z <= 1; z++) {
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    Cubelet cubelet = createCubelet(x, y, z);
                    rubikObjectMatrix.get(z+1).get(y+1).set(x+1,cubelet);
                    this.getChildren().add(cubelet);
                }
            }
        }
    }

    protected Point3D getLengthOfCubeletAt(int x, int y, int z){
        return new Point3D(
            Config.Mirror.CUBELET_MEDIUM_LENGTH * Math.pow(Config.Mirror.CUBELET_GROWING_RATIO_HORIZONTAL, x),
            Config.Mirror.CUBELET_MEDIUM_LENGTH * Math.pow(Config.Mirror.CUBELET_GROWING_RATIO_HORIZONTAL, y),
            Config.Mirror.CUBELET_MEDIUM_LENGTH * Math.pow(Config.Mirror.CUBELET_GROWING_RATIO_VERTICAL, z)
        );
    }

    @Override
    protected Cubelet createCubelet(int x, int y, int z) {
        Point3D lengthOfCubelet = getLengthOfCubeletAt(x,y,z);
//        System.out.println(lengthOfCubelet.toString());
        double xLength = lengthOfCubelet.getX();
        double yLength = lengthOfCubelet.getY();
        double zLength = lengthOfCubelet.getZ();
        Point3D lengthOfCenterCubelet = getLengthOfCubeletAt(0,0,0);
        Cubelet cubelet = new Cubelet(xLength, yLength, zLength, x, y, z);
        cubelet.setTranslateX((lengthOfCenterCubelet.getX()/2 + xLength / 2 + Config.Mirror.CUBELET_DISTANCE) * -x);
        cubelet.setTranslateY((lengthOfCenterCubelet.getY()/2 + yLength / 2 + Config.Mirror.CUBELET_DISTANCE) * -y);
        cubelet.setTranslateZ((lengthOfCenterCubelet.getZ()/2 + zLength / 2 + Config.Mirror.CUBELET_DISTANCE) * -z);
        return cubelet;
    }

    @Override
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
    @Override
    public ArrayList<Cubelet> getSideAny(int distance, Axis axis){
        return switch(axis){
            case X_AXIS -> getSideX(distance);
            case Y_AXIS -> getSideY(distance);
            case Z_AXIS -> getSideZ(distance);
        };
    }
    @Override
    public ArrayList<Cubelet> getSideX(int direction){
        int x=direction+1;
        ArrayList<Cubelet> result = new ArrayList<>(9);
        for(int y=0;y<3;y++){
            for(int z=0;z<3;z++){
                result.add(rubikObjectMatrix.get(z).get(y).get(x));
            }
        }
        return result;
    }
    @Override
    public ArrayList<Cubelet> getSideY(int direction){
        int y=direction+1;
        ArrayList<Cubelet> result = new ArrayList<>(9);
        for(int x=0;x<3;x++){
            for(int z=0;z<3;z++){
                result.add(rubikObjectMatrix.get(z).get(y).get(x));
            }
        }
        return result;
    }
    @Override
    public ArrayList<Cubelet> getSideZ(int direction){
        int z=direction+1;
        ArrayList<Cubelet> result = new ArrayList<>(9);
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                result.add(rubikObjectMatrix.get(z).get(y).get(x));
            }
        }
        return result;
    }
    @Override
    protected void swapFromNotation(Notation notation, Map.Entry<Integer,Integer> source, Map.Entry<Integer,Integer>  target){

        int sourceX = 0, sourceY = 0, sourceZ = 0,
                targetX = 0, targetY = 0, targetZ = 0;
        switch (notation.axis) {
            case X_AXIS: {
                sourceX = notation.direction+1;
                sourceY = source.getKey();
                sourceZ = source.getValue();
                targetX = notation.direction+1;
                targetY = target.getKey();
                targetZ = target.getValue();
                break;
            }
            case Y_AXIS: {
                sourceX = source.getKey();
                sourceY = notation.direction+1;
                sourceZ = source.getValue();
                targetX = target.getKey();
                targetY = notation.direction+1;
                targetZ = target.getValue();
                break;
            }
            case Z_AXIS: {
                sourceX = source.getKey();
                sourceY = source.getValue();
                sourceZ = notation.direction+1;
                targetX = target.getKey();
                targetY = target.getValue();
                targetZ = notation.direction+1;
                break;
            }
        }
        swapRubikMatrix(sourceX,sourceY,sourceZ,targetX,targetY,targetZ);
    }

    @Override
    protected void swapCorners(Notation notation){

        final Map.Entry<Integer,Integer>[] allCornersSequentially=new Map.Entry[]{
                Map.entry(0,0),
                Map.entry(0, 2),
                Map.entry(2, 2),
                Map.entry(2, 0),
        };
        swapThreeTimes(notation,allCornersSequentially);
    }

    @Override
    protected void swapEdges(Notation notation){
        final Map.Entry<Integer,Integer>[] allEdgesSequentially=new Map.Entry[]{
                Map.entry(0,1),
                Map.entry(1,2),
                Map.entry(2,1),
                Map.entry(1,0),
        };
        swapThreeTimes(notation, allEdgesSequentially);
    }

    @Override
    protected void swapThreeTimes(Notation notation,Map.Entry<Integer,Integer>[] sequentialOrder){
        // Y axis isn't sequential
        boolean isSequential=!notation.isInverted ==(notation.axis==Axis.Y_AXIS)==(notation.direction==-1);

        if(isSequential){
            //anti clockwise
            for (int i = 0; i < 3; i++) {
                swapFromNotation(notation, sequentialOrder[i], sequentialOrder[i + 1]);
            }
        }
        else{
            //clockwise
            for (int i = 3; i >= 1; i--) {
                swapFromNotation(notation, sequentialOrder[i], sequentialOrder[i - 1]);
            }
        }
    }

    @Override
    public void call(Notation notation){
        swapCorners(notation);
        swapEdges(notation);

    }

    @Override
    protected void swapRubikMatrix(int lhsX, int lhsY,int lhsZ, int rhsX, int rhsY,int rhsZ){
        Cubelet temp = rubikObjectMatrix.get(lhsZ).get(lhsY).get(lhsX);
        rubikObjectMatrix.get(lhsZ).get(lhsY).set(lhsX,rubikObjectMatrix.get(rhsZ).get(rhsY).get(rhsX));
        rubikObjectMatrix.get(rhsZ).get(rhsY).set(rhsX,temp);
    }
}
