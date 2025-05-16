package org.sputnik.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/sputnik";
    private static final String USER = "root";
    private static final String PASSWORD = "fatec";


    public static void salvarExplicacao(String codigo, String explicacao, LocalDateTime dataCriacao) {
        String sql = "INSERT INTO historico_explicacoes (codigo, explicacao, data_criacao) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.setString(2, explicacao);
            stmt.setTimestamp(3, Timestamp.valueOf(dataCriacao));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void salvarSugestao(String codigo, String sugestao, LocalDateTime dataCriacao) {
        String sql = "INSERT INTO sugestoes (codigo, sugestao, data_criacao) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.setString(2, sugestao);
            stmt.setTimestamp(3, Timestamp.valueOf(dataCriacao));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void salvarTraducao(String codigo, String traducao, LocalDateTime dataCriacao) {
        String sql = "INSERT INTO traducoes (codigo, traducao, data_criacao) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.setString(2, traducao);
            stmt.setTimestamp(3, Timestamp.valueOf(dataCriacao));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static ObservableList<Explicacao> carregarHistorico() {
        ObservableList<Explicacao> historicoList = FXCollections.observableArrayList();
        String sql = "SELECT codigo, explicacao, data_criacao FROM historico_explicacoes";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String explicacao = rs.getString("explicacao");
                Timestamp ts = rs.getTimestamp("data_criacao");
                LocalDateTime dataCriacao = ts.toLocalDateTime();
                historicoList.add(new Explicacao(codigo, explicacao, dataCriacao));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar histórico: " + e.getMessage());
        }
        return historicoList;
    }


    public static ObservableList<Sugestao> carregarSugestoes() {
        ObservableList<Sugestao> sugestaoList = FXCollections.observableArrayList();
        String sql = "SELECT codigo, sugestao, data_criacao FROM sugestoes";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String sugestao = rs.getString("sugestao");
                Timestamp ts = rs.getTimestamp("data_criacao");
                LocalDateTime dataCriacao = ts.toLocalDateTime();
                sugestaoList.add(new Sugestao(codigo, sugestao, dataCriacao));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar sugestões: " + e.getMessage());
        }
        return sugestaoList;
    }


    public static ObservableList<Traducao> carregarTraducoes() {
        ObservableList<Traducao> traducaoList = FXCollections.observableArrayList();
        String sql = "SELECT codigo, traducao, data_criacao FROM traducoes";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String traducao = rs.getString("traducao");
                Timestamp ts = rs.getTimestamp("data_criacao");
                LocalDateTime dataCriacao = ts.toLocalDateTime();
                traducaoList.add(new Traducao(codigo, traducao, dataCriacao));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar traduções: " + e.getMessage());
        }
        return traducaoList;
    }
}
