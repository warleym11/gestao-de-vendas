package Controle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexao {
    // Configurações de conexão com o banco de dados
    private static final String URL = "jdbc:postgresql://localhost:5432/gestaoDeProdutos";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "12345678";

    // Método para estabelecer a conexão com o banco de dados
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    // Método para inserir dados na tabela de usuários
    public static void InserirUsuario(String nome, String email, String cpf, String senha, String perfil) {
        String sql = "INSERT INTO usuario (nome, email, cpf, senha, perfil) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setString(3, cpf);
            pstmt.setString(4, senha);
            pstmt.setString(5, perfil);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para verificar o login
    public static boolean verificarLogin(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, senha);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // retorna true se encontrar um resultado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
