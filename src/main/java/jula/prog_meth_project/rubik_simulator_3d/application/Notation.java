package jula.prog_meth_project.rubik_simulator_3d.application;

import jula.prog_meth_project.rubik_simulator_3d.rendering.Axis;

public enum Notation {
    R(false, Axis.X_AXIS,1),
    R_(true,Axis.X_AXIS,1),
    L(false,Axis.X_AXIS,-1),
    L_(true,Axis.X_AXIS,-1),
    F(false,Axis.Y_AXIS,-1),
    F_(true,Axis.Y_AXIS,-1),
    B(false,Axis.Y_AXIS,1),
    B_(true,Axis.Y_AXIS,1),
    U(false,Axis.Z_AXIS,-1),
    U_(true,Axis.Z_AXIS,-1),
    D(false,Axis.Z_AXIS,1),
    D_(true,Axis.Z_AXIS,1);

    public final boolean isInverted;
    public final Axis axis;
    public final int direction;
    Notation(boolean isInverted, Axis axis, int direction) {
        this.isInverted = isInverted;
        this.axis=axis;
        this.direction=direction;
    }
    public String toPrettyString(){
        if(isInverted){
            return this.toString().charAt(0)+"'";
        }
        return this.toString();
    }
    public static Notation stringToNotation(String notationString){
        if(notationString.length()==2){
            return Notation.valueOf(notationString.charAt(0)+"_");
        }
        return Notation.valueOf(notationString);
    }
    public int rotatingDirection(){
        return this.direction * (this.isInverted ? -1 : 1);
    }
}
