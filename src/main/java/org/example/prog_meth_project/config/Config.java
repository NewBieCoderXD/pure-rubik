package org.example.prog_meth_project.config;

import javafx.scene.paint.Color;

public class Config {
    public static class Mirror{
        public static final double CUBELET_SMALLEST_WIDTH = 3;
        public static final double CUBELET_SMALLEST_HEIGHT = 1.4;
        public static final double CUBELET_DISTANCE = 1;
        public static final double CUBELET_GROWING_RATIO_VERTICAL= 2.6;
        public static final double CUBELET_GROWING_RATIO_HORIZONTAL= 1.5;
    }
    public static class Standard{
        public static final double CUBELET_LENGTH=5;
        public static final double CUBELET_DISTANCE = 1;
    }
    public static final double CUBELET_BORDER_WIDTH = 0.4;
    public static final Color CUBELET_MAIN_BOX_COLOR = Color.WHITE;
    public static final Color CUBELET_BORDER_COLOR = Color.BLACK;
    public static final double DRAG_SENSITIVITY = 0.1;
    public static final double RUBIK_MENU_RATIO = 0.05;
    public static final double SECOND_PER_NOTATION = 0.5;
    public static final double CAMERA_INITIAL_DISTANCE = -100;
    public static final double CAMERA_INITIAL_X_ANGLE = 45;
    public static final double CAMERA_INITIAL_Y_ANGLE = -180;
    public static final double CAMERA_INITIAL_Z_ANGLE = -45;
    public static final double CAMERA_NEAR_CLIP = 0.01;
    public static final double CAMERA_FAR_CLIP = 10000.0;
    public static final double AXIS_LENGTH = 250.0;
    public static final double INIT_SUBSCENE_WIDTH = 500;
    public static final double INIT_SUBSCENE_HEIGHT = 500;
}
