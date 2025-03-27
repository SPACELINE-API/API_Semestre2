package org.sputnik.api;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


public class menuTopo {

    private static File currentFile;

    public static void setCurrentFile(File file) {
        currentFile = file;
    }

    public static MenuBar createMenuBar(Stage primaryStage, BorderPane root) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Arquivo");
        MenuItem newFile = new MenuItem("Novo");
        MenuItem openFile = new MenuItem("Abrir");
        MenuItem saveFile = new MenuItem("Salvar");

        fileMenu.getItems().addAll(newFile, openFile, saveFile);

        Menu editMenu = new Menu("Editar");
        MenuItem undo = new MenuItem("Desfazer");
        MenuItem redo = new MenuItem("Refazer");
        MenuItem delete = new MenuItem("Deletar");

        editMenu.getItems().addAll(undo, redo, delete);

        Menu helpMenu = new Menu("Ajuda");
        MenuItem about = new MenuItem("Sobre");

        helpMenu.getItems().addAll(about);
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        openFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir Arquivo");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Arquivos de Texto", "*.txt"),
                    new FileChooser.ExtensionFilter("Arquivos Java", "*.java")
            );

            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {

                    String content = new String(Files.readAllBytes(file.toPath()));
                    currentFile = file;
                    root.setCenter(createContentDisplay(content));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        saveFile.setOnAction(e -> {
            if (currentFile != null) {
                try {
                    TextArea contentArea = (TextArea) root.getCenter();
                    String updatedContent = contentArea.getText();

                    Files.write(
                            currentFile.toPath(),
                            updatedContent.getBytes(),
                            StandardOpenOption.TRUNCATE_EXISTING
                    );
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso");
                alert.setHeaderText("Nenhum arquivo aberto");
                alert.setContentText("Favor abrir ou criar um arquivo antes de salvar.");
                alert.showAndWait();
            }
        });

        return menuBar;
    }
    public static TextArea createContentDisplay(String content) {
        TextArea contentArea = new TextArea(content);
        contentArea.setEditable(true); // Agora o texto é editável
        contentArea.getStyleClass().add("editor-texto"); // Aplicar estilo definido no CSS
        contentArea.setWrapText(true); // Garantir quebra automática de linha
        return contentArea;

    }

}
