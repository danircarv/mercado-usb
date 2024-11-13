import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Mercado extends JFrame {
    private GerenciadorDeConta gerenciador;
    private JTextField nomeField;
    private JPasswordField senhaField;
    private Cliente contaAutenticada;
    private int quantidadeAbacate = 0; // Quantidade inicial de abacate

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

        // Ação do botão de login ao pressionar Enter no campo de senha
        senhaField.addActionListener(e -> loginButton.doClick());

        // Ação do botão de login ao clicar no botão
        loginButton.addActionListener(e -> autenticarUsuario());

        setVisible(true);
    }

    private void abrirTelaPrincipal() {
        JFrame telaPrincipal = new JFrame("Mercado - Área Principal");
        telaPrincipal.setSize(1080, 720);
        telaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaPrincipal.setLocationRelativeTo(null);
        telaPrincipal.setLayout(new BorderLayout());

        // Cabeçalho
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1080, 50));
        headerPanel.setBackground(Color.LIGHT_GRAY);

        // Nome do cliente
        String nomeCliente = contaAutenticada.getNome();
        JLabel nomeClienteLabel = new JLabel("Bem-vindo, " + nomeCliente);
        nomeClienteLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Verificar se o cliente é premium
        if (contaAutenticada instanceof ClientePremium) {
            nomeClienteLabel.setForeground(Color.ORANGE);
        }

        headerPanel.add(nomeClienteLabel, BorderLayout.WEST);

        // Ícone do carrinho e quantidade de pontos
        ImageIcon carrinhoIcon = new ImageIcon("C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/carrinho.png");
        Image img = carrinhoIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // Redimensiona a imagem para 32x32
        carrinhoIcon = new ImageIcon(img);
        JLabel carrinhoLabel = new JLabel(carrinhoIcon);

        int pontos = contaAutenticada.getPontos();
        JLabel pontosLabel = new JLabel("Pontos: " + pontos);
        pontosLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel carrinhoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        carrinhoPanel.add(carrinhoLabel);
        carrinhoPanel.add(pontosLabel);
        headerPanel.add(carrinhoPanel, BorderLayout.EAST);

        telaPrincipal.add(headerPanel, BorderLayout.NORTH);

        // Painel Central com Produtos
        JPanel produtosPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 6 produtos
        telaPrincipal.add(produtosPanel, BorderLayout.CENTER);

        // Cria cada produto
        for (int i = 0; i < 6; i++) {
            JPanel produtoPanel = new JPanel();
            produtoPanel.setLayout(new BoxLayout(produtoPanel, BoxLayout.Y_AXIS));
            produtoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Botões de controle de quantidade
            JPanel quantidadePanel = new JPanel();
            JButton maisButton = new JButton("+");
            JButton menosButton = new JButton("-");

            JLabel quantidadeLabel = new JLabel("Quantidade: " + quantidadeAbacate);

            maisButton.addActionListener(e -> {
                quantidadeAbacate++;
                quantidadeLabel.setText("Quantidade: " + quantidadeAbacate);
            });

            menosButton.addActionListener(e -> {
                if (quantidadeAbacate > 0) {
                    quantidadeAbacate--;
                    quantidadeLabel.setText("Quantidade: " + quantidadeAbacate);
                }
            });

            quantidadePanel.add(menosButton);
            quantidadePanel.add(quantidadeLabel);
            quantidadePanel.add(maisButton);
            produtoPanel.add(quantidadePanel);

            // Imagem do produto
            ImageIcon abacateIcon = new ImageIcon("C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/abacate.png");
            Image acabateImg = abacateIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            abacateIcon = new ImageIcon(acabateImg);
            JLabel abacateLabel = new JLabel(abacateIcon);
            abacateLabel.setHorizontalAlignment(JLabel.CENTER);
            produtoPanel.add(abacateLabel);

            // Botão Adicionar
            JButton adicionarButton = new JButton("Adicionar");
            adicionarButton.addActionListener(e -> {
                Abacate abacate = new Abacate();
                double valorTotal = abacate.getValor(quantidadeAbacate);
                System.out.println("Quantidade de abacate adicionada: " + quantidadeAbacate);
                System.out.println("Preço total: " + valorTotal);
            });
            produtoPanel.add(adicionarButton);

            produtosPanel.add(produtoPanel);
        }

        // Exibe o valor final no console ao fechar
        telaPrincipal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Abacate abacate = new Abacate();
                double valorTotal = abacate.getValor(quantidadeAbacate);
                System.out.println("Quantidade de abacate final: " + quantidadeAbacate);
                System.out.println("Preço final: " + valorTotal);
            }
        });

        telaPrincipal.setVisible(true);
    }

    private void autenticarUsuario() {
        String nome = nomeField.getText().trim();
        String senhaTexto = new String(senhaField.getPassword()).trim();

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

    public static void main(String[] args) {
        GerenciadorDeConta gerenciador = new GerenciadorDeConta();
        gerenciador.carregarContas("C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/dados.csv");
        new Mercado(gerenciador);
    }
}