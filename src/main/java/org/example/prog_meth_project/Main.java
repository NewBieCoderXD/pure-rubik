package org.example.prog_meth_project;

import com.ggFROOK.InvalidRubikNotation;
import com.ggFROOK.RubikFROOK;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.prog_meth_project.model.Cubelet;
import org.example.prog_meth_project.model.Rubik;
import org.example.prog_meth_project.pane.NotationStack;
import org.example.prog_meth_project.rendering.Rotation;
import org.example.prog_meth_project.rendering.Xform;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static org.example.prog_meth_project.Config.DRAG_SENSITIVITY;
import static org.example.prog_meth_project.Config.SECOND_PER_NOTATION;

public class Main extends Application {
    final VBox root = new VBox();
    final Xform world = new Xform();
    private static final double AXIS_LENGTH = 250.0;
    final Xform axisGroup = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    volatile static Rubik rubik;
//    volatile static Queue<Notation> notationQueue = new LinkedList<>();
    volatile static NotationQueue notationQueue = new NotationQueue();
    private static final double CAMERA_INITIAL_DISTANCE = -100;
    private static final double CAMERA_INITIAL_X_ANGLE = 45;
    private static final double CAMERA_INITIAL_Y_ANGLE = -180;
    private static final double CAMERA_INITIAL_Z_ANGLE = -45;
    private static final double CAMERA_NEAR_CLIP = 0.01;
    private static final double CAMERA_FAR_CLIP = 10000.0;

    private double startDragX;
    private double startDragY;
    volatile static RubikFROOK rubikFROOK = new RubikFROOK();
    volatile static boolean startSolving = false;
    volatile static boolean isSolving = false;
    volatile static NotationStack notationStack = NotationStack.getInstance();

    private void setAnglesText(Text text){
        text.setText(MessageFormat.format("x:{0}\ny:{1}\nz:{2}", cameraXform.rx.getAngle(), cameraXform.ry.getAngle(), cameraXform.rz.getAngle()));
    }

    private SubScene build3DSubScene(double width, double height){
        SubScene subScene = new SubScene(world, width, height, true, SceneAntialiasing.BALANCED);
        subScene.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildAxes();
        buildRubik();
        subScene.setCamera(camera);
        return subScene;
    }

    private Text buildAnglesText(){
        Text anglesText = new Text();
        anglesText.setTextAlignment(TextAlignment.LEFT);
        anglesText.setWrappingWidth(root.getPrefWidth());
        setAnglesText(anglesText);
        return anglesText;
    }

    @Override
    public void start(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        SubScene subScene=build3DSubScene(bounds.getWidth(), bounds.getHeight());

        root.setPrefWidth(bounds.getWidth());
        root.setPrefHeight(bounds.getHeight());

        Scene scene = new Scene(new StackPane(subScene, root), bounds.getWidth(), bounds.getHeight());

        Text anglesText = buildAnglesText();

        Region emptyRegion1 = new Region();
        VBox.setVgrow(emptyRegion1, Priority.ALWAYS);

        Region emptyRegion2 = new Region();
        VBox.setVgrow(emptyRegion2, Priority.ALWAYS);

        GridPane notationMenu = buildMenus();

        root.getChildren().add(anglesText);

        root.getChildren().add(emptyRegion1);

        root.getChildren().add(notationStack);

        root.getChildren().add(emptyRegion2);

        root.getChildren().add(notationMenu);

        stage.setMaximized(true);
        stage.setTitle("ISUS: rubik simulator");
        stage.setScene(scene);
        stage.show();

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

        Thread rubikAnimationThread = RubikAnimationThread.getInstance();

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startDragX = event.getSceneX();
                startDragY = event.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double xDistance = event.getSceneX() - startDragX;
                double yDistance = event.getSceneY() - startDragY;
                startDragX = event.getSceneX();
                startDragY = event.getSceneY();
                cameraXform.rz.setAngle(cameraXform.rz.getAngle() - xDistance * DRAG_SENSITIVITY);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() - yDistance * DRAG_SENSITIVITY);
                setAnglesText(anglesText);
            }
        });
        rubikAnimationThread.start();
    }

    private Button buildSolveButton(){
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
                startSolving=true;
            }
        });
        return button;
    }

    public GridPane buildMenus(){
        GridPane menu = new GridPane();
        menu.setAlignment(Pos.CENTER);
        menu.setPrefWidth(root.getPrefWidth());
        Button solveButton = buildSolveButton();
        menu.add(solveButton,0,0,1,2);
        buildMenu(menu);
        return menu;
    }

    private void buildMenu(GridPane menu){
        for(int i=0;i<Notation.values().length;i++) {
            Notation notation = Notation.values()[i];
            Button notationButton = notationButton(notation);
            menu.add(notationButton, i/2+1, notation.isInverted? 3:0);
        }
    }

    public Button notationButton(Notation notation){
        Button button = new Button(notation.toPrettyString());
        button.setFont(new Font(26));
        button.setPrefWidth(70);
        button.setPrefHeight(70);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notationQueue.add(notation);
            }
        });
        return button;
    }

    private void buildRubik() {
        rubik = new Rubik();
        rubikFROOK.initRubik();
        world.getChildren().add(rubik);
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
//        cameraXform.getChildren().add(cameraXform2);
//        cameraXform2.getChildren().add(cameraXform3);
//        cameraXform3.getChildren().add(camera);
        cameraXform.getChildren().add(camera);
        cameraXform.setRotateZ(180.0);
//        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
        cameraXform.rz.setAngle(CAMERA_INITIAL_Z_ANGLE);
    }

    private void buildAxes() {
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

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        world.getChildren().addAll(axisGroup);
    }

    public static void main(String[] args) {
        launch(args);
    }
}