package org.example.prog_meth_project.application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.prog_meth_project.page.StandardRubikPage;
import org.scenicview.ScenicView;

public class Main extends Application {
    public SubScene currentScene;
    public TabPane root = new TabPane();
    public StandardRubikPage standardRubikPage = new StandardRubikPage();
    @Override
    public void start(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        VBox standardRubikPageScene = standardRubikPage.getScene();
//        VBox mirrorRubikPage = new MirrorRubikPage().getScene();

        Tab standardTab = new Tab("Standard", standardRubikPageScene);
//        Tab mirrorTab = new Tab("Mirror", MirrorRubikPage.getScene());

        root.setTabMaxHeight(35);
        root.setTabMinHeight(35);
        standardRubikPage.subScene3DView.heightProperty().bind(root.heightProperty().subtract(root.getTabMaxHeight()).subtract(7));
        standardRubikPage.subScene3DView.widthProperty().bind(root.widthProperty());

        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        root.getTabs().addAll(standardTab);
        root.setPrefSize(bounds.getWidth(),bounds.getHeight());

        Scene scene = new Scene(new VBox(root));
        stage.setMaximized(true);
        stage.setTitle("rubik simulator");
        stage.setScene(scene);
        stage.show();
        ScenicView.show(scene);
//        System.out.println(stage.getHeight()+" "+test.getHeight()+" "+subScene.getHeight());
//        System.out.println(stage.getWidth()+" "+test.getWidth()+" "+subScene.getWidth());

    }

    public static void main(String[] args) {
        launch(args);
    }
}