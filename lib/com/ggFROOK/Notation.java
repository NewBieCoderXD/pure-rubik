package com.ggFROOK;

public enum Notation {
    R(false),
    R_(true),
    L(false),
    L_(true),
    F(false),
    F_(true),
    B(false),
    B_(true),
    U(false),
    U_(true),
    D(false),
    D_(true);
    private final boolean isInverted;
    private Notation oppositeNotation;
    static {
        R.oppositeNotation=R_;
        R_.oppositeNotation=R;
        L.oppositeNotation=L_;
        L_.oppositeNotation=L;
        F.oppositeNotation=F_;
        F_.oppositeNotation=F;
        B.oppositeNotation=B_;
        B_.oppositeNotation=B;
        U.oppositeNotation=U_;
        U_.oppositeNotation=U;
        D.oppositeNotation=D_;
        D_.oppositeNotation=D;
    }
    public boolean isInverted(){
        return isInverted;
    }
    public boolean isOpposite(Notation rhs){
        return this.oppositeNotation.equals(rhs);
    }
    public Notation toggle(){
        return oppositeNotation;
    }
    Notation(boolean isInverted) {
        this.isInverted = isInverted;
    }
}
