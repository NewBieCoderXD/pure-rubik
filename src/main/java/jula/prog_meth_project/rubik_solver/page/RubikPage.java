package jula.prog_meth_project.rubik_solver.page;

import com.ggFROOK.RubikFROOK;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import jula.prog_meth_project.rubik_solver.application.Notation;
import jula.prog_meth_project.rubik_solver.application.NotationQueue;
import jula.prog_meth_project.rubik_solver.application.RubikAnimationThread;
import jula.prog_meth_project.rubik_solver.component.NotationStack;
import jula.prog_meth_project.rubik_solver.model.BaseRubik;
import jula.prog_meth_project.rubik_solver.rendering.Xform;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;

import static jula.prog_meth_project.rubik_solver.config.Config.*;
import static jula.prog_meth_project.rubik_solver.config.Config.Camera.*;

public interface RubikPage {
    public String getName();
    public VBox getScene();
    public AtomicBoolean getHasStartedSolving();
    public NotationQueue getNotationQueue();
//    public RubikPage getInstance();
    public BaseRubik getRubik();
    public RubikFROOK getRubikSolver();
    public AtomicBoolean getIsSolving();
    public Group getWorld();
//    public void setBounds(Rectangle2D bounds);
    public Pane getRoot();
    public Camera getCamera();
    public Xform getCameraXform();
    public Group getAxisGroup();
    public NotationStack getNotationStack();
    public double getStartDragX();
    public double getStartDragY();
    public void setStartDragX(double d);
    public void setStartDragY(double d);
    public SubScene getSubScene3DView();
    public void setScene(VBox scene);
    public void setSubScene3DView(SubScene subScene3DView);

    default Text buildAnglesText(){
        Text anglesText = new Text();
        anglesText.setTextAlignment(TextAlignment.LEFT);
        anglesText.setWrappingWidth(getRoot().getPrefWidth());
        setAnglesText(anglesText);
        StackPane.setAlignment(anglesText,Pos.TOP_LEFT);
        return anglesText;
    }

    default void setAnglesText(Text text){
        text.setText(MessageFormat.format("x:{0}\ny:{1}\nz:{2}", getCameraXform().rx.getAngle(), getCameraXform().ry.getAngle(), getCameraXform().rz.getAngle()));
    }

    default void build3DSubScene(double width, double height){
        SubScene subScene = new SubScene(getWorld(), width, height, true, SceneAntialiasing.BALANCED);
        subScene.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildAxes();
        buildRubik();
        subScene.setCamera(getCamera());
        setSubScene3DView(subScene);
    };

    default void createScene(){
        build3DSubScene(INIT_SUBSCENE_WIDTH,INIT_SUBSCENE_HEIGHT);

        setScene(new VBox(new StackPane(getSubScene3DView(),getRoot())));

        Text anglesText = buildAnglesText();

        GridPane notationMenu = buildMenus();

        getRoot().getChildren().add(anglesText);

        getRoot().getChildren().add(getNotationStack());

        getRoot().getChildren().add(notationMenu);

    //        notationQueue.add(Notation.R);
    //        notationQueue.add(Notation.R_);
    //        notationQueue.add(Notation.L);
    //        notationQueue.add(Notation.L_);
    //        notationQueue.add(Notation.U);
    //        notationQueue.add(Notation.U_);
    //        notationQueue.add(Notation.D);
    //        notationQueue.add(Notation.D_);
    //        notationQueue.add(Notation.F);
    //        notationQueue.add(Notation.F_);
    //        notationQueue.add(Notation.B);
    //        notationQueue.add(Notation.B_);

        Thread rubikAnimationThread = new RubikAnimationThread(getNotationQueue(), getIsSolving(), getHasStartedSolving(), getRubikSolver(), getRubik());


        getScene().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStartDragX(event.getSceneX());
                setStartDragY(event.getSceneY());
            }
        });
        getScene().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double xDistance = event.getSceneX() - getStartDragX();
                double yDistance = event.getSceneY() - getStartDragY();
                setStartDragX(event.getSceneX());
                setStartDragY(event.getSceneY());
                getCameraXform().rz.setAngle(getCameraXform().rz.getAngle() - xDistance * DRAG_SENSITIVITY);
                getCameraXform().rx.setAngle(getCameraXform().rx.getAngle() - yDistance * DRAG_SENSITIVITY);
                setAnglesText(anglesText);
            }
        });
        rubikAnimationThread.start();
    }

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
        menu.setAlignment(Pos.BOTTOM_CENTER);
        menu.setPrefWidth(getRoot().getPrefWidth());
        Button solveButton = buildSolveButton();
        menu.add(solveButton,0,0,1,2);
        buildMenu(menu);
        StackPane.setAlignment(menu,Pos.BOTTOM_CENTER);
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

    default void buildRubik() {
        getRubikSolver().initRubik();
        getWorld().getChildren().add(getRubik());
    }

    default void buildCamera(){
        getCameraXform().getChildren().add(getCamera());

        getCamera().setNearClip(CAMERA_NEAR_CLIP);
        getCamera().setFarClip(CAMERA_FAR_CLIP);
        getCamera().setTranslateZ(CAMERA_INITIAL_DISTANCE);
        getCameraXform().ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        getCameraXform().rx.setAngle(CAMERA_INITIAL_X_ANGLE);
        getCameraXform().rz.setAngle(CAMERA_INITIAL_Z_ANGLE);
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

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        xAxis.setTranslateX(AXIS_LENGTH / 2);
        yAxis.setTranslateY(AXIS_LENGTH / 2);
        zAxis.setTranslateZ(AXIS_LENGTH / 2);

        getAxisGroup().getChildren().addAll(xAxis, yAxis, zAxis);
        getAxisGroup().setVisible(true);
        getWorld().getChildren().add(getAxisGroup());
    }
}
