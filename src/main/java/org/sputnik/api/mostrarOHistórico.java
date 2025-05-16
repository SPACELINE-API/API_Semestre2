package org.sputnik.api;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;



public class mostrarOHistórico implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private TableView<Explicacao> tableExplicacoes;
    @FXML
    private TableColumn<Explicacao, String> colCodExp;
    @FXML
    private TableColumn<Explicacao, String> colExplicacao;
    @FXML
    private TableColumn<Explicacao, String> colDataExp;

    @FXML
    private TableView<Sugestao> tableSugestoes;
    @FXML
    private TableColumn<Sugestao, String> colCodSug;
    @FXML
    private TableColumn<Sugestao, String> colSugestao;
    @FXML
    private TableColumn<Sugestao, String> colDataSug;

    @FXML
    private TableView<Traducao> tableTraducoes;
    @FXML
    private TableColumn<Traducao, String> colCodTrad;
    @FXML
    private TableColumn<Traducao, String> colTraducao;
    @FXML
    private TableColumn<Traducao, String> colDataTrad;

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
    private void fechar(ActionEvent event) {
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

        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    @FXML
    private void movimento(MouseEvent event) {
        Stage stage = (Stage) btnTab.getScene().getWindow();

        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colCodExp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colExplicacao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExplicacao()));
        colDataExp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataCriacao().toString()));


        colCodSug.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colSugestao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSugestao()));
        colDataSug.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataCriacao().toString()));

        colCodTrad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colTraducao.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTraducao()));
        colDataTrad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataCriacao().toString()));


        ObservableList<Explicacao> explicacoes = DatabaseManager.carregarHistorico();
        tableExplicacoes.setItems(explicacoes);

        ObservableList<Sugestao> sugestoes = DatabaseManager.carregarSugestoes();
        tableSugestoes.setItems(sugestoes);

        ObservableList<Traducao> traducoes = DatabaseManager.carregarTraducoes();
        tableTraducoes.setItems(traducoes);


        tableExplicacoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableSugestoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableTraducoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
}



