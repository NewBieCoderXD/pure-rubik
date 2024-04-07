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
import org.example.prog_meth_project.model.BaseRubik;
import org.example.prog_meth_project.model.StandardRubik;
import org.example.prog_meth_project.rendering.Xform;

import java.text.MessageFormat;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.prog_meth_project.config.Config.DRAG_SENSITIVITY;

public class StandardRubikPage implements RubikPage{
    final StackPane root = new StackPane();
    final Xform world = new Xform();
    final Xform axisGroup = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();

    private double startDragX;
    private double startDragY;
    volatile RubikFROOK rubikSolver = new RubikFROOK();
    volatile AtomicBoolean hasStartedSolving = new AtomicBoolean(false);
    volatile AtomicBoolean isSolving = new AtomicBoolean(false);
    volatile BaseRubik rubik = new StandardRubik();
    private NotationStack notationStack = new NotationStack();
    private NotationQueue notationQueue = new NotationQueue(notationStack);

    @Override
    public NotationStack getNotationStack() {
        return notationStack;
    }

    @Override
    public NotationQueue getNotationQueue() {
        return notationQueue;
    }

    public StandardRubikPage(){
        createScene();
    }

    @Override
    public double getStartDragX() {
        return startDragX;
    }

    @Override
    public AtomicBoolean getIsSolving() {
        return isSolving;
    }

    @Override
    public Xform getWorld() {
        return world;
    }

    @Override
    public Xform getAxisGroup() {
        return axisGroup;
    }

    @Override
    public RubikFROOK getRubikSolver() {
        return rubikSolver;
    }

    @Override
    public Xform getCameraXform() {
        return cameraXform;
    }

    @Override
    public StackPane getRoot() {
        return root;
    }

    @Override
    public double getStartDragY() {
        return startDragY;
    }

    @Override
    public AtomicBoolean getHasStartedSolving() {
        return hasStartedSolving;
    }

    @Override
    public SubScene getSubScene3DView() {
        return subScene3DView;
    }

    @Override
    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override
    public BaseRubik getRubik() {
        return rubik;
    }

    @Override
    public void setRubik(BaseRubik rubik) {
        this.rubik=rubik;
    }

    @Override
    public void setScene(VBox scene) {
        this.scene = scene;
    }

    @Override
    public void setStartDragY(double startDragY) {
        this.startDragY = startDragY;
    }

    @Override
    public void setStartDragX(double startDragX) {
        this.startDragX = startDragX;
    }

    @Override
    public void setSubScene3DView(SubScene subScene3DView) {
        this.subScene3DView = subScene3DView;
    }

    @Override
    public void initializeRubik(){

    }

    private VBox scene = new VBox();

    public VBox getScene(){
        return scene;
    }

    public SubScene subScene3DView;

//    public void createScene() {
//        subScene3DView=build3DSubScene(500,500);
//
//        scene = new VBox(new StackPane(subScene3DView, root));
//
//        Text anglesText = buildAnglesText();
//
//        GridPane notationMenu = buildMenus();
//
//        StackPane.setAlignment(anglesText,Pos.TOP_LEFT);
//        root.getChildren().add(anglesText);
//
//        root.getChildren().add(notationStack);
//
//        StackPane.setAlignment(notationMenu,Pos.BOTTOM_CENTER);
//        root.getChildren().add(notationMenu);
//
////        notationQueue.add(Notation.R);
////        notationQueue.add(Notation.R_);
////        notationQueue.add(Notation.L);
////        notationQueue.add(Notation.L_);
////        notationQueue.add(Notation.U);
////        notationQueue.add(Notation.U_);
////        notationQueue.add(Notation.D);
////        notationQueue.add(Notation.D_);
////        notationQueue.add(Notation.F);
////        notationQueue.add(Notation.F_);
////        notationQueue.add(Notation.B);
////        notationQueue.add(Notation.B_);
//
//        Thread rubikAnimationThread = new RubikAnimationThread(notationQueue, isSolving, startSolving, rubikFROOK, rubik);
//
//
//        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                startDragX = event.getSceneX();
//                startDragY = event.getSceneY();
//            }
//        });
//        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                double xDistance = event.getSceneX() - startDragX;
//                double yDistance = event.getSceneY() - startDragY;
//                startDragX = event.getSceneX();
//                startDragY = event.getSceneY();
//                cameraXform.rz.setAngle(cameraXform.rz.getAngle() - xDistance * DRAG_SENSITIVITY);
//                cameraXform.rx.setAngle(cameraXform.rx.getAngle() - yDistance * DRAG_SENSITIVITY);
//                setAnglesText(anglesText);
//            }
//        });
//        rubikAnimationThread.start();
//    }
}
