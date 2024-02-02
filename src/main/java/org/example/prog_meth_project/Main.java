package org.example.prog_meth_project;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.prog_meth_project.model.Cubelet;
import org.example.prog_meth_project.rendering.Xform;

import java.io.IOException;
import java.text.MessageFormat;

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
    private static final double CAMERA_INITIAL_DISTANCE = -100;
    private static final double CAMERA_INITIAL_X_ANGLE = -52;
    private static final double CAMERA_INITIAL_Y_ANGLE = -184;
    private static final double CAMERA_INITIAL_Z_ANGLE = 138;
    private static final double CAMERA_NEAR_CLIP = 0.01;
    private static final double CAMERA_FAR_CLIP = 10000.0;

    private double startDragX;
    private double startDragY;

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

//    private void rotateCameraAnimation(){
//        Rotate rotate = new Rotate(0,0,0,0,Rotate.Y_AXIS);
//        cameraXform.getTransforms().add(rotate);
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.seconds(0.01), e -> {
//                    cameraYAngle+=0.5;
//                    rotate.setAngle(cameraYAngle);
//                })
//        );
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
//    }

    private void buildRubik() {
//        Box test = new Box(10,10,10);
        for(int z=-1;z<=1;z++){
            for(int y=-1;y<=1;y++) {
                for (int x = -1; x <= 1; x++) {
                    double xLength = CUBELET_SMALLEST_WIDTH * Math.pow(CUBELET_GROWING_RATIO_HORIZONTAL, x + 1);
                    double yLength = CUBELET_SMALLEST_WIDTH * Math.pow(CUBELET_GROWING_RATIO_HORIZONTAL, y + 1);
                    double zLength = CUBELET_SMALLEST_HEIGHT * Math.pow(CUBELET_GROWING_RATIO_VERTICAL, z + 1);
                    Cubelet cubelet = new Cubelet(xLength, yLength, zLength);
                    cubelet.setTranslateX((CUBELET_SMALLEST_WIDTH * CUBELET_GROWING_RATIO_HORIZONTAL + xLength / 2 + CUBELET_DISTANCE) * -x);
                    cubelet.setTranslateY((CUBELET_SMALLEST_WIDTH * CUBELET_GROWING_RATIO_HORIZONTAL + yLength / 2 + CUBELET_DISTANCE) * -y);
                    cubelet.setTranslateZ((CUBELET_SMALLEST_HEIGHT * CUBELET_GROWING_RATIO_VERTICAL + zLength / 2 + CUBELET_DISTANCE) * -z);
                    world.getChildren().add(cubelet);
                }
            }
        }
//        Cubelet test = new Cubelet(10, 10, 10);
//        world.getChildren().add(test);
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