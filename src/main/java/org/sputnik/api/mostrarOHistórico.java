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
    private TableView<Historico> tableView;

    @FXML
    private TableColumn<Historico, String> codigoColumn;

    @FXML
    private TableColumn<Historico, String> explicacaoColumn;

    @FXML
    private TableColumn<Historico, String> dataCriacaoColumn;

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

        codigoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCodigo()));

        explicacaoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getExplicacao()));

        dataCriacaoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataCriacao().toString()));


        ObservableList<Historico> historicoList = DatabaseManager.carregarHistorico();
        tableView.setItems(historicoList);


    }
}



