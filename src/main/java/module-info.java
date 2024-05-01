module jula.prog_meth_project.rubik_solver {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.web;
    requires transitive javafx.swing;

    requires org.kordamp.bootstrapfx.core;
    requires scenic.view;
    opens jula.prog_meth_project.rubik_solver.application to javafx.fxml;
    exports jula.prog_meth_project.rubik_solver.application;
    exports jula.prog_meth_project.rubik_solver.component;
    exports jula.prog_meth_project.rubik_solver.config;
    exports jula.prog_meth_project.rubik_solver.model;
    exports jula.prog_meth_project.rubik_solver.page;
    exports jula.prog_meth_project.rubik_solver.rendering;
    exports com.ggFROOK;
}