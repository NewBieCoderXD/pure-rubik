package org.example.prog_meth_project.page;

import com.ggFROOK.RubikFROOK;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.prog_meth_project.application.NotationQueue;
import org.example.prog_meth_project.component.NotationStack;
import org.example.prog_meth_project.model.BaseRubik;
import org.example.prog_meth_project.model.MirrorRubik;
import org.example.prog_meth_project.model.StandardRubik;
import org.example.prog_meth_project.rendering.Xform;

import java.util.concurrent.atomic.AtomicBoolean;

public class MirrorRubikPage implements RubikPage{
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
    private final BaseRubik rubik = new MirrorRubik();
    private final NotationStack notationStack = new NotationStack();
    private final NotationQueue notationQueue = new NotationQueue(notationStack);
    private VBox scene = new VBox();
    public SubScene subScene3DView;
    public MirrorRubikPage(){
        createScene();
    }
    @Override
    public AtomicBoolean getIsSolving(){
        return isSolving;
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
    public BaseRubik getRubik(){
        return rubik;
    }

    @Override
    public RubikFROOK getRubikSolver(){
        return rubikSolver;
    }

    @Override
    public Group getWorld(){
        return world;
    }

    @Override
    public StackPane getRoot() {
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
    public Group getAxisGroup() {
        return axisGroup;
    }

    @Override
    public NotationStack getNotationStack(){
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

    @Override
    public void setStartDragX(double startDragX) {
        this.startDragX = startDragX;
    }

    @Override
    public void setStartDragY(double startDragY) {
        this.startDragY = startDragY;
    }
    @Override
    public VBox getScene(){
        return scene;
    }
}
