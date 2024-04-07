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
import org.example.prog_meth_project.page.MirrorRubikPage;
import org.example.prog_meth_project.page.RubikPage;
import org.example.prog_meth_project.page.StandardRubikPage;
import org.scenicview.ScenicView;

public class Main extends Application {
    public SubScene currentScene;
    public TabPane root = new TabPane();
    public StandardRubikPage standardRubikPage = new StandardRubikPage();
    public MirrorRubikPage mirrorRubikPage = new MirrorRubikPage();
    public void bindSubSceneSize(RubikPage node){
        node.getSubScene3DView().heightProperty().bind(root.heightProperty().subtract(root.getTabMaxHeight()).subtract(7));
        node.getSubScene3DView().widthProperty().bind(root.widthProperty());
    }
    @Override
    public void start(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        root.setTabMaxHeight(35);
        root.setTabMinHeight(35);

        Tab standardTab = new Tab("Standard", standardRubikPage.getScene());
        bindSubSceneSize(standardRubikPage);

        Tab mirrorTab = new Tab("Mirror", mirrorRubikPage.getScene());
        bindSubSceneSize(mirrorRubikPage);

        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        root.getTabs().addAll(standardTab,mirrorTab);
        root.setPrefSize(bounds.getWidth(),bounds.getHeight());

        Scene scene = new Scene(new VBox(root));
        stage.setMaximized(true);
        stage.setTitle("rubik simulator");
        stage.setScene(scene);
        stage.show();
//        ScenicView.show(scene);

    }

    public static void main(String[] args) {
        launch(args);
    }
}