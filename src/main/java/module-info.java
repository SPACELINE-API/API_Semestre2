module org.sputnik.api {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires ollama4j;
    requires javafx.graphics;

    opens org.sputnik.api to javafx.fxml;
    exports org.sputnik.api;
}