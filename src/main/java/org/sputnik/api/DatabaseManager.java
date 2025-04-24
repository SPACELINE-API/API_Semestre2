package org.sputnik.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Timestamp;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/sputnik";
    private static final String USER = "root";
    private static final String PASSWORD = "fatec";


    public static void salvarExplicacao(String codigo, String explicacao) {
        String sql = "INSERT INTO historico_explicacoes (codigo, explicacao) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.setString(2, explicacao);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ObservableList<Historico> carregarHistorico() {
        ObservableList<Historico> historicoList = FXCollections.observableArrayList();
        String sql = "SELECT codigo, explicacao, data_criacao FROM historico_explicacoes";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String explicacao = rs.getString("explicacao");
                Timestamp dataCriacao = rs.getTimestamp("data_criacao");
                historicoList.add(new Historico(codigo, explicacao, dataCriacao));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar histórico: " + e.getMessage());
        }
        return historicoList;
    }
}