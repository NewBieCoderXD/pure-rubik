package org.example.prog_meth_project.page;

import com.ggFROOK.RubikFROOK;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.DepthTest;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.prog_meth_project.application.NotationQueue;
import org.example.prog_meth_project.application.RubikAnimationThread;
import org.example.prog_meth_project.component.NotationStack;
import org.example.prog_meth_project.config.Config;
import org.example.prog_meth_project.model.BaseRubik;
import org.example.prog_meth_project.model.MirrorRubik;
import org.example.prog_meth_project.rendering.Xform;

import java.util.concurrent.atomic.AtomicBoolean;

public class MirrorRubikPage implements RubikPage{
    final VBox root = new VBox();
    final Xform world = new Xform();
    final Xform axisGroup = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();

    volatile static MirrorRubik rubik;
    volatile static NotationQueue notationQueue = new NotationQueue();

    private double startDragX;
    private double startDragY;
    volatile static RubikFROOK rubikSolver = new RubikFROOK();
    volatile static AtomicBoolean hasStartedSolving = new AtomicBoolean(false);
    volatile static AtomicBoolean isSolving = new AtomicBoolean(false);
    volatile static NotationStack notationStack = NotationStack.getInstance();
    public static MirrorRubikPage instance;
    public static Rectangle2D bounds;
    public static void setBounds(Rectangle2D bounds){
        MirrorRubikPage.bounds = bounds;
    }
    public static void createInstance(Rectangle2D bounds){
        MirrorRubikPage.setBounds(bounds);
        instance = new MirrorRubikPage();
    }
    public static MirrorRubikPage getInstance(){
        if(instance==null){
            instance=new MirrorRubikPage();
        }
        return instance;
    }

    @Override
    public AtomicBoolean getHasStartedSolving() {
        return MirrorRubikPage.hasStartedSolving;
    }

    @Override
    public NotationQueue getNotationQueue() {
        return MirrorRubikPage.notationQueue;
    }

    @Override
    public MirrorRubik getRubik(){
        return rubik;
    }

    @Override
    public RubikFROOK getRubikSolver(){
        return rubikSolver;
    }

    @Override
    public Xform getWorld(){
        return world;
    }

    @Override
    public VBox getRoot() {
        return root;
    }

    @Override
    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override
    public Xform getCameraXform() {
        return cameraXform;
    }

    @Override
    public Xform getAxisGroup() {
        return axisGroup;
    }

    @Override
    public void setRubik(BaseRubik rubik){
        if(rubik instanceof MirrorRubik newRubik){
            MirrorRubikPage.rubik=newRubik;
        }
        else{
            throw new RuntimeException("rubik type is invalid");
        }
    }

    @Override
    public void initializeRubik(){
        setRubik(new MirrorRubik());
    }

    private MirrorRubikPage(){
        createScene();
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
    private SubScene scene;

    public SubScene getScene(){
        return scene;
    }

    public void createScene() {
        if(bounds==null){
            throw new RuntimeException("Bounds are not set");
        }
//        Rectangle2D bounds = screen.getVisualBounds();

        SubScene subScene=build3DSubScene(bounds.getWidth(), bounds.getHeight());

        root.setPrefWidth(bounds.getWidth());
        root.setPrefHeight(bounds.getHeight());

        scene = new SubScene(new StackPane(subScene, root), bounds.getWidth(), bounds.getHeight());

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

        Thread rubikAnimationThread = new RubikAnimationThread(notationQueue, isSolving, hasStartedSolving, rubikSolver, rubik);


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
                cameraXform.rz.setAngle(cameraXform.rz.getAngle() - xDistance * Config.DRAG_SENSITIVITY);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() - yDistance * Config.DRAG_SENSITIVITY);
                setAnglesText(anglesText);
            }
        });
        rubikAnimationThread.start();
    }


}
