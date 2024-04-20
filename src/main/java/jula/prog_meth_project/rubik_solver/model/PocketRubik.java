package jula.prog_meth_project.rubik_solver.model;

import jula.prog_meth_project.rubik_solver.application.Notation;
import jula.prog_meth_project.rubik_solver.config.Config;
import jula.prog_meth_project.rubik_solver.rendering.Axis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class PocketRubik extends BaseRubik {
    public int dimension = 2;
    public PocketRubik() {
        rubikObjectMatrix = new ArrayList<>(dimension);
        for (int y = 0; y < dimension; y++) {
            rubikObjectMatrix.add(new ArrayList<>(dimension));
            for (int x = 0; x < dimension; x++) {
                rubikObjectMatrix.get(y).add(new ArrayList<>(Collections.nCopies(dimension, null)));
            }
        }
        for (int z = -1; z <= 1; z+=2) {
            for (int y = -1; y <= 1; y+=2) {
                for (int x = -1; x <= 1; x+=2) {
                    Cubelet cubelet = createCubelet(x, y, z);
                    int indexX = (x+1)/2;
                    int indexY = (y+1)/2;
                    int indexZ = (z+1)/2;
                    rubikObjectMatrix.get(indexZ).get(indexY).set(indexX, cubelet);
                    this.getChildren().add(cubelet);
                }
            }
        }
    }

    @Override
    protected Cubelet createCubelet(int x, int y, int z) {
        double lengthOfCubelet = Config.Standard.CUBELET_LENGTH;

        Cubelet cubelet = new Cubelet(lengthOfCubelet, lengthOfCubelet, lengthOfCubelet, x, y, z);

        cubelet.setTranslateX((lengthOfCubelet + Config.Standard.CUBELET_DISTANCE) * -x/2);
        cubelet.setTranslateY((lengthOfCubelet + Config.Standard.CUBELET_DISTANCE) * -y/2);
        cubelet.setTranslateZ((lengthOfCubelet + Config.Standard.CUBELET_DISTANCE) * -z/2);

        return cubelet;
    }

    @Override
    public ArrayList<Cubelet> getSideX(int direction) {
        int x=(direction+1)/2;
        ArrayList<Cubelet> result = new ArrayList<>((int) Math.pow(dimension,2));
        for(int y=0;y<dimension;y++){
            for(int z=0;z<dimension;z++){
                result.add(rubikObjectMatrix.get(z).get(y).get(x));
            }
        }
        return result;
    }

    @Override
    public ArrayList<Cubelet> getSideY(int direction) {
        int y=(direction+1)/2;
        ArrayList<Cubelet> result = new ArrayList<>(dimension*dimension);
        for(int x=0;x<dimension;x++){
            for(int z=0;z<dimension;z++){
                result.add(rubikObjectMatrix.get(z).get(y).get(x));
            }
        }
        return result;
    }

    @Override
    public ArrayList<Cubelet> getSideZ(int direction) {
        int z=(direction+1)/2;
        ArrayList<Cubelet> result = new ArrayList<>(dimension*dimension);
        for(int x=0;x<dimension;x++){
            for(int y=0;y<dimension;y++){
                result.add(rubikObjectMatrix.get(z).get(y).get(x));
            }
        }
        return result;
    }

    @Override
    protected void swapFromNotation(Notation notation, Map.Entry<Integer, Integer> source, Map.Entry<Integer, Integer> target) {
        int sourceX = 0, sourceY = 0, sourceZ = 0,
                targetX = 0, targetY = 0, targetZ = 0;
        switch (notation.axis) {
            case X_AXIS: {
                sourceX = (notation.direction+1)/2;
                sourceY = source.getKey();
                sourceZ = source.getValue();
                targetX = (notation.direction+1)/2;
                targetY = target.getKey();
                targetZ = target.getValue();
                break;
            }
            case Y_AXIS: {
                sourceX = source.getKey();
                sourceY = (notation.direction+1)/2;
                sourceZ = source.getValue();
                targetX = target.getKey();
                targetY = (notation.direction+1)/2;
                targetZ = target.getValue();
                break;
            }
            case Z_AXIS: {
                sourceX = source.getKey();
                sourceY = source.getValue();
                sourceZ = (notation.direction+1)/2;
                targetX = target.getKey();
                targetY = target.getValue();
                targetZ = (notation.direction+1)/2;
                break;
            }
        }
        swapRubikMatrix(sourceX,sourceY,sourceZ,targetX,targetY,targetZ);
    }

    @Override
    protected void swapCorners(Notation notation) {
        final Map.Entry<Integer,Integer>[] allCornersSequentially=new Map.Entry[]{
                Map.entry(0,0),
                Map.entry(0,1),
                Map.entry(1,1),
                Map.entry(1,0),
        };

        swapThreeTimes(notation, allCornersSequentially);
    }

    @Override
    protected void swapEdges(Notation notation) {

    }

    @Override
    protected void swapThreeTimes(Notation notation, Map.Entry<Integer, Integer>[] sequentialOrder) {
        boolean isSequential=!notation.isInverted ==(notation.axis== Axis.Y_AXIS)==(notation.direction==-1);
        if(isSequential){
            for(int i=0;i<3;i++){
                swapFromNotation(notation,sequentialOrder[i],sequentialOrder[i+1]);
            }
        }
        else{
            for(int i=3;i>=1;i--){
                swapFromNotation(notation,sequentialOrder[i],sequentialOrder[i-1]);
            }
        }
    }

    @Override
    public void call(Notation notation) {
        swapCorners(notation);
        swapEdges(notation);
    }

    @Override
    protected void swapRubikMatrix(int lhsX, int lhsY, int lhsZ, int rhsX, int rhsY, int rhsZ) {
        Cubelet temp = rubikObjectMatrix.get(lhsZ).get(lhsY).get(lhsX);
        rubikObjectMatrix.get(lhsZ).get(lhsY).set(lhsX,rubikObjectMatrix.get(rhsZ).get(rhsY).get(rhsX));
        rubikObjectMatrix.get(rhsZ).get(rhsY).set(rhsX,temp);
    }
}

