package org.sputnik.api;

import com.formdev.flatlaf.FlatDarkLaf;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
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
        UIManager.put("List.selectionBackground", new ColorUIResource(46,58,71));
        UIManager.put("List.selectionForeground", new ColorUIResource(238, 234, 117));
        UIManager.put("List.background", new ColorUIResource(3, 34, 48));
        UIManager.put("List.foreground", new ColorUIResource(243, 243, 243));
    }


    public static void main(String[] args) {
        launch();
    }
}