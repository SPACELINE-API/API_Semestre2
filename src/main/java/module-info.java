module org.sputnik.api {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires ollama4j;
    requires org.python.jython2;
    requires org.fife.RSyntaxTextArea;
    requires javafx.swing;

    opens org.sputnik.api to javafx.fxml;
    exports org.sputnik.api;
}
