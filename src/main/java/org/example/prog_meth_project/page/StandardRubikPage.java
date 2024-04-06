package org.example.prog_meth_project.page;

import com.ggFROOK.RubikFROOK;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.DepthTest;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import org.example.prog_meth_project.application.Notation;
import org.example.prog_meth_project.application.NotationQueue;
import org.example.prog_meth_project.application.RubikAnimationThread;
import org.example.prog_meth_project.component.NotationStack;
import org.example.prog_meth_project.model.StandardRubik;
import org.example.prog_meth_project.rendering.Xform;

import java.text.MessageFormat;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.prog_meth_project.config.Config.DRAG_SENSITIVITY;

public class StandardRubikPage {
    final StackPane root = new StackPane();
    final Xform world = new Xform();
    private static final double AXIS_LENGTH = 250.0;
    final Xform axisGroup = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();

    volatile static StandardRubik rubik;
    //    volatile static Queue<Notation> notationQueue = new LinkedList<>();
    volatile static NotationStack notationStack = new NotationStack();
    volatile static NotationQueue notationQueue = new NotationQueue(notationStack);
    private static final double CAMERA_INITIAL_DISTANCE = -100;
    private static final double CAMERA_INITIAL_X_ANGLE = 45;
    private static final double CAMERA_INITIAL_Y_ANGLE = -180;
    private static final double CAMERA_INITIAL_Z_ANGLE = -45;
    private static final double CAMERA_NEAR_CLIP = 0.01;
    private static final double CAMERA_FAR_CLIP = 10000.0;

    private double startDragX;
    private double startDragY;
    volatile static RubikFROOK rubikFROOK = new RubikFROOK();
    volatile static AtomicBoolean startSolving = new AtomicBoolean(false);
    volatile static AtomicBoolean isSolving = new AtomicBoolean(false);
    public static StandardRubikPage instance;
    public static Rectangle2D bounds;
    public static void setBounds(Rectangle2D bounds){
        StandardRubikPage.bounds = bounds;
    }
    public static StandardRubikPage getInstance(){
        if(instance==null){
            instance=new StandardRubikPage();
        }
        return instance;
    }

    private StandardRubikPage(){
        createScene();
    }

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
    private VBox scene;

    public VBox getScene(){
        return scene;
    }

    public SubScene subScene3DView;

    public void createScene() {
        if(bounds==null){
            throw new RuntimeException("Bounds are not set");
        }
        subScene3DView=build3DSubScene(500,500);

        scene = new VBox(new StackPane(subScene3DView, root));

        Text anglesText = buildAnglesText();

        GridPane notationMenu = buildMenus();

        StackPane.setAlignment(anglesText,Pos.TOP_LEFT);
        root.getChildren().add(anglesText);

        StackPane.setAlignment(notationStack,Pos.CENTER_LEFT);
        root.getChildren().add(notationStack);

        StackPane.setAlignment(notationMenu,Pos.BOTTOM_CENTER);
        root.getChildren().add(notationMenu);

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

        Thread rubikAnimationThread = new RubikAnimationThread(notationQueue, isSolving, startSolving, rubikFROOK, rubik);


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
                startSolving.set(true);
            }
        });
        return button;
    }

    public GridPane buildMenus(){
        GridPane menu = new GridPane();
        menu.setAlignment(Pos.BOTTOM_CENTER);
        menu.setPrefWidth(root.getPrefWidth());
        Button solveButton = buildSolveButton();
        menu.add(solveButton,0,0,1,2);
        buildMenu(menu);
        return menu;
    }

    private void buildMenu(GridPane menu){
        for(int i = 0; i< Notation.values().length; i++) {
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
        rubik = new StandardRubik();
        rubikFROOK.initRubik();
        world.getChildren().add(rubik);
    }

    private void buildCamera() {
        cameraXform.getChildren().add(camera);

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
}
