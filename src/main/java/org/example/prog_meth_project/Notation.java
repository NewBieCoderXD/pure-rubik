package org.example.prog_meth_project;

import org.example.prog_meth_project.rendering.Axis;

public enum Notation {
    R(false,Axis.X_AXIS,1),
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

    public final boolean IsInverted;
    public final Axis axis;
    public final int direction;
    Notation(boolean IsInverted, Axis axis, int direction) {
        this.IsInverted=IsInverted;
        this.axis=axis;
        this.direction=direction;
    }

}
