import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Mercado extends JFrame {
    private GerenciadorDeConta gerenciador;
    private JTextField nomeField;
    private JPasswordField senhaField;
    private Cliente contaAutenticada;
    private int quantidadeAbacate = 0;
    private int quantidadeMaca = 0;
    private int quantidadeBanana = 0;
    private int quantidadeCelular = 0;
    private int quantidadeNotebook = 0;
    private int quantidadeTablet = 0;
    private ArrayList<JSpinner> spinners = new ArrayList<>();
    private ArrayList<Object> produtos = new ArrayList<>();

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

        senhaField.addActionListener(e -> loginButton.doClick());

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

        String nomeCliente = contaAutenticada.getNome();
        JLabel nomeClienteLabel = new JLabel("Bem-vindo, " + nomeCliente);
        nomeClienteLabel.setFont(new Font("Arial", Font.BOLD, 16));

        if (contaAutenticada instanceof ClientePremium) {
            nomeClienteLabel.setForeground(Color.ORANGE);
        }

        headerPanel.add(nomeClienteLabel, BorderLayout.WEST);

        ImageIcon carrinhoIcon = new ImageIcon("C:/Users/danie/Downloads/Trabalho/Trabalho/Trabalho/mercado-usb/Mercado/carrinho.png");
        Image img = carrinhoIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
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

        // Painel de Produtos
        JPanel produtosPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        produtosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inicializa produtos
        produtos.clear(); // Limpa a lista antes de adicionar
        produtos.add(new Abacate());
        produtos.add(new Banana());
        produtos.add(new Maca());
        produtos.add(new Celular());
        produtos.add(new Notebook());
        produtos.add(new Tablet());

        // Limpa a lista de spinners
        spinners.clear();

        // Array com nomes das imagens
        String[] imagensNomes = {"abacate", "banana", "maca", "celular", "notebook", "tablet"};

        // Adiciona os produtos ao grid
        for (int i = 0; i < produtos.size(); i++) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            String caminhoBase = "C:/Users/danie/Downloads/Trabalho/Trabalho/Trabalho/mercado-usb/Mercado/";
            String caminhoImagem = caminhoBase + imagensNomes[i] + ".png";
            
            ImageIcon icon = new ImageIcon(caminhoImagem);
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.out.println("Erro ao carregar imagem: " + caminhoImagem);
                icon = new ImageIcon(caminhoBase + "abacate.png");
            }
            
            Image scaledImg = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(imageLabel);

            // Nome do produto
            String nome = "";
            if (produtos.get(i) instanceof Alimentos) {
                nome = ((Alimentos) produtos.get(i)).getNome();
            } else if (produtos.get(i) instanceof Eletronicos) {
                nome = ((Eletronicos) produtos.get(i)).getNome();
            }
            JLabel nameLabel = new JLabel(nome);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(nameLabel);

            // Spinner
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            spinner.setMaximumSize(new Dimension(80, 25));
            spinner.setAlignmentX(Component.CENTER_ALIGNMENT);
            spinners.add(spinner);
            panel.add(spinner);

            produtosPanel.add(panel);
        }

        telaPrincipal.add(produtosPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton calcularButton = new JButton("Calcular Total");
        calcularButton.addActionListener(e -> calcularTotal());
        bottomPanel.add(calcularButton);
        telaPrincipal.add(bottomPanel, BorderLayout.SOUTH);

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
            System.out.println("Contas disponíveis:");
            for (Cliente conta : gerenciador.getContas().values()) {
                System.out.println("Nome: " + conta.getNome() + ", Senha: " + conta.getSenha());
                if (conta.getNome().equals(nome) && conta.getSenha() == senha) {
                    contaAutenticada = conta;
                    System.out.println("Login bem sucedido para: " + nome);
                    abrirTelaPrincipal();
                    dispose();
                    return;
                }
            }
            
            System.out.println("Login falhou para: " + nome);
            JOptionPane.showMessageDialog(this, "Nome ou senha incorretos", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Senha inválida", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularTotal() {
        double total = 0;
        for (int i = 0; i < produtos.size(); i++) {
            Object produto = produtos.get(i);
            int quantidade = (Integer) spinners.get(i).getValue();
            
            if (produto instanceof Alimentos) {
                total += ((Alimentos) produto).getValor(quantidade);
            } else if (produto instanceof Eletronicos) {
                total += ((Eletronicos) produto).getValor(quantidade);
            }
        }
        JOptionPane.showMessageDialog(this, 
            String.format("Total da compra: R$ %.2f", total));
    }

    public static void main(String[] args) {
        GerenciadorDeConta gerenciador = new GerenciadorDeConta();
        System.out.println("Tentando carregar arquivo: C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/dados.csv");
        gerenciador.carregarContas("C:/Users/danie/Downloads/Trabalho/Trabalho/Trabalho/mercado-usb/Mercado/dados.csv");
        new Mercado(gerenciador);
    }
}