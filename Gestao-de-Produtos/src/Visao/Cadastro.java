package Visao;

import Controle.Conexao;

import javax.swing.*;
import java.awt.*;
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
    private JPasswordField ConfirmarSenha;

    public Cadastro() {
        setContentPane(TelaCadastro);
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Configurar placeholders
        configurePlaceholders();

        // Criar um JPopupMenu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem2 = new JMenuItem("Login");
        JMenuItem menuItem3 = new JMenuItem("Cadastro");

        // Adicionar os itens de menu ao JPopupMenu
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);

        // ActionListener para menuItem2 (Login)
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Cadastro();
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

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = NomeInput.getText();
                String email = EmailInput.getText();
                String cpf = CPFInput.getText();
                String senha = new String(SenhaInput.getPassword());
                String confirmarSenha = new String(ConfirmarSenha.getPassword());
                String perfil = (String) userTypeComboBox.getSelectedItem();

                if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
                } else if (!validateEmail(email)) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um email válido.");
                } else if (!validateCPF(cpf)) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um CPF válido.");
                } else if (!validatePasswordChars(senha)) {
                    JOptionPane.showMessageDialog(null, "A senha deve conter pelo menos um número, uma letra e um caractere especial.");
                } else if (!senha.equals(confirmarSenha)) {
                    JOptionPane.showMessageDialog(null, "As senhas não coincidem.");
                } else {
                    Conexao.InserirUsuario(nome, email, cpf, senha, perfil);
                    JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");
                    dispose();
                    new Login();
                }
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home();
            }
        });
    }

    // Método para validar se a senha possui número, letra e caractere especial
    private boolean validatePasswordChars(String senha) {
        boolean hasNumber = false;
        boolean hasLetter = false;
        boolean hasSpecialChar = false;

        for (char c : senha.toCharArray()) {
            if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasNumber && hasLetter && hasSpecialChar;
    }

    // Método para validar se o email está em um formato válido
    private boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*(?:\\.[a-zA-Z]{2,7})$";
        return email.matches(regex);
    }

    // Método para validar o CPF
    private boolean validateCPF(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Valida o CPF usando o algoritmo
        int[] numbers = new int[11];
        for (int i = 0; i < 11; i++) {
            numbers[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += numbers[i] * (10 - i);
        }

        int remainder = sum % 11;
        int expectedDigit1 = (remainder < 2) ? 0 : (11 - remainder);

        if (numbers[9] != expectedDigit1) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += numbers[i] * (11 - i);
        }

        remainder = sum % 11;
        int expectedDigit2 = (remainder < 2) ? 0 : (11 - remainder);

        return numbers[10] == expectedDigit2;
    }

    private void configurePlaceholders() {
        addPlaceholder(NomeInput, "Digite seu nome");
        addPlaceholder(EmailInput, "Digite seu email");
        addPlaceholder(CPFInput, "Digite seu CPF");
        addPlaceholder(SenhaInput, "Digite sua Senha");
        addPlaceholder(ConfirmarSenha, "Digite sua senha novamente");
    }

    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    public static void main(String[] args) {
        new Cadastro();
    }
}
