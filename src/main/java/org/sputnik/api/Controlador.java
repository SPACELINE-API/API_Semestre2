package org.sputnik.api;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.python.util.PythonInterpreter;


public class Controlador implements Initializable {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TabPane tabPane;

    @FXML
    private VBox telaInicialBox;

    @FXML
    private TextArea outPut;


    private FileChooser fileChooser = new FileChooser();
    private Map<Tab, File> arquivosAbertos = new HashMap<>();
    private Map<String, File> nomeParaArquivo = new HashMap<>(); // Para TreeView


    /*treeview*/
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

    /*Abrir arquivos*/
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
        TextArea editor = new TextArea();
        editor.getStyleClass().add("editor-texto");


        novaAba.setContent(editor);
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

    /*compilador*/

    @FXML
    void compilar() {
        System.out.println("Método compilar chamado");
        Tab abaSelecionada = tabPane.getSelectionModel().getSelectedItem();
        if (abaSelecionada == null) {
            outPut.setText("Nenhuma aba selecionada.");
            return;
        }

        TextArea editor = (TextArea) abaSelecionada.getContent();
        String codigo = editor.getText();

        if (codigo == null || codigo.isBlank()) {
            outPut.setText("Código vazio.");
            return;
        }

        outPut.setText("Executando...\n");

        new Thread(() -> {
            try {
                String resultado = executarPython(codigo);
                javafx.application.Platform.runLater(() -> outPut.setText(resultado));
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> outPut.setText("Erro ao executar: " + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    private String executarPython(String codigoPython) throws IOException, InterruptedException {

        File temp = File.createTempFile("codigo_temp", ".py");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {
            writer.write(codigoPython);
        }

        ProcessBuilder pb = new ProcessBuilder("python", temp.getAbsolutePath());
        pb.redirectErrorStream(true); // junta stdout e stderr
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder saida = new StringBuilder();
        String linha;
        while ((linha = reader.readLine()) != null) {
            saida.append(linha).append("\n");
        }

        int exitCode = process.waitFor();
        temp.delete();

        return "Código finalizado com código de saída " + exitCode + ":\n" + saida.toString();
    }




    /*IA*/
    @FXML
    void explicar() {
        Stage popup = new Stage();
        popup.setTitle("Explicação do Código");


        TextArea input = new TextArea();
        input.setPromptText("Digite seu código aqui...");
        input.setWrapText(true);
        input.getStyleClass().add("input-area");
        input.setPrefWidth(350);
        input.setPrefHeight(400);


        Button btnExplicar = new Button("Gerar Explicação");
        btnExplicar.getStyleClass().add("botao");


        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.getStyleClass().add("output-area");
        output.setPrefWidth(350);
        output.setPrefHeight(400);


        btnExplicar.setOnAction(e -> {
            String entrada = input.getText();
            output.setText("Aguarde...");
            new Thread(() -> {
                try {
                    String resposta = IA.getRespostaIA(entrada);
                    javafx.application.Platform.runLater(() -> output.setText(resposta));
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> output.setText("Erro ao tentar obter explicação: " + ex.getMessage()));
                    ex.printStackTrace();
                }
            }).start();
        });


        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 30;");
        Label entradaLabel = new Label("Entrada");
        entradaLabel.getStyleClass().add("entrada");
        Label saidaLabel = new Label("Saída");
        saidaLabel.getStyleClass().add("saida");

        layout.getChildren().addAll(entradaLabel, input, btnExplicar, saidaLabel, output);
        layout.getStyleClass().add("popup");

        Scene scene = new Scene(layout, 400, 450);
        scene.getStylesheets().add(getClass().getResource("/Css/principal.css").toExternalForm());
        popup.setScene(scene);

        if (tabPane != null && tabPane.getScene() != null) {
            popup.initOwner(tabPane.getScene().getWindow());
        }

        popup.show();
    }


    @FXML
    void traduzirLinguagem (javafx.event.ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String linguagem = (String) item.getUserData();
        traduzir(linguagem);
    }


    @FXML
    void traduzir(String linguagem) {
        Stage popup = new Stage();
        popup.setTitle("Tradução do código");


        TextArea input = new TextArea();
        input.setPromptText("Digite seu código aqui...");
        input.setWrapText(true);
        input.getStyleClass().add("input-area");
        input.setPrefWidth(350);
        input.setPrefHeight(400);


        Button btnTraduzir = new Button("Traduzir o código");
        btnTraduzir.getStyleClass().add("botao");


        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.getStyleClass().add("output-area");
        output.setPrefWidth(350);
        output.setPrefHeight(400);


        btnTraduzir.setOnAction(e -> {
            String entrada = input.getText();
            output.setText("Aguarde...");
            new Thread(() -> {
                try {
                    String resposta = IA.getTraducaoIA(entrada, linguagem);
                    javafx.application.Platform.runLater(() -> output.setText(resposta));
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> output.setText("Erro ao tentar obter tradução: " + ex.getMessage()));
                    ex.printStackTrace();
                }
            }).start();
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 30;");
        Label entradaLabel = new Label("Entrada");
        entradaLabel.getStyleClass().add("entrada");
        Label saidaLabel = new Label("Saída");
        saidaLabel.getStyleClass().add("saida");

        layout.getChildren().addAll(entradaLabel, input, btnTraduzir, saidaLabel, output);
        layout.getStyleClass().add("popup");

        Scene scene = new Scene(layout, 400, 450);
        scene.getStylesheets().add(getClass().getResource("/Css/principal.css").toExternalForm());
        popup.setScene(scene);

        if (tabPane != null && tabPane.getScene() != null) {
            popup.initOwner(tabPane.getScene().getWindow());
        }

        popup.show();
    }


    @FXML
    void ajuda() {
        Stage popup = new Stage();
        popup.setTitle("Ajuda");

        Rectangle2D tela = Screen.getPrimary().getVisualBounds();
        double largulaTela = tela.getWidth();
        Label label = new Label("Bem-vindo à Sputnik! \uD83D\uDC0D");
        label.getStyleClass().add("titulo");
        Text text = new Text("Sputnik é um ambiente de desenvolvimento integrado (IDE) criado especialmente para facilitar a escrita, execução e organização de seus projetos em Python. ");
        text.getStyleClass().add("conteudo");
        text.setWrappingWidth(largulaTela - 50);
        Label label2 = new Label("\uD83D\uDD27 O que você pode fazer aqui:");
        label2.getStyleClass().add("titulo");
        Text text2 = new Text("Criar, abrir e salvar arquivos .py\n" +
                "\n" +
                "Executar seu código Python diretamente na IDE\n" +
                "\n" +
                "Clonar repositórios do GitHub\n" +
                "\n" +
                "Salvar arquivos em .kps e .asm");
        text2.getStyleClass().add("conteudo");
        text2.setWrappingWidth(largulaTela - 50);
        Label label3 = new Label("\uD83D\uDCA1 Assistente de IA");
        label3.getStyleClass().add("titulo");
        Text text3 = new Text("A Sputnik utiliza inteligência artificial para auxiliar os desenvolvedores. Algumas de suas funcionalidades são:\n" +
                "\n" +
                "Sugestão de trechos de código\n" +
                "\n" +
                "Explicação do código\n" +
                "\n" +
                "Tradução de código Python para a linguagem Kotlin\n" +
                "\n" +
                "Tradução de código Python para a linguagem Assembly");
        text3.getStyleClass().add("conteudo");
        text3.setWrappingWidth(largulaTela - 50);
        Label label4 = new Label("\uD83D\uDCCC Dicas\n");
        label4.getStyleClass().add("subtitulo");
        Text text4 = new Text("Quanto mais contexto no código, melhores as sugestões\n" +
                "\n" +
                "Revise sempre as sugestões antes de confirmar\n" +
                "\n" +
                "Você pode editar ou ignorar qualquer sugestão");
        text4.getStyleClass().add("conteudo");
        text4.setWrappingWidth(largulaTela - 50);
        Label label5 = new Label("\uD83E\uDDE0 Limitações");
        label5.getStyleClass().add("subtitulo");
        Text text5 = new Text("A IA não garante que o código seja 100% correto ou otimizado\n" +
                "\n" +
                "Não substitui revisão humana nem testes manuais\n" +
                "\n");
        text5.getStyleClass().add("conteudo");
        text5.setWrappingWidth(largulaTela - 50);

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 30;");

        layout.getChildren().addAll(label, text, label2, text2, label3, text3, label4, text4, label5, text5);
        layout.getStyleClass().add("popup");

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 650, 550);
        scene.getStylesheets().add(getClass().getResource("/Css/principal.css").toExternalForm());
        popup.setScene(scene);

        if (tabPane != null && tabPane.getScene() != null) {
            popup.initOwner(tabPane.getScene().getWindow());
        }

        popup.show();
    }
}
