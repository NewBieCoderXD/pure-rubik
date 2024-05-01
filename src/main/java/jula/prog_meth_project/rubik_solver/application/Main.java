package jula.prog_meth_project.rubik_solver.application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jula.prog_meth_project.rubik_solver.page.MirrorRubikPage;
import jula.prog_meth_project.rubik_solver.page.PocketRubikPage;
import jula.prog_meth_project.rubik_solver.page.RubikPage;
import jula.prog_meth_project.rubik_solver.page.StandardRubikPage;

public class Main extends Application {
    public TabPane root = new TabPane();
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

        RubikPage[] pages = new RubikPage[]{
                new StandardRubikPage(),
                new MirrorRubikPage(),
                new PocketRubikPage(),
        };
        for (RubikPage page : pages) {
            Tab standardTab = new Tab(page.getName(), page.getScene());
            bindSubSceneSize(page);
            root.getTabs().add(standardTab);
        }

        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
//        root.getTabs().addAll(standardTab,mirrorTab);
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