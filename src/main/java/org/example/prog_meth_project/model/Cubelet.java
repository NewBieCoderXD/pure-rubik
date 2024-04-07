package org.example.prog_meth_project.model;

import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Box;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import static org.example.prog_meth_project.config.Config.*;

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
        borderBox.setTranslateX(xSign * xLength / 2.0);
        borderBox.setTranslateY(ySign * yLength / 2);
        return borderBox;
    }

    private Group mainBox;
    public Affine getAffine(){
        return (Affine)this.getTransforms().get(0);
    }
    public void setAffine(Affine affine){
        this.getTransforms().setAll(affine);
    }
    public Box buildFront(double xLength, double yLength,double zLength,Color color){
        Box back = new Box(xLength,0,zLength);
        back.setMaterial(new PhongMaterial(color));
        back.setTranslateY(yLength/2);
        return back;
    }
    public Box buildBack(double xLength, double yLength,double zLength,Color color){
        Box back = new Box(xLength,0,zLength);
        back.setMaterial(new PhongMaterial(color));
        back.setTranslateY(-yLength/2);
        return back;
    }
    public Box buildTop(double xLength, double yLength,double zLength,Color color){
        Box top = new Box(xLength,yLength,0);
        top.setMaterial(new PhongMaterial(color));
        top.setTranslateZ(zLength/2);
        return top;
    }
    public Box buildBottom(double xLength, double yLength,double zLength,Color color){
        Box bottom = new Box(xLength,yLength,0);
        bottom.setMaterial(new PhongMaterial(color));
        bottom.setTranslateZ(-zLength/2);
        return bottom;
    }
    public Box buildLeft(double xLength, double yLength,double zLength,Color color){
        Box left = new Box(0,yLength,zLength);
        left.setMaterial(new PhongMaterial(color));
        left.setTranslateX(xLength/2);
        return left;
    }
    public Box buildRight(double xLength, double yLength,double zLength,Color color){
        Box left = new Box(0,yLength,zLength);
        left.setMaterial(new PhongMaterial(color));
        left.setTranslateX(-xLength/2);
        return left;
    }

    public Cubelet(double xLength, double yLength, double zLength, Color topColor, Color rightColor, Color leftColor, Color bottomColor, Color backColor, Color frontColor) {
        Affine affine=new Affine();
        this.getTransforms().setAll(affine);

        mainBox = new Group();
        Box left = buildLeft(xLength,yLength,zLength,Color.BLUE);
        Box right = buildRight(xLength,yLength,zLength,Color.GREEN);
        Box bottom = buildBottom(xLength,yLength,zLength,Color.WHITE);
        Box back = buildBack(xLength,yLength,zLength,Color.ORANGE);
        Box front = buildFront(xLength,yLength,zLength,Color.RED);
        Box top = buildTop(xLength,yLength,zLength,Color.YELLOW);

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
        getChildren().addAll(top,bottom,front,back,left,right);
    }
    public Cubelet(double xLength, double yLength, double zLength) {
        this(xLength,yLength,zLength,CUBELET_MAIN_BOX_COLOR,CUBELET_MAIN_BOX_COLOR,CUBELET_MAIN_BOX_COLOR,CUBELET_MAIN_BOX_COLOR,CUBELET_MAIN_BOX_COLOR,CUBELET_MAIN_BOX_COLOR);
    }
}
