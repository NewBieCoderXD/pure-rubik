package org.example.prog_meth_project.application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.prog_meth_project.component.RubikMenu;
import org.example.prog_meth_project.page.StandardRubikPage;

import static org.example.prog_meth_project.config.Config.RUBIK_MENU_RATIO;

import com.tangorabox.componentinspector.fx.FXComponentInspectorHandler;
public class Main extends Application {
    public SubScene currentScene;
    public TabPane root = new TabPane();
    @Override
    public void start(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        StandardRubikPage.setBounds(bounds);
//        StandardRubikPage.setBounds(new Rectangle2D(bounds.getMinX(),bounds.getMinY(),bounds.getWidth()/2, bounds.getHeight()));
        currentScene=StandardRubikPage.getInstance().getScene();

        Tab mirrorTab = new Tab("Mirror", currentScene);

        root.getTabs().add(mirrorTab);

        stage.setMaximized(true);
        stage.setTitle("rubik simulator");
        stage.setScene(
            new Scene(
                root
            )
        );
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}