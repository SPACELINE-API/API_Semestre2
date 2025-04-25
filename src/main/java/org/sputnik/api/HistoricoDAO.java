package org.sputnik.api;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/sputnik";
    private static final String USER = "root";
    private static final String PASSWORD = "fatec";

    public void salvar(String codigo, String explicacao) {
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

    public List<String> listarHistorico() {
        List<String> historico = new ArrayList<>();
        String sql = "SELECT codigo, explicacao, data_criacao FROM historico_explicacoes ORDER BY data_criacao DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String linha = "Código:\n" + rs.getString("codigo") + "\nExplicação:\n" + rs.getString("explicacao") + "\nData: " + rs.getTimestamp("data_criacao");
                historico.add(linha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historico;
    }
}