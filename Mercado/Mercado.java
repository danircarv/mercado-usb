import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Mercado extends JFrame {
    private GerenciadorDeConta gerenciador;
    private JTextField nomeField;
    private JPasswordField senhaField;
    private Cliente contaAutenticada;
    private int quantidadeAbacate = 0; // Quantidade inicial de abacate
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

        // Inicializa produtos
        produtos.add(new Abacate());
        produtos.add(new Banana());
        produtos.add(new Maca());
        produtos.add(new Celular());
        produtos.add(new Notebook());
        produtos.add(new Tablet());

        // Adiciona os produtos ao grid
        for (Object produto : produtos) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Usa a mesma imagem do abacate para todos os produtos
            ImageIcon icon = new ImageIcon("caminho/para/imagem/abacate.png");
            JLabel imageLabel = new JLabel(icon);
            panel.add(imageLabel);

            // Adiciona o nome do produto
            String nome = "";
            if (produto instanceof Alimentos) {
                nome = ((Alimentos) produto).getNome();
            } else if (produto instanceof Eletronicos) {
                nome = ((Eletronicos) produto).getNome();
            }
            JLabel nameLabel = new JLabel(nome);
            panel.add(nameLabel);

            // Adiciona o spinner para quantidade
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            spinners.add(spinner);
            panel.add(spinner);

            add(panel);
        }

        // Adiciona botão de calcular
        JButton calcularButton = new JButton("Calcular Total");
        calcularButton.addActionListener(e -> calcularTotal());
        add(calcularButton);

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
            // Primeiro, vamos imprimir as contas disponíveis para debug
            System.out.println("Contas disponíveis:");
            for (Cliente conta : gerenciador.getContas().values()) {
                System.out.println("Nome: " + conta.getNome() + ", Senha: " + conta.getSenha());
            }
            
            // Agora vamos imprimir as credenciais tentadas
            System.out.println("Tentativa de login - Nome: " + nome + ", Senha: " + senha);

            Cliente conta = gerenciador.getContas().get(nome);
            if (conta != null && conta.getSenha() == senha) {
                contaAutenticada = conta;
                abrirTelaPrincipal();
                dispose();
                return;
            }
            
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
        // Imprime o caminho absoluto para verificar se está correto
        System.out.println("Tentando carregar arquivo: C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/dados.csv");
        gerenciador.carregarContas("C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/dados.csv");
        new Mercado(gerenciador);
    }
}