package org.example.prog_meth_project.model;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import static org.example.prog_meth_project.Config.*;

public class Cubelet extends Group {
    private void setMaterialOfBox(Box borderBox) {
        PhongMaterial material = new PhongMaterial(CUBELET_BORDER_COLOR);
        borderBox.setMaterial(material);
    }

    private Box makeXBorder(double xLength, double yLength, double zLength, int ySign, int zSign) {
        Box borderBox = new Box(xLength + CUBELET_BORDER_WIDTH, CUBELET_BORDER_WIDTH, CUBELET_BORDER_WIDTH);
        setMaterialOfBox(borderBox);
        borderBox.setTranslateY(ySign * yLength / 2);
        borderBox.setTranslateZ(zSign * zLength / 2);
        return borderBox;
    }

    private Box makeYBorder(double xLength, double yLength, double zLength, int xSign, int zSign) {
        Box borderBox = new Box(CUBELET_BORDER_WIDTH, yLength + CUBELET_BORDER_WIDTH, CUBELET_BORDER_WIDTH);
        setMaterialOfBox(borderBox);
        borderBox.setTranslateX(xSign * xLength / 2);
        borderBox.setTranslateZ(zSign * zLength / 2);
        return borderBox;
    }

    private Box makeZBorder(double xLength, double yLength, double zLength, int xSign, int ySign) {
        Box borderBox = new Box(CUBELET_BORDER_WIDTH, CUBELET_BORDER_WIDTH, zLength + CUBELET_BORDER_WIDTH);
        setMaterialOfBox(borderBox);
        borderBox.setTranslateX(xSign * xLength / 2);
        borderBox.setTranslateY(ySign * yLength / 2);
        return borderBox;
    }

    public Cubelet(double xLength, double yLength, double zLength) {
        Box mainBox = new Box(xLength, yLength, zLength);
        PhongMaterial material = new PhongMaterial(CUBELET_MAIN_BOX_COLOR);
        mainBox.setMaterial(material);

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                getChildren().add(makeXBorder(xLength, yLength, zLength, i, j));
            }
        }
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                getChildren().add(makeYBorder(xLength, yLength, zLength, i, j));
            }
        }
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                getChildren().add(makeZBorder(xLength, yLength, zLength, i, j));
            }
        }
        getChildren().addAll(mainBox);
    }
}
