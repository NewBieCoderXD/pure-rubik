package org.example.prog_meth_project;

public enum Notation {
    R(false),
    R_(true);

    public final boolean IsInverted;
    Notation(boolean IsInverted) {
        this.IsInverted=IsInverted;
    }

}
