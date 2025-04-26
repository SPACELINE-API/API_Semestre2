package org.sputnik.api;

import com.formdev.flatlaf.FlatDarkLaf;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.util.Objects;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws UnsupportedLookAndFeelException {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scene-builder.fxml")));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Css/principal.css").toExternalForm());
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FlatDarkLaf.setup();
        UIManager.setLookAndFeel(new FlatDarkLaf());
    }


    public static void main(String[] args) {
        launch();
    }
}