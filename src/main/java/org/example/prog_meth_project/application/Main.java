package org.example.prog_meth_project.application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.prog_meth_project.page.MirrorRubikPage;
import org.example.prog_meth_project.page.StandardRubikPage;
public class Main extends Application {
    public SubScene currentScene;
    public TabPane root = new TabPane();
    @Override
    public void start(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        StandardRubikPage.setBounds(bounds);
        MirrorRubikPage.setBounds(bounds);
//        StandardRubikPage.setBounds(new Rectangle2D(bounds.getMinX(),bounds.getMinY(),bounds.getWidth()/2, bounds.getHeight()));
        ;

        Tab mirrorTab = new Tab("Standard", StandardRubikPage.getInstance().getScene());
        Tab standardTab = new Tab("Mirror", MirrorRubikPage.getInstance().getScene());

//        StandardRubikPage.getInstance().getScene().heightProperty().bind(mirrorTab.getContextMenu().heightProperty());

        root.getTabs().addAll(mirrorTab,standardTab);

        Scene rootScene = new Scene(root);

        stage.setMaximized(true);
        stage.setTitle("rubik simulator");
        stage.setScene(rootScene);
//        rootScene.heightProperty().
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}