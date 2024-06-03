package Controle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Produto {
    private int id_produto;
    private String nome;
    private String descricao;
    private String tipo;
    private String fornecedor;
    private double valor_unit;

    // Getters e setters

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(double valor_unit) {
        this.valor_unit = valor_unit;
    }

    // Buscar todos os produtos
    public static List<Produto> buscarTodosProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId_produto(rs.getInt("ID_Produto"));
                produto.setNome(rs.getString("Nome"));
                produto.setDescricao(rs.getString("Descricao"));
                produto.setTipo(rs.getString("Tipo"));
                produto.setFornecedor(rs.getString("Fornecedor"));
                produto.setValor_unit(rs.getDouble("Valor"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }


    // Adicionar um novo produto
    public void adicionarProduto() {
        String sql = "INSERT INTO Produtos (Nome, Descricao, Tipo, Fornecedor, Valor) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, descricao);
            pstmt.setString(3, tipo);
            pstmt.setString(4, fornecedor);
            pstmt.setDouble(5, valor_unit);

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id_produto = generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Editar um produto existente
    public void editarProduto() {
        String sql = "UPDATE Produtos SET Nome = ?, Descricao = ?, Tipo = ?, Fornecedor = ?, Valor = ? WHERE ID_Produto = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, descricao);
            pstmt.setString(3, tipo);
            pstmt.setString(4, fornecedor);
            pstmt.setDouble(5, valor_unit);
            pstmt.setInt(6, id_produto);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remover um produto
    public void removerProduto() {
        String sql = "DELETE FROM Produtos WHERE ID_Produto = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_produto);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
