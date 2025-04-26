package org.sputnik.api;

import com.formdev.flatlaf.FlatDarkLaf;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import javax.swing.*;


public class Controlador implements Initializable {

    private double xOffset=0;
    private double yOffset=0;

    @FXML
    private TreeView<String> treeView;
    private final Map<TreeItem<String>, Tab> itemParaAba = new HashMap<>();
    private final Map<String, File> nomeParaArquivo = new HashMap<>();
    private FileChooser fileChooser = new FileChooser();
    private Map<Tab, File> arquivosAbertos = new HashMap<>();

    @FXML
    private TabPane tabPane;

    @FXML
    private VBox telaInicialBox;

    @FXML
    private TextArea outPut;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnMinimize;

    @FXML
    private Button btnTab;

    @FXML
    private AnchorPane topPane;

    /*barra de título*/
    @FXML
    private void  fechar(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();

        stage.close();
    }

    @FXML
    private void diminuir(ActionEvent event) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();

        stage.setIconified(true);
    }

    @FXML
    private void fullscreen(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    private void click(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();

        xOffset=stage .getX() - event.getScreenX();
        yOffset=stage.getY() - event.getScreenY();
    }

    @FXML
    private void movimento(MouseEvent event) {
        Stage stage = (Stage) btnTab.getScene().getWindow();

        stage.setX(event.getScreenX() + xOffset );
        stage.setY(event.getScreenY() + yOffset);
    }

    /*treeview*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTreeView();

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
            if (item != null && item.getParent() != null && nomeParaArquivo.containsKey(item.getValue())) {
                File arquivo = nomeParaArquivo.get(item.getValue());
                abrirArquivoDireto(arquivo);
            }
        });
    }

    private void adicionarArquivoNaTreeView(File arquivo) {
        if (arquivo == null) return;

        TreeItem<String> raiz = treeView.getRoot();
        String nome = arquivo.getName();
        nomeParaArquivo.put(nome, arquivo);

        boolean jaExiste = raiz.getChildren().stream()
                .anyMatch(child -> child.getValue().equals(nome));

        if (!jaExiste) {
            TreeItem<String> novoItem = new TreeItem<>(nome);
            raiz.getChildren().add(novoItem);
            itemParaAba.put(novoItem, tabPane.getSelectionModel().getSelectedItem());
        }
    }

    /*Abrir arquivos*/

    @FXML
    private void abrirArquivoDireto(File file) {
        for (Tab aba : tabPane.getTabs()) {
            if (file.equals(arquivosAbertos.get(aba))) {
                tabPane.getSelectionModel().select(aba);
                return;
            }
        }
        if (file != null && file.exists()) {
            try {
                String conteudo = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                String linguagem = detectarLinguagem(file);

                SwingNode swingNode = new SwingNode();
                JComponent editorComScroll = EditorRSyntaxFactory.criarEditor(conteudo, linguagem);
                SwingUtilities.invokeLater(() -> swingNode.setContent(editorComScroll));

                StackPane conteudoAba = new StackPane(swingNode);
                Tab novaAba = new Tab(file.getName());
                novaAba.setContent(conteudoAba);
                tabPane.getTabs().add(novaAba);
                tabPane.getSelectionModel().select(novaAba);

                arquivosAbertos.put(novaAba, file);
                adicionarArquivoNaTreeView(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void novoArquivo() {
        SwingNode swingNode = new SwingNode();
        JComponent editorComScroll = EditorRSyntaxFactory.criarEditor("", "python");
        SwingUtilities.invokeLater(() -> swingNode.setContent(editorComScroll));
        StackPane conteudoAba = new StackPane(swingNode);

        Tab novaAba = new Tab("Novo Arquivo");
        novaAba.setContent(conteudoAba);
        tabPane.getTabs().add(novaAba);
        tabPane.getSelectionModel().select(novaAba);
        arquivosAbertos.put(novaAba, null);
    }

    @FXML
    private void abrirArquivo(ActionEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                String conteudo = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                String linguagem = detectarLinguagem(file);

                SwingNode swingNode = new SwingNode();
                JComponent editorComScroll = EditorRSyntaxFactory.criarEditor(conteudo, linguagem);
                SwingUtilities.invokeLater(() -> swingNode.setContent(editorComScroll));

                StackPane conteudoAba = new StackPane(swingNode);

                Tab novaAba = new Tab(file.getName());
                novaAba.setContent(conteudoAba);
                tabPane.getTabs().add(novaAba);
                tabPane.getSelectionModel().select(novaAba);

                arquivosAbertos.put(novaAba, file);

                adicionarArquivoNaTreeView(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String detectarLinguagem(File file) {
        String nome = file.getName().toLowerCase();
        if (nome.endsWith(".py")) return "python";
        if (nome.endsWith(".java")) return "java";
        if (nome.endsWith(".js")) return "javascript";
        if (nome.endsWith(".html")) return "html";
        if (nome.endsWith(".c")) return "c";
        if (nome.endsWith(".cpp")) return "cpp";
        if (nome.endsWith(".txt")) return "text";
        return "text";
    }

    @FXML
    void salvarArquivo() {
        Tab abaSelecionada = tabPane.getSelectionModel().getSelectedItem();
        if (abaSelecionada != null) {
            Node content = ((Pane) abaSelecionada.getContent()).getChildrenUnmodifiable().get(0);
            String texto = "";

            if (content instanceof SwingNode swingNode) {
                RSyntaxTextArea rSyntaxTextArea = (RSyntaxTextArea) ((RTextScrollPane) swingNode.getContent()).getTextArea();
                texto = rSyntaxTextArea.getText();
            } else if (content instanceof TextArea textArea) {
                texto = textArea.getText();
            }

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
                bw.write(texto);
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

        Node conteudo = abaSelecionada.getContent();
        String codigo = null;

        if (conteudo instanceof TextArea editor) {
            codigo = editor.getText();
        } else if (conteudo instanceof StackPane stackPane && !stackPane.getChildren().isEmpty()) {
            Node node = stackPane.getChildren().get(0);
            if (node instanceof SwingNode swingNode) {
                JComponent swingContent = swingNode.getContent();
                if (swingContent instanceof RTextScrollPane scrollPane) {
                    RSyntaxTextArea rsta = (RSyntaxTextArea) scrollPane.getTextArea();
                    codigo = rsta.getText();
                }
            }
        }

        if (codigo == null || codigo.isBlank()) {
            outPut.setText("Código vazio ou editor não identificado.");
            return;
        }

        outPut.setText("Executando...\n");

        String finalCodigo = codigo;
        new Thread(() -> {
            try {
                String resultado = executarPython(finalCodigo);
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
        pb.redirectErrorStream(true);
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


    /*Sintaxe*/

    public class EditorRSyntaxFactory {

        public static JComponent criarEditor(String conteudo, String linguagem) {
            RSyntaxTextArea textArea = new RSyntaxTextArea(30, 80);
            textArea.setSyntaxEditingStyle(getSyntaxConstant(linguagem));
            textArea.setCodeFoldingEnabled(true);
            textArea.setText(conteudo);
            textArea.setCaretPosition(0);
            textArea.setAntiAliasingEnabled(true);


            try {
                Theme theme = Theme.load(EditorRSyntaxFactory.class.getResourceAsStream(
                        "/Themes/monokai.xml"));
                theme.apply(textArea);
            } catch (IOException ioe) { // Never happens
                ioe.printStackTrace();
            }

//            CompletionProvider provider = createCompletionProvider();
//            AutoCompletion ac = new AutoCompletion(provider);
//            ac.install(textArea);

            return new RTextScrollPane(textArea);
        }

        private static String getSyntaxConstant(String linguagem) {
            return switch (linguagem) {
                case "python" -> SyntaxConstants.SYNTAX_STYLE_PYTHON;
                case "java" -> SyntaxConstants.SYNTAX_STYLE_JAVA;
                case "javascript" -> SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
                case "html" -> SyntaxConstants.SYNTAX_STYLE_HTML;
                case "c" -> SyntaxConstants.SYNTAX_STYLE_C;
                case "cpp" -> SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
                case "text" -> SyntaxConstants.SYNTAX_STYLE_NONE;
                default -> SyntaxConstants.SYNTAX_STYLE_NONE;
            };

        }

//        private static CompletionProvider createCompletionProvider() {
//            DefaultCompletionProvider provider = new DefaultCompletionProvider();
//
//            provider.addCompletion(new BasicCompletion(provider, "abstract"));
//            provider.addCompletion(new BasicCompletion(provider, "assert"));
//            provider.addCompletion(new BasicCompletion(provider, "break"));
//            provider.addCompletion(new BasicCompletion(provider, "case"));
//            provider.addCompletion(new BasicCompletion(provider, "transient"));
//            provider.addCompletion(new BasicCompletion(provider, "try"));
//            provider.addCompletion(new BasicCompletion(provider, "void"));
//            provider.addCompletion(new BasicCompletion(provider, "volatile"));
//            provider.addCompletion(new BasicCompletion(provider, "while"));
//
//            provider.addCompletion(new ShorthandCompletion(provider, "sysout",
//                    "System.out.println(", "System.out.println("));
//            provider.addCompletion(new ShorthandCompletion(provider, "syserr",
//                    "System.err.println(", "System.err.println("));
//
//            return provider;
//        }
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
                    DatabaseManager.salvarExplicacao(entrada, resposta);
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

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.getStyleClass().add("output-area");
        output.setPrefWidth(350);
        output.setPrefHeight(400);

        Button btnTraduzir = new Button("Traduzir o código");
        btnTraduzir.getStyleClass().add("botao");

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
    void sugerir() {
        Stage popup = new Stage();
        popup.setTitle("Sugestão de Código");

        TextArea input = new TextArea();
        input.setPromptText("Digite seu código aqui...");
        input.setWrapText(true);
        input.getStyleClass().add("input-area");
        input.setPrefWidth(350);
        input.setPrefHeight(400);

        Button btnSugerir = new Button("Gerar Sugestão");
        btnSugerir.getStyleClass().add("botao");

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.getStyleClass().add("output-area");
        output.setPrefWidth(350);
        output.setPrefHeight(400);

        btnSugerir.setOnAction(e -> {
            String entrada = input.getText();
            output.setText("Aguarde...");
            new Thread(() -> {
                try {
                    String resposta = IA.getSugestaoIA(entrada);
                    javafx.application.Platform.runLater(() -> output.setText(resposta));
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> output.setText("Erro ao tentar obter sugestão: " + ex.getMessage()));
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

        layout.getChildren().addAll(entradaLabel, input, btnSugerir, saidaLabel, output);
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
    @FXML
    void mostrarHistorico() {
        Stage popup = new Stage();
        popup.setTitle("Histórico de Explicações");

        TableView<Historico> tableView = new TableView<>();
        TableColumn<Historico, String> codigoColumn = new TableColumn<>("Código");
        codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));

        TableColumn<Historico, String> explicacaoColumn = new TableColumn<>("Explicação");
        explicacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExplicacao()));

        tableView.getColumns().add(codigoColumn);
        tableView.getColumns().add(explicacaoColumn);

        ObservableList<Historico> historicoList = DatabaseManager.carregarHistorico();
        tableView.setItems(historicoList);

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 30;");
        layout.getChildren().add(tableView);
        layout.getStyleClass().add("popup");

        Scene scene = new Scene(layout, 500, 300);
        scene.getStylesheets().add(getClass().getResource("/Css/principal.css").toExternalForm());
        popup.setScene(scene);

        if (tabPane != null && tabPane.getScene() != null) {
            popup.initOwner(tabPane.getScene().getWindow());
        }

        popup.show();
    }
}