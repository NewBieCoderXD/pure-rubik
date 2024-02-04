package org.example.prog_meth_project;

import com.ggFROOK.InvalidRubikNotation;
import com.ggFROOK.RubikFROOK;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.prog_meth_project.model.Cubelet;
import org.example.prog_meth_project.model.Rubik;
import org.example.prog_meth_project.rendering.Xform;

import java.io.IOException;
import java.io.InvalidClassException;
import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.Queue;

import static org.example.prog_meth_project.Config.*;

public class Main extends Application {
    final Group root = new Group();
    final Xform world = new Xform();
    private static final double AXIS_LENGTH = 250.0;
    final Xform axisGroup = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private Rubik rubik;
    private Queue<Notation> notationQueue = new ArrayDeque<>();
    private static final double CAMERA_INITIAL_DISTANCE = -100;
    private static final double CAMERA_INITIAL_X_ANGLE = -52;
    private static final double CAMERA_INITIAL_Y_ANGLE = -184;
    private static final double CAMERA_INITIAL_Z_ANGLE = 138;
    private static final double CAMERA_NEAR_CLIP = 0.01;
    private static final double CAMERA_FAR_CLIP = 10000.0;

    private double startDragX;
    private double startDragY;
    private final ParallelTransition pt = new ParallelTransition();

    private RubikFROOK rubikFROOK = new RubikFROOK();

    private void setAnglesText(Text text){
        text.setText(MessageFormat.format("\nx: {0}\ny: {1}\nz:{2}", cameraXform.rz.getAngle(), cameraXform.ry.getAngle(), cameraXform.rz.getAngle()));
    }

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        SubScene subScene = new SubScene(new Group(world), bounds.getWidth(), bounds.getHeight(), true, SceneAntialiasing.BALANCED);
        subScene.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildAxes();
        buildRubik();
        subScene.setCamera(camera);
        Scene scene = new Scene(new Group(subScene, root), bounds.getWidth(), bounds.getHeight());

//        stage.setFullScreen(true);
        stage.setMaximized(true);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        notationQueue.add(Notation.R);
        notationQueue.add(Notation.R_);
        notationQueue.add(Notation.L);
        notationQueue.add(Notation.L_);
//        notationQueue.add(Notation.U);
//        notationQueue.add(Notation.U_);
//        notationQueue.add(Notation.D);
//        notationQueue.add(Notation.D_);
//        notationQueue.add(Notation.F);
//        notationQueue.add(Notation.F_);
//        notationQueue.add(Notation.B);
//        notationQueue.add(Notation.B_);

        startAnimation();
        ERROR HERE
        System.out.println(rubikFROOK.mainSolving());
        // not work because called instantly

        Text text = new Text();
        setAnglesText(text);
        root.getChildren().add(text);
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
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + yDistance * DRAG_SENSITIVITY);
                setAnglesText(text);
            }
        });
    }

    private void buildRubik() {
        rubik = new Rubik();
        world.getChildren().add(rubik);
    }

    private void startAnimation(){
        if(pt.getStatus()== Animation.Status.RUNNING){
            return;
        }
        Notation notation = notationQueue.poll();
        if(notation==null){
            return;
        }
        pt.getChildren().clear();
        for(Cubelet cubelet:rubik.getSideOfNotation(notation)){
            Rotate rotate = new Rotate();
            rotate.setPivotX(-cubelet.getTranslateX());
            rotate.setPivotY(-cubelet.getTranslateY());
            rotate.setPivotZ(-cubelet.getTranslateZ());
            rotate.setAxis(notation.axis.toPoint3D());
            rotate.setAngle(0);
            cubelet.getTransforms().add(rotate);
            int sign = 1;
            if(notation.IsInverted){
               sign=-1;
            }
            sign*=notation.direction;
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(rotate.angleProperty(), 0)
                    ),
                    new KeyFrame(Duration.seconds(SECOND_PER_NOTATION),
                            new KeyValue(rotate.angleProperty(), sign*90)
                    )
            );
            timeline.setCycleCount(1);
            pt.getChildren().add(timeline);
        }
        callNotation(notation,true);
        pt.setOnFinished(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                startAnimation();
            }
        });
        pt.play();
    }

    private void callNotation(Notation notation, boolean IsScramble){
        try{
            if(rubikFROOK.getRubik()==null){
                rubikFROOK.initRubik();
            }
            System.out.println(notation.toString());
            rubikFROOK.call(notation.toString(),IsScramble);
        }
        catch(InvalidRubikNotation e){
            System.out.println(e);
        }
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

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