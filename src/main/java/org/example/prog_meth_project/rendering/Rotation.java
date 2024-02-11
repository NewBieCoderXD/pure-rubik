package org.example.prog_meth_project.rendering;

import javafx.beans.value.WritableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import org.example.prog_meth_project.Notation;
import org.example.prog_meth_project.model.Cubelet;

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
        public void onAngleChanges(Rotate rotate,double oldAngle,double newAngle);
    }
    private Rotate rotate;
    private double angle;
    private final ArrayList<RotateListener> listeners = new ArrayList<>();
    public Rotation(Cubelet cubelet, Notation notation){
        rotate=new Rotate();
        Point3D origin = cubelet.sceneToLocal(new Point3D(0,0,0));
        rotate.setPivotX(origin.getX());
        rotate.setPivotY(origin.getY());
        rotate.setPivotZ(origin.getZ());
        rotate.setAxis(notation.axis.toPoint3D());
        rotate.setAngle(0);
    }
    public void addListener(RotateListener listener){
        listeners.add(listener);
    }
    public void setAngle(double newAngle){
        for(RotateListener listener: listeners){
            listener.onAngleChanges(rotate,angle,newAngle);
        }
        angle=newAngle;
    }
}
