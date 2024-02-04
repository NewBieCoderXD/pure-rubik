package org.example.prog_meth_project.model;

import javafx.scene.Group;
import org.example.prog_meth_project.Notation;

import java.util.ArrayList;
import java.util.Collections;

import static org.example.prog_meth_project.Config.*;

public class Rubik extends Group {
    private final ArrayList<ArrayList<ArrayList<Cubelet>>> rubikMatrix;

    public Rubik() {
        rubikMatrix = new ArrayList<>(3);
        for (int y = 0; y < 3 ; y++){
            rubikMatrix.add(new ArrayList<>(3));
            for(int x=0;x<3;x++){
                rubikMatrix.get(y).add(new ArrayList<>(Collections.nCopies(3, null)));
            }
        }
        for (int z = -1; z <= 1; z++) {
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    double xLength = CUBELET_SMALLEST_WIDTH * Math.pow(CUBELET_GROWING_RATIO_HORIZONTAL, x + 1);
                    double yLength = CUBELET_SMALLEST_WIDTH * Math.pow(CUBELET_GROWING_RATIO_HORIZONTAL, y + 1);
                    double zLength = CUBELET_SMALLEST_HEIGHT * Math.pow(CUBELET_GROWING_RATIO_VERTICAL, z + 1);
                    Cubelet cubelet = new Cubelet(xLength, yLength, zLength);
                    cubelet.setTranslateX((CUBELET_SMALLEST_WIDTH * CUBELET_GROWING_RATIO_HORIZONTAL + xLength / 2 + CUBELET_DISTANCE) * -x);
                    cubelet.setTranslateY((CUBELET_SMALLEST_WIDTH * CUBELET_GROWING_RATIO_HORIZONTAL + yLength / 2 + CUBELET_DISTANCE) * -y);
                    cubelet.setTranslateZ((CUBELET_SMALLEST_HEIGHT * CUBELET_GROWING_RATIO_VERTICAL + zLength / 2 + CUBELET_DISTANCE) * -z);
                    rubikMatrix.get(z+1).get(y+1).set(x+1,cubelet);
                    this.getChildren().add(cubelet);
                }
            }
        }
    }
    public ArrayList<Cubelet> getSideOfNotation(Notation notation){
        return switch (notation){
            case R, R_ -> getSideX(1);
        };
    }
    public ArrayList<Cubelet> getSideZ(int z){
        z++;
        ArrayList<Cubelet> result = new ArrayList<>(9);
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                result.add(rubikMatrix.get(z).get(y).get(x));
            }
        }
        return result;
    }
    public ArrayList<Cubelet> getSideX(int x){
        x++;
        ArrayList<Cubelet> result = new ArrayList<>(9);
        for(int y=0;y<3;y++){
            for(int z=0;z<3;z++){
                result.add(rubikMatrix.get(z).get(y).get(x));
            }
        }
        return result;
    }
}
