package Visao;

import Controle.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Produtos extends JFrame {
    private JPanel TelaProdutos;
    private JLabel opcoes;
    private JScrollPane scrollPane;
    private JTable table1;
    private JPanel infoFinais;
    private JLabel txtTotal;
    private JLabel valorTotal;
    private JButton comprarButton;
    private Map<Integer, Integer> contador = new HashMap<>(); // Mapa para manter o contador para cada linha
    private double total = 0.0; // Variável para armazenar o valor total da compra

    public Produtos() {
        setContentPane(TelaProdutos);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        String[] colunas = {"ID", "Nome", "Descrição", "Tipo", "Fornecedor", "Valor", "Adicionar", "Diminuir", "Contador"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        table1 = new JTable(model);
        scrollPane.setViewportView(table1);

        table1.setDefaultEditor(Object.class, null);

        // Adicionar o renderizador de botão personalizado para as colunas "Adicionar" e "Diminuir"
        table1.getColumnModel().getColumn(colunas.length - 3).setCellRenderer(new ButtonRenderer("+"));
        table1.getColumnModel().getColumn(colunas.length - 2).setCellRenderer(new ButtonRenderer("-"));
        // Adicionar a ação aos botões
        table1.getColumnModel().getColumn(colunas.length - 3).setCellEditor(new ButtonEditor(new JCheckBox(), "+"));
        table1.getColumnModel().getColumn(colunas.length - 2).setCellEditor(new ButtonEditor(new JCheckBox(), "-"));

        // Atualizar tabela ao iniciar a janela
        atualizarTabela();

        // Configurar o botão de comprar
        comprarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerarNotaFiscal();
                JOptionPane.showMessageDialog(null, "Compra realizada! Valor total: R$ " + total);
                // Resetar os contadores após a compra
                resetarContadores();
            }
        });

        // Criar um JPopupMenu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Home");
        JMenuItem menuItem4 = new JMenuItem("Produtos");

        // Adicionar os itens de menu ao JPopupMenu
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem4);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home();
                dispose();
            }
        });

        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Produto();
                dispose(); // Fechar a janela atual sem abrir uma nova
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

    }

    // Método para atualizar a tabela de produtos
    private void atualizarTabela() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Limpa a tabela

        // Buscar todos os produtos e adicionar na tabela
        List<Produto> produtos = Produto.buscarTodosProdutos();
        for (Produto produto : produtos) {
            Object[] row = {produto.getId_produto(), produto.getNome(), produto.getDescricao(), produto.getTipo(), produto.getFornecedor(), produto.getValor_unit(), "+", "-", 0};
            model.addRow(row);
            contador.put(produto.getId_produto(), 0); // Inicializa o contador como 0 para cada produto
        }
    }

    // Método para atualizar o valor total
    private void atualizarValorTotal() {
        total = 0.0;
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            int count = (int) model.getValueAt(i, model.getColumnCount() - 1);
            double valor = (double) model.getValueAt(i, 5);
            total += count * valor;
        }
        valorTotal.setText("R$ " + total);
    }

    // Método para resetar os contadores após a compra
    private void resetarContadores() {
        contador.clear();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(0, i, model.getColumnCount() - 1);
            contador.put((int) model.getValueAt(i, 0), 0);
        }
        atualizarValorTotal();
    }

    // Método para gerar a nota fiscal em PDF
    private void gerarNotaFiscal() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Nota Fiscal");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile() + ".pdf";

            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                Font boldFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
                Font regularFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

                String notaFiscalId = UUID.randomUUID().toString();
                String dataVenda = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

                Paragraph title = new Paragraph("Nota Fiscal", boldFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                document.add(new Paragraph("\n"));

                Paragraph info = new Paragraph("ID da Nota Fiscal: " + notaFiscalId + "\nData da Venda: " + dataVenda, regularFont);
                document.add(info);

                document.add(new Paragraph("\n"));

                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Adicionando cabeçalhos
                PdfPCell cell1 = new PdfPCell(new Phrase("ID", boldFont));
                PdfPCell cell2 = new PdfPCell(new Phrase("Nome", boldFont));
                PdfPCell cell3 = new PdfPCell(new Phrase("Descrição", boldFont));
                PdfPCell cell4 = new PdfPCell(new Phrase("Quantidade", boldFont));
                PdfPCell cell5 = new PdfPCell(new Phrase("Valor Unitário", boldFont));
                PdfPCell cell6 = new PdfPCell(new Phrase("Total", boldFont));

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);

                // Adicionando dados da tabela
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    int count = (int) model.getValueAt(i, model.getColumnCount() - 1);
                    if (count > 0) {
                        int id = (int) model.getValueAt(i, 0);
                        String nome = (String) model.getValueAt(i, 1);
                        String descricao = (String) model.getValueAt(i, 2);
                        double valorUnitario = (double) model.getValueAt(i, 5);
                        double totalProduto = count * valorUnitario;

                        table.addCell(new Phrase(String.valueOf(id), regularFont));
                        table.addCell(new Phrase(nome, regularFont));
                        table.addCell(new Phrase(descricao, regularFont));
                        table.addCell(new Phrase(String.valueOf(count), regularFont));
                        table.addCell(new Phrase(String.valueOf(valorUnitario), regularFont));
                        table.addCell(new Phrase(String.valueOf(totalProduto), regularFont));
                    }
                }

                document.add(table);
                document.close();

                JOptionPane.showMessageDialog(null, "Nota fiscal salva em: " + filePath);
            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao gerar a nota fiscal.");
            }
        }
    }

    // Classe para renderizar botões na tabela
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Classe para editar células de botão na tabela
    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, String label) {
            super(checkBox);
            this.label = label;
            button = new JButton(label);
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table1.getSelectedRow();
                int id = (int) table1.getValueAt(row, 0);
                int count = contador.get(id);
                if (label.equals("+")) {
                    count++;
                } else if (label.equals("-") && count > 0) {
                    count--;
                }
                contador.put(id, count);
                table1.setValueAt(count, row, table1.getColumnCount() - 1);
                atualizarValorTotal();
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public static void main(String[] args) {
        new Produtos();
    }
}
