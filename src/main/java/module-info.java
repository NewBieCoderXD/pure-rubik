module jula.prog_meth_project.rubik_solver {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.web;
    requires transitive javafx.swing;

    requires org.kordamp.bootstrapfx.core;
    requires scenic.view;
    opens jula.prog_meth_project.rubik_simulator_3d.application to javafx.fxml;
    exports jula.prog_meth_project.rubik_simulator_3d.application;
    exports jula.prog_meth_project.rubik_simulator_3d.component;
    exports jula.prog_meth_project.rubik_simulator_3d.config;
    exports jula.prog_meth_project.rubik_simulator_3d.model;
    exports jula.prog_meth_project.rubik_simulator_3d.page;
    exports jula.prog_meth_project.rubik_simulator_3d.rendering;
    exports com.ggFROOK;
}