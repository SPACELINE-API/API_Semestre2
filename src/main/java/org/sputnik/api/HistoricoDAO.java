package org.sputnik.api;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO {

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/sputnik", "root", "");
    }

    // -------------------- EXPLICAÇÃO --------------------
    public void salvarExplicacao(Explicacao explicacao) {
        String sql = "INSERT INTO historico_explicacoes (codigo, explicacao, data_criacao) VALUES (?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, explicacao.getCodigo());
            stmt.setString(2, explicacao.getExplicacao());
            stmt.setTimestamp(3, Timestamp.valueOf(explicacao.getDataCriacao()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Explicacao> listarExplicacoes() {
        List<Explicacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM historico_explicacoes";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Explicacao e = new Explicacao(
                        rs.getString("codigo"),
                        rs.getString("explicacao"),
                        rs.getTimestamp("data_criacao").toLocalDateTime()
                );
                lista.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // -------------------- SUGESTÃO --------------------
    public void salvarSugestao(Sugestao sugestao) {
        String sql = "INSERT INTO historico_sugestoes (codigo_sugestao, sugestao, data_criacao_sugestao) VALUES (?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sugestao.getCodigo());
            stmt.setString(2, sugestao.getSugestao());
            stmt.setTimestamp(3, Timestamp.valueOf(sugestao.getDataCriacao()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Sugestao> listarSugestoes() {
        List<Sugestao> lista = new ArrayList<>();
        String sql = "SELECT * FROM historico_sugestoes";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sugestao s = new Sugestao(
                        rs.getString("codigo_sugestao"),
                        rs.getString("sugestao"),
                        rs.getTimestamp("data_criacao_sugestao").toLocalDateTime()
                );
                lista.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // -------------------- TRADUÇÃO --------------------
    public void salvarTraducao(Traducao traducao) {
        String sql = "INSERT INTO historico_traducao (codigo_traducao, traducao, data_criacao_traducao) VALUES (?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, traducao.getCodigo());
            stmt.setString(2, traducao.getTraducao());
            stmt.setTimestamp(3, Timestamp.valueOf(traducao.getDataCriacao()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Traducao> listarTraducoes() {
        List<Traducao> lista = new ArrayList<>();
        String sql = "SELECT * FROM historico_traducao";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Traducao t = new Traducao(
                        rs.getString("codigo_traducao"),
                        rs.getString("traducao"),
                        rs.getTimestamp("data_criacao_traducao").toLocalDateTime()
                );
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
