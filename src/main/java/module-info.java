module org.example.prog_meth_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.prog_meth_project to javafx.fxml;
    exports org.example.prog_meth_project;
}