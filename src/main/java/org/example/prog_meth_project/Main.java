package org.example.prog_meth_project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.scene.transform.Rotate;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;
import org.example.prog_meth_project.rendering.Xform;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;
import javafx.stage.Window;

public class Main extends Application {
    final Group root = new Group();
    final Xform world = new Xform();
    private static final double AXIS_LENGTH = 250.0;
    final Xform axisGroup = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -100;
    private static final double CAMERA_INITIAL_X_ANGLE = -52;
    private static final double CAMERA_INITIAL_Y_ANGLE = -184;
    private static final double CAMERA_INITIAL_Z_ANGLE = 138;
//    private static double CAMERA_X_ANGLE = CAMERA_INITIAL_X_ANGLE;
    private static double cameraXAngle = CAMERA_INITIAL_X_ANGLE;
    private static double cameraYAngle = CAMERA_INITIAL_Y_ANGLE;
    private static double cameraZAngle = CAMERA_INITIAL_Z_ANGLE;

    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        SubScene subScene = new SubScene(new Group(world), bounds.getWidth(), bounds.getHeight());
        buildCamera();
        buildAxes();
        buildRubik();
        subScene.setCamera(camera);
        Scene scene = new Scene(new Group(subScene,root), bounds.getWidth(), bounds.getHeight());

//        stage.setFullScreen(true);
        stage.setMaximized(true);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        Text text= new Text();
        root.getChildren().add(text);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case A :{
                    cameraXform.rx.setAngle(--cameraXAngle);
                    break;
                }
                case D :{
                    cameraXform.rx.setAngle(++cameraXAngle);
                    break;
                }
                case S :{
                    cameraXform.ry.setAngle(--cameraYAngle);
                    break;
                }
                case W :{
                    cameraXform.ry.setAngle(++cameraYAngle);
                    break;
                }
                case Z :{
                    cameraXform.rz.setAngle(--cameraZAngle);
                    break;
                }
                case X :{
                    cameraXform.rz.setAngle(++cameraZAngle);
                    break;
                }
            }
            text.setText(MessageFormat.format("\nx: {0}\ny: {1}\nz:{2}",cameraXAngle,cameraYAngle,cameraZAngle));
        });
//        rotateCameraAnimation();
    }

    private void rotateCameraAnimation(){
        Rotate rotate = new Rotate(0,0,0,0,Rotate.Y_AXIS);
        cameraXform.getTransforms().add(rotate);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), e -> {
                    cameraYAngle+=0.5;
                    rotate.setAngle(cameraYAngle);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void buildRubik(){
        Box test = new Box(10,10,10);
        world.getChildren().add(test);
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
        launch();
    }
}