package org.sputnik.api;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class Lateral {

    public static SplitPane createLateral() {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.setDividerPositions(0.8);
        splitPane.setPrefWidth(320);

        VBox barraLateral = createBarraLateral();
        VBox chat = createChat();

        splitPane.getItems().addAll(barraLateral, chat);

        return splitPane;
    }

    public static VBox createChat() {
        VBox chatLayout = new VBox(10);
        chatLayout.setPadding(new Insets(0));
        chatLayout.getStyleClass().add("area-chat");

        Label titulo = new Label("Ask Ollama");
        titulo.getStyleClass().add("titulo-chat");

        TextArea chatTexto = new TextArea();
        chatTexto.setEditable(false);
        chatTexto.setWrapText(true);
        chatTexto.getStyleClass().add("display-chat");

        TextField chatmensagem = new TextField();
        chatmensagem.setPromptText("Digite sua mensagem...");
        chatmensagem.getStyleClass().add("mensagem-chat");

        ImageView enviarIcone = new ImageView(new Image("file:src/main/resources/Imagens/enviar-botão.png"));
        enviarIcone.setFitHeight(20);
        enviarIcone.setFitWidth(20);

        Button enviar = new Button("");
        enviar.setGraphic(enviarIcone);
        enviar.getStyleClass().add("enviar-chat");
        enviar.setOnAction(e -> {
            String mensagem = chatmensagem.getText();
            if (!mensagem.trim().isEmpty()) {
                chatTexto.appendText("Você: " + mensagem + "\n");
                chatmensagem.clear();
            }
        });

        HBox inputchat = new HBox(10, chatmensagem, enviar);
        inputchat.setPadding(new Insets(10));

        chatLayout.getChildren().addAll(titulo, chatTexto, inputchat);
        return chatLayout;
    }

    public static VBox createBarraLateral() {
        VBox barraLateral = new VBox();
        barraLateral.setPadding(new Insets(0));
        barraLateral.setSpacing(180);
        barraLateral.setStyle("-fx-background-color: #002b36;");
        barraLateral.setPrefWidth(180);

        ListView<String> fileExplorer = new ListView<>();
        fileExplorer.getItems().addAll(".txt");
        fileExplorer.setPrefWidth(180);
        fileExplorer.getStyleClass().add("explorador-arquivos");

        barraLateral.getChildren().add(fileExplorer);
        VBox.setVgrow(fileExplorer, Priority.ALWAYS);
        return barraLateral;
    }
}
