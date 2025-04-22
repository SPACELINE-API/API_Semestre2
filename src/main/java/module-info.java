module org.sputnik.api {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires ollama4j;
    requires java.desktop;
    requires org.python.jython2;

    opens org.sputnik.api to javafx.fxml;
    exports org.sputnik.api;
}
