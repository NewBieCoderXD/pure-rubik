package org.example.prog_meth_project.page;

import com.ggFROOK.RubikFROOK;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Camera;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import org.example.prog_meth_project.application.Notation;
import org.example.prog_meth_project.application.NotationQueue;
import org.example.prog_meth_project.config.Config;
import org.example.prog_meth_project.model.BaseRubik;
import org.example.prog_meth_project.model.StandardRubik;
import org.example.prog_meth_project.rendering.Xform;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;

public interface RubikPage {

    public SubScene getScene();
    public AtomicBoolean getHasStartedSolving();

    public NotationQueue getNotationQueue();
//    public RubikPage getInstance();

    public BaseRubik getRubik();
    public void setRubik(BaseRubik rubik);

    public RubikFROOK getRubikSolver();

    public Xform getWorld();
//    public void setBounds(Rectangle2D bounds);
    public Pane getRoot();
    public Camera getCamera();

    public Xform getCameraXform();
    public Xform getAxisGroup();

    default Text buildAnglesText(){
        Text anglesText = new Text();
        anglesText.setTextAlignment(TextAlignment.LEFT);
        anglesText.setWrappingWidth(getRoot().getPrefWidth());
        setAnglesText(anglesText);
        return anglesText;
    }

    default void setAnglesText(Text text){
        text.setText(MessageFormat.format("x:{0}\ny:{1}\nz:{2}", getCameraXform().rx.getAngle(), getCameraXform().ry.getAngle(), getCameraXform().rz.getAngle()));
    }

    public void createScene();

    default Button buildSolveButton(){
        Button button = new Button("solve");
        button.setFont(new Font(30));
        button.setAlignment(Pos.CENTER);
        double buttonWidth = 70;
        double buttonHeight = 140;
        button.setPrefWidth(buttonHeight);
        button.setPrefHeight(buttonWidth);
        // rotate button
        button.getTransforms().add(new Rotate(-90));
        button.setTranslateX(buttonWidth);
        button.setTranslateY(buttonHeight);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getHasStartedSolving().set(true);
            }
        });
        return button;
    }

    default GridPane buildMenus(){
        GridPane menu = new GridPane();
        menu.setAlignment(Pos.CENTER);
        menu.setPrefWidth(getRoot().getPrefWidth());
        Button solveButton = buildSolveButton();
        menu.add(solveButton,0,0,1,2);
        buildMenu(menu);
        return menu;
    }

    default void buildMenu(GridPane menu){
        for(int i = 0; i< Notation.values().length; i++) {
            Notation notation = Notation.values()[i];
            Button notationButton = notationButton(notation);
            menu.add(notationButton, i/2+1, notation.isInverted? 3:0);
        }
    }

    default Button notationButton(Notation notation){
        Button button = new Button(notation.toPrettyString());
        button.setFont(new Font(26));
        button.setPrefWidth(70);
        button.setPrefHeight(70);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getNotationQueue().add(notation);
            }
        });
        return button;
    }

    public void initializeRubik();

    default void buildRubik() {
        initializeRubik();
        getRubikSolver().initRubik();
        getWorld().getChildren().add(getRubik());
    }

    default void buildCamera(){
        getRoot().getChildren().add(getCameraXform());
        getCameraXform().getChildren().add(getCamera());
        getCameraXform().setRotateZ(180.0);

        getCamera().setNearClip(Config.CAMERA_NEAR_CLIP);
        getCamera().setFarClip(Config.CAMERA_FAR_CLIP);
        getCamera().setTranslateZ(Config.CAMERA_INITIAL_DISTANCE);
        getCameraXform().ry.setAngle(Config.CAMERA_INITIAL_Y_ANGLE);
        getCameraXform().rx.setAngle(Config.CAMERA_INITIAL_X_ANGLE);
        getCameraXform().rz.setAngle(Config.CAMERA_INITIAL_Z_ANGLE);
    }

    default void buildAxes(){
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(Config.AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, Config.AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, Config.AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        xAxis.setTranslateX(Config.AXIS_LENGTH / 2);
        yAxis.setTranslateY(Config.AXIS_LENGTH / 2);
        zAxis.setTranslateZ(Config.AXIS_LENGTH / 2);

        getAxisGroup().getChildren().addAll(xAxis, yAxis, zAxis);
        getAxisGroup().setVisible(true);
        getWorld().getChildren().add(getAxisGroup());
    }
}
