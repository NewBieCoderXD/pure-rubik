module org.example.prog_meth_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.web;
    requires transitive javafx.swing;


    requires org.kordamp.bootstrapfx.core;
    requires scenic.view;
    opens org.example.prog_meth_project.application to javafx.fxml;
    exports org.example.prog_meth_project.application;
}