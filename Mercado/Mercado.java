import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Mercado extends JFrame {
    private GerenciadorDeConta gerenciador;
    private JTextField nomeField;
    private JPasswordField senhaField;
    private Cliente contaAutenticada;

    public Mercado(GerenciadorDeConta gerenciador) {
        this.gerenciador = gerenciador;
        inicializarTelaLogin();
    }

    private void inicializarTelaLogin() {
        setTitle("Mercado - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Nome:"));
        nomeField = new JTextField();
        add(nomeField);

        add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        add(senhaField);

        JButton loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });

        setVisible(true);
    }

    private void autenticarUsuario() {
        String nome = nomeField.getText();
        String senhaTexto = new String(senhaField.getPassword());

        try {
            int senha = Integer.parseInt(senhaTexto);
            for (Cliente conta : gerenciador.getContas().values()) {
                if (conta.getNome().equals(nome) && conta.getSenha() == senha) {
                    contaAutenticada = conta;
                    abrirTelaPrincipal();
                    dispose();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Nome ou senha incorretos", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Senha inválida", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirTelaPrincipal() {
        JFrame telaPrincipal = new JFrame("Mercado - Área Principal");
        telaPrincipal.setSize(900, 600);
        telaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaPrincipal.setLocationRelativeTo(null);

        ImageIcon backgroundIcon = new ImageIcon("Mercado/fundo.jpg");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new FlowLayout());
        telaPrincipal.setContentPane(backgroundLabel);

        JLabel pontosLabel = new JLabel("Pontos: " + contaAutenticada.getPontos());
        pontosLabel.setForeground(Color.BLACK); // Muda a cor do texto para ser visível no fundo
        backgroundLabel.add(pontosLabel);


        JButton adicionarPontosButton = new JButton("Adicionar Pontos");
        backgroundLabel.add(adicionarPontosButton);

        adicionarPontosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contaAutenticada.setPontos(contaAutenticada.getPontos() + 10);
                pontosLabel.setText("Pontos: " + contaAutenticada.getPontos());
            }
        });

        telaPrincipal.setVisible(true);
    }

    public static void main(String[] args) {
        GerenciadorDeConta gerenciador = new GerenciadorDeConta();
        gerenciador.carregarContas("C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/dados.csv");
        new Mercado(gerenciador);
    }
}
