package jula.prog_meth_project.rubik_solver.page;

import com.ggFROOK.RubikFROOK;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jula.prog_meth_project.rubik_solver.application.NotationQueue;
import jula.prog_meth_project.rubik_solver.component.NotationStack;
import jula.prog_meth_project.rubik_solver.model.BaseRubik;
import jula.prog_meth_project.rubik_solver.model.PocketRubik;
import jula.prog_meth_project.rubik_solver.rendering.Xform;

import java.util.concurrent.atomic.AtomicBoolean;

public class PocketRubikPage implements RubikPage {

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
    private final BaseRubik rubik = new PocketRubik();
    private final NotationStack notationStack = new NotationStack();
    private final NotationQueue notationQueue = new NotationQueue(notationStack);
    private VBox scene = new VBox();
    public SubScene subScene3DView;
    public PocketRubikPage(){
        createScene();
    }
    @Override
    public String getName() {
        return "Pocket";
    }
    @Override
    public VBox getScene() {
        return scene;
    }

    @Override
    public AtomicBoolean getHasStartedSolving() {
        return hasStartedSolving;
    }

    @Override
    public NotationQueue getNotationQueue() {
        return notationQueue;
    }

    @Override
    public BaseRubik getRubik() {
        return rubik;
    }

    @Override
    public RubikFROOK getRubikSolver() {
        return rubikSolver;
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
    public Pane getRoot() {
        return root;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public Xform getCameraXform() {
        return cameraXform;
    }

    @Override
    public Group getAxisGroup() {
        return axisGroup;
    }

    @Override
    public NotationStack getNotationStack() {
        return notationStack;
    }

    @Override
    public double getStartDragX() {
        return startDragX;
    }

    @Override
    public double getStartDragY() {
        return startDragY;
    }

    @Override
    public void setStartDragX(double d) {
        this.startDragX=d;
    }

    @Override
    public void setStartDragY(double d) {
        this.startDragY=d;
    }

    @Override
    public SubScene getSubScene3DView() {
        return subScene3DView;
    }

    @Override
    public void setScene(VBox scene) {
        this.scene = scene;
    }

    @Override
    public void setSubScene3DView(SubScene subScene3DView) {
        this.subScene3DView=subScene3DView;
    }

}
