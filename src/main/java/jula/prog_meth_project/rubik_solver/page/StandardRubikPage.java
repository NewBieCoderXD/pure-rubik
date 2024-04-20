package jula.prog_meth_project.rubik_solver.page;

import com.ggFROOK.RubikFROOK;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jula.prog_meth_project.rubik_solver.application.NotationQueue;
import jula.prog_meth_project.rubik_solver.component.NotationStack;
import jula.prog_meth_project.rubik_solver.model.BaseRubik;
import jula.prog_meth_project.rubik_solver.rendering.Xform;
import jula.prog_meth_project.rubik_solver.model.StandardRubik;

import java.util.concurrent.atomic.AtomicBoolean;

public class StandardRubikPage implements RubikPage{
    private final StackPane root = new StackPane();
    private final Group world = new Group();
    private final Group axisGroup = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Xform cameraXform = new Xform();

    private double startDragX;
    private double startDragY;
    private final RubikFROOK rubikSolver = new RubikFROOK();
    private final AtomicBoolean hasStartedSolving = new AtomicBoolean(false);
    private final AtomicBoolean isSolving = new AtomicBoolean(false);
    private final BaseRubik rubik = new StandardRubik();
    private final NotationStack notationStack = new NotationStack();
    private final NotationQueue notationQueue = new NotationQueue(notationStack);

    private VBox scene = new VBox();
    public SubScene subScene3DView;
    public StandardRubikPage(){
        createScene();
    }

    @Override
    public String getName() {
        return "Standard";
    }

    @Override
    public NotationStack getNotationStack() {
        return notationStack;
    }

    @Override
    public NotationQueue getNotationQueue() {
        return notationQueue;
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
    public Group getWorld() {
        return world;
    }

    @Override
    public Group getAxisGroup() {
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

    public VBox getScene(){
        return scene;
    }

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
