package org.example.prog_meth_project.application;

import com.ggFROOK.RubikFROOK;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.prog_meth_project.model.MirrorRubik;
import org.example.prog_meth_project.model.StandardRubik;
import org.example.prog_meth_project.page.StandardRubikPage;
import org.example.prog_meth_project.pane.NotationStack;
import org.example.prog_meth_project.rendering.Xform;

import java.text.MessageFormat;

import static org.example.prog_meth_project.config.Config.DRAG_SENSITIVITY;

public class Main extends Application {
    public Scene currentScene;

    @Override
    public void start(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        StandardRubikPage.setBounds(bounds);
        currentScene=StandardRubikPage.getInstance().getScene();

        stage.setMaximized(true);
        stage.setTitle("ISUS: rubik simulator");
        stage.setScene(currentScene);
        stage.show();

//        notationQueue.add(Notation.R);
//        notationQueue.add(Notation.R_);
//        notationQueue.add(Notation.L);
//        notationQueue.add(Notation.L_);
//        notationQueue.add(Notation.U);
//        notationQueue.add(Notation.U_);
//        notationQueue.add(Notation.D);
//        notationQueue.add(Notation.D_);
//        notationQueue.add(Notation.F);
//        notationQueue.add(Notation.F_);
//        notationQueue.add(Notation.B);
//        notationQueue.add(Notation.B_);

    }

    public static void main(String[] args) {
        launch(args);
    }
}