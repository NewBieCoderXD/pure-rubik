package jula.prog_meth_project.rubik_solver.rendering;

import javafx.beans.value.WritableValue;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import jula.prog_meth_project.rubik_solver.application.Notation;
import jula.prog_meth_project.rubik_solver.model.Cubelet;

import java.util.ArrayList;

public class Rotation implements WritableValue<Double> {

    @Override
    public Double getValue() {
        return angle;
    }

    @Override
    public void setValue(Double t) {
        setAngle(t);
    }

    public interface RotateListener{
        public void onAngleChanges(Cubelet cubelet, Rotate rotate, double oldAngle, double newAngle);
    }
    private final Rotate rotate;
    private double angle=0;
    private final Cubelet cubelet;
    private final ArrayList<RotateListener> listeners = new ArrayList<>();
    public Rotation(Cubelet cubelet, Notation notation){
        rotate=new Rotate(0);
        rotate.setPivotX(-cubelet.getTranslateX());
        rotate.setPivotY(-cubelet.getTranslateY());
        rotate.setPivotZ(-cubelet.getTranslateZ());
        rotate.setAxis(notation.axis.toPoint3D());
        this.cubelet=cubelet;
        this.addListener(new Rotation.RotateListener(){
            @Override
            public void onAngleChanges(Cubelet cubelet, Rotate rotate,double oldAngle,double newAngle){
                rotate.setAngle(newAngle-oldAngle);
                Affine affine = cubelet.getAffine();
                affine.prepend(rotate);
            }
        });
    }
    public void addListener(RotateListener listener){
        listeners.add(listener);
    }
    public void setAngle(double newAngle){
        for(RotateListener listener: listeners){
            listener.onAngleChanges(cubelet,rotate,angle,newAngle);
        }
        this.angle=newAngle;
    }
}
