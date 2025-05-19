package org.sputnik.api;

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
import javafx.scene.input.*;
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
import javafx.application.Platform;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.undo.UndoManager;
import javafx.scene.control.TreeView;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import java.util.ResourceBundle;

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
        UndoManager undoManager = new UndoManager();
        RSyntaxTextArea textArea = new RSyntaxTextArea();
        textArea.getDocument().addUndoableEditListener(undoManager);
        Platform.runLater(() -> {
            Scene scene = topPane.getScene();
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.isControlDown() && event.getCode() == KeyCode.O) {
                    File arquivo = fileChooser.showOpenDialog(scene.getWindow());
                    adicionarArquivoNaTreeView(arquivo);
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.N) {
                    novoArquivo();
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.S) {
                    salvarArquivo();
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.H) {
                    ajuda();
                    event.consume();
                }
                if (event.getCode() == KeyCode.F5) {
                    compilar();
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.T) {
                    traduzir("binario");
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.E) {
                    explicar();
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.D) {
                    mostrarHistorico();
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.R) {
                    sugerir();
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                    RSyntaxTextArea rsta = getEditorAtual();
                    if (rsta != null) {
                        UndoManager manager = EditorRSyntaxFactory.getUndoManager(rsta);
                        if (manager != null && manager.canUndo()) {
                            manager.undo();
                        }
                    }
                    event.consume();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                    RSyntaxTextArea rsta = getEditorAtual();
                    if (rsta != null) {
                        UndoManager manager = EditorRSyntaxFactory.getUndoManager(rsta);
                        if (manager != null && manager.canRedo()) {
                            manager.redo();
                        }
                    }
                    event.consume();
                }
            });
        });
    ;}

        private RSyntaxTextArea getEditorAtual() {
            Tab aba = tabPane.getSelectionModel().getSelectedItem();
            if (aba == null) return null;

            Node conteudo = aba.getContent();
            if (conteudo instanceof StackPane stack && !stack.getChildren().isEmpty()) {
                Node node = stack.getChildren().get(0);
                if (node instanceof SwingNode swingNode) {
                    JComponent editorComScroll = swingNode.getContent();
                    if (editorComScroll instanceof RTextScrollPane scrollPane) {
                        return (RSyntaxTextArea) scrollPane.getTextArea();
                    }
                }
            }
            return null;
        }

        private void configurarTreeView () {
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

        private void adicionarArquivoNaTreeView (File arquivo){
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
        private void abrirArquivoDireto (File file){
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
        void novoArquivo () {
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
        private void abrirArquivo (ActionEvent event){
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

        private String detectarLinguagem (File file){
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
        void salvarArquivo () {
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
        void compilar () {
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


        private String executarPython (String codigoPython) throws IOException, InterruptedException {

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
            private static final Map<RSyntaxTextArea, UndoManager> undoManagers = new HashMap<>();

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
                UndoManager undoManager = new UndoManager();
                textArea.getDocument().addUndoableEditListener(undoManager);
                undoManagers.put(textArea, undoManager);

                return new RTextScrollPane(textArea);
            }

            public static UndoManager getUndoManager(RSyntaxTextArea textArea) {
                return undoManagers.get(textArea);
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
        void explicar () {
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
                        Timestamp dataCriacao = new Timestamp(System.currentTimeMillis());
                        DatabaseManager.salvarExplicacao(entrada, resposta, dataCriacao);
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
        void traduzirLinguagem (javafx.event.ActionEvent event){
            MenuItem item = (MenuItem) event.getSource();
            String linguagem = (String) item.getUserData();
            traduzir(linguagem);
        }


        @FXML
        void traduzir (String linguagem){
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
        void sugerir () {
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


        public Label criarLabel (String texto, String classe){
            Label label = new Label(texto);
            label.getStyleClass().add("titulo");
            label.setWrapText(true);
            return label;
        }
        ;

        public Text criarText (String texto, Scene scene){
            Text text = new Text(texto);
            text.getStyleClass().add("conteudo");
            text.wrappingWidthProperty().bind(scene.widthProperty().subtract(60));
            return text;
        }
        ;

        @FXML
        void ajuda () {
            Stage popup = new Stage();
            popup.setTitle("Ajuda");

            Rectangle2D tela = Screen.getPrimary().getVisualBounds();
            double larguraTela = tela.getWidth();

            VBox layout = new VBox(10);
            layout.setStyle("-fx-padding: 30;");
            layout.getStyleClass().add("popup");

            ScrollPane scrollPane = new ScrollPane(layout);
            scrollPane.setFitToWidth(true);
            Scene scene = new Scene(scrollPane, 400, 450);
            scene.getStylesheets().add(getClass().getResource("/Css/principal.css").toExternalForm());
            popup.setScene(scene);

            layout.getChildren().addAll(
                    criarLabel("Bem-vindo à Sputnik! \uD83D\uDC0D", "titulo"),
                    criarText("Sputnik é um ambiente de desenvolvimento integrado (IDE) criado especialmente para facilitar a escrita, execução e " +
                            "organização de seus projetos em Python. ", scene),

                    criarLabel("\uD83D\uDD27 O que você pode fazer aqui:", "titulo"),
                    criarText("\u25CF Criar, abrir e salvar arquivos .py\n\u25CF Executar seu código Python diretamente na IDE\n\u25CF Consultar o histórico de explicações do seu código", scene),

                    criarLabel("\uD83D\uDCA1 Assistente de IA", "titulo"),
                    criarText("A Sputnik utiliza inteligência artificial para auxiliar os desenvolvedores. Algumas de suas funcionalidades são:\n\u25CF Sugestão de trechos de código\n\u25CF Explicação " +
                            "do código\n\u25CF Tradução de código Python para binário", scene),

                    criarLabel("\uD83D\uDCCC Dicas\n", "subtitulo"),
                    criarText("\u25CF Quanto mais contexto no código, melhores as sugestões\n\u25CF Revise sempre as sugestões antes de confirmar\n\u25CF Você pode editar ou ignorar qualquer sugestão", scene),

                    criarLabel("\u2757 Limitações", "subtitulo"),
                    criarText("\u25CF A IA não garante que o código seja 100% correto ou otimizado\n\u25CF Não substitui revisão humana nem testes manuais", scene),

                    criarLabel("Atalhos", "titulo"),
                    criarText("Novo arquivo \u2192 Ctrl + N \nAbrir arquivo \u2192 Ctrl + O\nSalvar arquivo \u2192 Ctrl + S\nAjuda \u2192 Ctrl + H\nMostrar histórico \u2192 Ctrl + D\nExecutar \u2192 F5\n" +
                            "Traduzir o código \u2192 Ctrl + T \nExplicar o código \u2192 Ctrl + E\nSugerir formas de completar o código \u2192 Ctrl + R", scene
                    )
            );

            if (tabPane != null && tabPane.getScene() != null) {
                popup.initOwner(tabPane.getScene().getWindow());
            }

            popup.show();
        }


        @FXML
        void mostrarHistorico () {
            Stage popup = new Stage();
            popup.setTitle("Histórico de Explicações");

            TableView<Historico> tableView = new TableView<>();
            TableColumn<Historico, String> codigoColumn = new TableColumn<>("Código");
            codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));

            TableColumn<Historico, String> explicacaoColumn = new TableColumn<>("Explicação");
            explicacaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExplicacao()));

            TableColumn<Historico, String> dataColumn = new TableColumn<>("Data");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dataColumn.setCellValueFactory(cellData -> {
                Timestamp timestamp = cellData.getValue().getDataCriacao();
                String formattedDate = (timestamp != null) ? dateFormat.format(timestamp) : "";
                return new SimpleStringProperty(formattedDate);
            });

            tableView.getColumns().addAll(codigoColumn, explicacaoColumn, dataColumn);

            tableView.setItems(DatabaseManager.carregarHistorico());


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