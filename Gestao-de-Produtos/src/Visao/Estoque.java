package Visao;

import Controle.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Estoque extends JFrame {
    private JPanel TelaProduto;
    private JLabel opcoes;
    private JTextField DescricaoInput;
    private JTextField TipoInput;
    private JTextField ValorInput;
    private JComboBox comboBox1;
    private JTextField NomeInput;
    private JButton salvarButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JScrollPane scrollPane;
    private JTable table;

    public Estoque() {
        setContentPane(TelaProduto);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        String[] colunas = {"ID", "Nome", "Descrição", "Tipo", "Fornecedor", "Valor"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        table = new JTable(model);
        scrollPane.setViewportView(table);

        table.setDefaultEditor(Object.class, null);


        // Atualizar tabela ao iniciar a janela
        atualizarTabela();

        // Criar um JPopupMenu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Home");
        JMenuItem menuItem4 = new JMenuItem("Estoque");

        // Adicionar os itens de menu ao JPopupMenu
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem4);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new adminHome();
                dispose();
            }
        });

        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Estoque();
                dispose();
            }
        });

        // Adicionar um ouvinte de mouse à JLabel "opcoes" para exibir o menu pop-up quando o botão direito do mouse for clicado
        opcoes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    popupMenu.show(opcoes, e.getX(), e.getY());
                }
            }
        });




        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (camposPreenchidos()) {
                    adicionarProduto();
                } else {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                }
            }
        });

        // Ação para o botão "Editar"
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verificar se uma linha está selecionada
                if (table.getSelectedRow() != -1) {
                    // Obter índice da linha selecionada
                    int linhaSelecionada = table.getSelectedRow();

                    // Obter valores da linha selecionada
                    int idProduto = (int) table.getValueAt(linhaSelecionada, 0);

                    // Preencher campos de entrada com valores obtidos
                    NomeInput.setText((String) table.getValueAt(linhaSelecionada, 1));
                    DescricaoInput.setText((String) table.getValueAt(linhaSelecionada, 2));
                    TipoInput.setText((String) table.getValueAt(linhaSelecionada, 3));
                    comboBox1.setSelectedItem((String) table.getValueAt(linhaSelecionada, 4));  // Selecionar fornecedor no comboBox
                    ValorInput.setText(String.valueOf(table.getValueAt(linhaSelecionada, 5)));

                    // Ajustar ação do botão salvar para editar
                    salvarButton.setText("Salvar Edição");
                    salvarButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            editarProduto(idProduto); // Passar o idProduto para o método editarProduto
                            salvarButton.setText("Salvar"); // Restaurar o texto original do botão
                            salvarButton.removeActionListener(this); // Remover o ouvinte de ação para evitar chamadas repetidas
                            JOptionPane.showMessageDialog(null, "Salvo com Sucesso!");
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione um produto para editar!");
                }
            }
        });


        // Ação para o botão "Excluir"
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    excluirProduto();
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione um produto para excluir!");
                }
            }
        });
    };

    // Método para verificar se todos os campos estão preenchidos
    private boolean camposPreenchidos() {
        return !NomeInput.getText().isEmpty() && !DescricaoInput.getText().isEmpty() &&
                comboBox1.getSelectedItem() != null && !TipoInput.getText().isEmpty() &&
                !ValorInput.getText().isEmpty();
    };

    // Método para adicionar um novo produto
    private void adicionarProduto() {
        // Obter valores dos campos
        String nome = NomeInput.getText();
        String descricao = DescricaoInput.getText();
        String tipo = TipoInput.getText();  // Corrigido para obter o tipo do campo TipoInput
        String fornecedor = comboBox1.getSelectedItem().toString();  // Corrigido para obter o fornecedor do ComboBox
        double valor = Double.parseDouble(ValorInput.getText());

        // Criar um novo objeto Produto e adicionar ao banco de dados
        Produto novoProduto = new Produto();
        novoProduto.setNome(nome);
        novoProduto.setDescricao(descricao);
        novoProduto.setTipo(tipo);
        novoProduto.setFornecedor(fornecedor);
        novoProduto.setValor_unit(valor);
        novoProduto.adicionarProduto();

        // Limpar campos após adição
        limparCampos();

        // Atualizar tabela
        atualizarTabela();
    };



    // Método para editar um produto existente
    private void editarProduto(int idProduto) {
        // Obter valores dos campos
        String nome = NomeInput.getText();
        String descricao = DescricaoInput.getText();
        String tipo = TipoInput.getText();
        String fornecedor = comboBox1.getSelectedItem().toString();
        double valor = Double.parseDouble(ValorInput.getText());

        // Criar um objeto Produto com os novos valores
        Produto produtoEditado = new Produto();
        produtoEditado.setId_produto(idProduto);
        produtoEditado.setNome(nome);
        produtoEditado.setDescricao(descricao);
        produtoEditado.setTipo(tipo);
        produtoEditado.setFornecedor(fornecedor);
        produtoEditado.setValor_unit(valor);

        // Editar o produto no banco de dados
        produtoEditado.editarProduto();

        // Limpar campos após edição
        limparCampos();

        // Atualizar tabela
        atualizarTabela();
    };



    // Método para excluir um produto
    private void excluirProduto() {
        // Obter o ID do produto selecionado na tabela
        int linhaSelecionada = table.getSelectedRow();
        int idProduto = (int) table.getValueAt(linhaSelecionada, 0);

        // Criar um objeto Produto com o ID
        Produto produtoExcluir = new Produto();
        produtoExcluir.setId_produto(idProduto);

        // Excluir o produto do banco de dados
        produtoExcluir.removerProduto();

        // Atualizar tabela
        atualizarTabela();
    };

    // Método para limpar os campos de entrada
    private void limparCampos() {
        NomeInput.setText("");
        DescricaoInput.setText("");
        comboBox1.setSelectedIndex(0);
        TipoInput.setText("");
        ValorInput.setText("");
    };

    // Método para atualizar a tabela de produtos
    private void atualizarTabela() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpa a tabela

        // Buscar todos os produtos e adicionar na tabela
        List<Produto> produtos = Produto.buscarTodosProdutos();
        for (Produto produto : produtos) {
            Object[] row = {produto.getId_produto(), produto.getNome(), produto.getDescricao(), produto.getTipo(), produto.getFornecedor(), produto.getValor_unit()};
            model.addRow(row);
        }
    };
}
