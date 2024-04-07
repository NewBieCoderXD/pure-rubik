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

public class MirrorRubikPage
//        implements RubikPage
{

//    final VBox root = new VBox();
//    final Xform world = new Xform();
//    final Xform axisGroup = new Xform();
//    final PerspectiveCamera camera = new PerspectiveCamera(true);
//    final Xform cameraXform = new Xform();
//
//    volatile static MirrorRubik rubik;
//    volatile static NotationStack notationStack = new NotationStack();
//    volatile static NotationQueue notationQueue = new NotationQueue(notationStack);
//
//    private double startDragX;
//    private double startDragY;
//    volatile static RubikFROOK rubikSolver = new RubikFROOK();
//    volatile static AtomicBoolean hasStartedSolving = new AtomicBoolean(false);
//    volatile static AtomicBoolean isSolving = new AtomicBoolean(false);
//    public static MirrorRubikPage instance;
//    public static Rectangle2D bounds;
//    public static void setBounds(Rectangle2D bounds){
//        MirrorRubikPage.bounds = bounds;
//    }
//
//    @Override
//    public AtomicBoolean getIsSolving(){
//        return isSolving;
//    }
//
//    @Override
//    public AtomicBoolean getHasStartedSolving() {
//        return MirrorRubikPage.hasStartedSolving;
//    }
//
//    @Override
//    public NotationQueue getNotationQueue() {
//        return MirrorRubikPage.notationQueue;
//    }
//
//    @Override
//    public MirrorRubik getRubik(){
//        return rubik;
//    }
//
//    @Override
//    public RubikFROOK getRubikSolver(){
//        return rubikSolver;
//    }
//
//    @Override
//    public Xform getWorld(){
//        return world;
//    }
//
//    @Override
//    public VBox getRoot() {
//        return root;
//    }
//
//    @Override
//    public PerspectiveCamera getCamera() {
//        return camera;
//    }
//
//    @Override
//    public Xform getCameraXform() {
//        return cameraXform;
//    }
//
//    @Override
//    public Xform getAxisGroup() {
//        return axisGroup;
//    }
//    @Override
//    public NotationStack getNotationStack(){
//        return notationStack;
//    }
//
//    @Override
//    public double getStartDragX() {
//        return startDragX;
//    }
//
//    @Override
//    public double getStartDragY() {
//        return startDragY;
//    }
//
//    @Override
//    public void setStartDragX(double startDragX) {
//        this.startDragX = startDragX;
//    }
//
//    @Override
//    public void setStartDragY(double startDragY) {
//        this.startDragY = startDragY;
//    }
//
//    @Override
//    public void setRubik(BaseRubik rubik){
//        if(rubik instanceof MirrorRubik newRubik){
//            MirrorRubikPage.rubik=newRubik;
//        }
//        else{
//            throw new RuntimeException("rubik type is invalid");
//        }
//    }
//
//    @Override
//    public void initializeRubik(){
//        setRubik(new MirrorRubik());
//    }
//
//    private MirrorRubikPage(){
//        createScene();
//    }
//    private VBox scene;
//
//    @Override
//    public VBox getScene(){
//        return scene;
//    }
}
