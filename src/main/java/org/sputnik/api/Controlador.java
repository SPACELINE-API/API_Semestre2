package org.sputnik.api;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import java.io.*;
import java.net.URL;
import java.util.*;

public class Controlador implements Initializable {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TabPane tabPane;

    @FXML
    private VBox telaInicialBox;

    private FileChooser fileChooser = new FileChooser();
    private Map<Tab, File> arquivosAbertos = new HashMap<>();
    private Map<String, File> nomeParaArquivo = new HashMap<>(); // Para TreeView

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTreeView();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads"));

        tabPane.setVisible(false);
        tabPane.setMouseTransparent(true);

        tabPane.getTabs().addListener((javafx.collections.ListChangeListener<Tab>) change -> {
            if (tabPane.getTabs().isEmpty()) {
                telaInicialBox.setVisible(true);
                tabPane.setVisible(false);
                tabPane.setMouseTransparent(true);
            } else {
                telaInicialBox.setVisible(false);
                tabPane.setVisible(true);
                tabPane.setMouseTransparent(false);
            }
        });
    }

    private void configurarTreeView() {
        TreeItem<String> raiz = new TreeItem<>("Arquivos");
        treeView.getStyleClass().add("Lateral");
        treeView.setRoot(raiz);
        treeView.setShowRoot(true);
        raiz.setExpanded(true);

        treeView.setOnMouseClicked(event -> {
            TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
            if (item != null && nomeParaArquivo.containsKey(item.getValue())) {
                File arquivo = nomeParaArquivo.get(item.getValue());
                abrirArquivoDireto(arquivo);
            }
        });
    }

    private void adicionarArquivoNaTreeView(File arquivo) {
        TreeItem<String> raiz = treeView.getRoot();
        String nome = arquivo.getName();
        boolean jaExiste = raiz.getChildren().stream()
                .anyMatch(child -> child.getValue().equals(nome));
        if (!jaExiste) {
            TreeItem<String> novoItem = new TreeItem<>(nome);
            raiz.getChildren().add(novoItem);
            nomeParaArquivo.put(nome, arquivo);
        }
    }

    @FXML
    void abrirArquivo(ActionEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            abrirArquivoDireto(file);
        }
    }

    private void abrirArquivoDireto(File file) {
        for (Tab aba : tabPane.getTabs()) {
            if (file.equals(arquivosAbertos.get(aba))) {
                tabPane.getSelectionModel().select(aba);
                return;
            }
        }

        Tab novaAba = new Tab(file.getName());
        CodeArea editor = new CodeArea();
        editor.getStyleClass().add("editor-python");
        novaAba.setContent(editor);
        SyntaxHighlighter.applyHighlighting(editor);

        tabPane.getTabs().add(novaAba);
        tabPane.getSelectionModel().select(novaAba);
        arquivosAbertos.put(novaAba, file);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                editor.appendText(scanner.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        adicionarArquivoNaTreeView(file);
    }

    @FXML
    void novoArquivo() {
        Tab novaAba = new Tab("Novo Arquivo");
        TextArea editor = new TextArea();
        editor.setPromptText("// Escreva aqui...");
        editor.getStyleClass().add("editor-texto");
        novaAba.setContent(editor);

        tabPane.getTabs().add(novaAba);
        tabPane.getSelectionModel().select(novaAba);

        arquivosAbertos.put(novaAba, null);
    }

    @FXML
    void salvarArquivo() {
        Tab abaSelecionada = tabPane.getSelectionModel().getSelectedItem();
        if (abaSelecionada != null) {
            TextArea editor = (TextArea) abaSelecionada.getContent();
            File arquivo = arquivosAbertos.get(abaSelecionada);

            if (arquivo == null) {
                arquivo = fileChooser.showSaveDialog(new Stage());
                if (arquivo != null) {
                    arquivosAbertos.put(abaSelecionada, arquivo);
                    abaSelecionada.setText(arquivo.getName());
                    adicionarArquivoNaTreeView(arquivo);
                } else {
                    return;
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
                bw.write(editor.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
