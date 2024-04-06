package org.example.prog_meth_project.application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.prog_meth_project.page.MirrorRubikPage;
import org.example.prog_meth_project.page.StandardRubikPage;
import javafx.stage.Window;
import org.scenicview.ScenicView;

public class Main extends Application {
    public SubScene currentScene;
    public TabPane root = new TabPane();
    @Override
    public void start(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        StandardRubikPage.setBounds(bounds);
        MirrorRubikPage.setBounds(bounds);

        VBox standardRubikPage = StandardRubikPage.getInstance().getScene();

        VBox stackPane = new VBox(standardRubikPage);
        Tab mirrorTab = new Tab("Standard", stackPane);
        Tab standardTab = new Tab("Mirror", MirrorRubikPage.getInstance().getScene());

        root.setTabMaxHeight(30);
        root.setTabMinHeight(30);
        StandardRubikPage.getInstance().subScene3DView.heightProperty().bind(root.heightProperty().subtract(root.getTabMaxHeight()).subtract(7));
        StandardRubikPage.getInstance().subScene3DView.widthProperty().bind(root.widthProperty());


        root.getTabs().addAll(mirrorTab,standardTab);
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