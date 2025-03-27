package org.sputnik.api;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TelaGeral extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sputnik");

        Font.loadFont(getClass().getResource("/fonts/Inter/Inter_18pt-Regular.ttf").toExternalForm(), 14);
        Font.loadFont(getClass().getResource("/fonts/Jura/Jura-Regular.ttf").toExternalForm(), 14);

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Sputnik");
        primaryStage.setScene(scene);
        primaryStage.show();

        root.setTop(menuTopo.createMenuBar(primaryStage, root));
        root.setLeft(Lateral.createLateral());
        root.setCenter(TelaInicial.createTelaInicial(primaryStage, root));


        scene.getStylesheets().add(getClass().getResource("/Css/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/Css/chat.css").toExternalForm());

    }

    public static void main(String[] args) {
        launch(args);
    }
}
