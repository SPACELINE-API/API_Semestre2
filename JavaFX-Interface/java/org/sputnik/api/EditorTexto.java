package org.sputnik.api;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class EditorTexto {
    public static SplitPane createEditorTexto() {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);

        StackPane editorPane = new StackPane();
        TextArea codeArea = new TextArea();
        codeArea.setPromptText("// Escreva seu código aqui...");
        codeArea.getStyleClass().add("editor-texto");
        editorPane.getChildren().add(codeArea);

        VBox outputPane = new VBox(5);
        outputPane.setPadding(new Insets(10));
        outputPane.getStyleClass().add("output-pane");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.getStyleClass().add("output-area");

        Button toggleOutput = new Button("Mostrar/Ocultar Output");
        toggleOutput.setOnAction(e -> {
            if (splitPane.getItems().contains(outputPane)) {
                splitPane.getItems().remove(outputPane);
            } else {
                splitPane.getItems().add(outputPane);
            }
        });

        outputPane.getChildren().addAll(new Label("Saída do Código:"), outputArea);
        splitPane.getItems().addAll(editorPane, outputPane);

        return splitPane;
    }
}
