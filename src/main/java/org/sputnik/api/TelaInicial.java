package org.sputnik.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javafx.scene.control.TextArea;

public class TelaInicial {
    public static VBox createTelaInicial(Stage primaryStage, BorderPane root) {
        Label title = new Label("Sputnik");
        title.getStyleClass().add("title");

        Button createProject = new Button("Explicação do código");
        Label openFile = new Label("Abra arquivo");
        Label openFolder = new Label("Abra pasta");
        Label cloneGit = new Label("Clone repositório Git");

        createProject.getStyleClass().add("meio");
        openFile.getStyleClass().add("meio");
        openFolder.getStyleClass().add("meio");
        cloneGit.getStyleClass().add("meio");

        openFile.setOnMouseClicked(e -> abrirArquivo(primaryStage, root));

        createProject.setOnAction(event -> openNewProjectWindow());

        VBox vbox = new VBox(10, title, createProject, openFile, openFolder, cloneGit);
        vbox.getStyleClass().add("inicio");

        return vbox;
    }

    private static void openNewProjectWindow() {
        Stage newStage = new Stage();
        newStage.setTitle("Novo Projeto");

        TextArea inputArea = new TextArea();
        inputArea.setPromptText("Digite seu texto aqui...");
        inputArea.setWrapText(true);

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPromptText("A saída será exibida aqui...");
        outputArea.setWrapText(true);

        Button explicação = new Button("Explicação");

        explicação.setOnAction(event -> {
            String inputText = inputArea.getText();
            try {
                System.out.println("Enviando para org.sputnik.api.IA: " + inputText);
                String respostaIA = IA.getRespostaIA(inputText); //Chamar a função "getRespostaIA" dentro do documento org.sputnik.api.IA.java
                System.out.println("Resposta recebida: " + respostaIA);
                outputArea.setText(respostaIA); //Colocar a resposta da org.sputnik.api.IA dentro da caixa de texto de output
            } catch (Exception e) {
                outputArea.setText("Erro ao processar a resposta da org.sputnik.api.IA. Verifique o console.");
                e.printStackTrace();
            }
        });

        VBox vbox = new VBox(10, inputArea, explicação, outputArea);


        Scene scene = new Scene(vbox, 400, 300);
        newStage.setScene(scene);

        newStage.show();
    }
    private static void abrirArquivo(Stage primaryStage, BorderPane root) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Arquivo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Arquivos de Texto", "*.txt"),
                new FileChooser.ExtensionFilter("Arquivos Java", "*.java")
        );
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                menuTopo.setCurrentFile(file);

                String content = new String(Files.readAllBytes(file.toPath()));
                root.setCenter(menuTopo.createContentDisplay(content));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
