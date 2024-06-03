package Visao;

import Controle.Conexao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Cadastro extends JFrame {

    private JPanel TelaCadastro;
    private JLabel opcoes;
    private JTextField NomeInput;
    private JTextField EmailInput;
    private JTextField CPFInput;
    private JPasswordField SenhaInput;
    private JButton cadastrarButton;
    private JButton cancelarButton;
    private JComboBox<String> userTypeComboBox;

    public Cadastro() {
        setContentPane(TelaCadastro);
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Adiciona dicas de texto temporárias
        addPlaceholder(NomeInput, "Digite seu nome");
        addPlaceholder(EmailInput, "Digite seu Email");
        addPlaceholder(CPFInput, "Digite seu CPF");
        addPlaceholder(SenhaInput, "Digite seu Senha");

        // Adiciona um botão para mostrar a senha dentro do campo de entrada da senha
        JButton mostrarSenhaButton = new JButton("Mostrar");
        SenhaInput.add(mostrarSenhaButton);

        // Adiciona a ação ao botão "mostrar senha"
        mostrarSenhaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SenhaInput.getEchoChar() == 0) {
                    SenhaInput.setEchoChar('*');
                    mostrarSenhaButton.setText("Mostrar");
                } else {
                    SenhaInput.setEchoChar((char) 0);
                    mostrarSenhaButton.setText("Esconder");
                }
            }
        });

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // implementação do cadastro...
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // implementação do cancelamento...
            }
        });
    }

    private void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });
    }
}
