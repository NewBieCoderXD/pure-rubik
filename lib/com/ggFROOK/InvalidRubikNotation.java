package com.ggFROOK;

public class InvalidRubikNotation extends Exception{
    public InvalidRubikNotation(String errorMessage){
        super(errorMessage);
    }
    public InvalidRubikNotation(){
        super();
    }
}