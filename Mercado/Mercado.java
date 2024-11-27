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
    private Carrinho carrinho;

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
        JFrame telaPrincipal = new JFrame("Mercados USB");
        telaPrincipal.setSize(1080, 720);
        telaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaPrincipal.setLocationRelativeTo(null);
        telaPrincipal.setLayout(new BorderLayout());

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

        carrinhoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        carrinhoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirTelaCarrinho();
            }
        });

        int pontos = contaAutenticada.getPontos();
        JLabel pontosLabel = new JLabel("Pontos: " + pontos);
        pontosLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel carrinhoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        carrinhoPanel.add(carrinhoLabel);
        carrinhoPanel.add(pontosLabel);
        headerPanel.add(carrinhoPanel, BorderLayout.EAST);

        telaPrincipal.add(headerPanel, BorderLayout.NORTH);

        JPanel produtosPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        produtosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        produtos.clear();
        produtos.add(new Abacate());
        produtos.add(new Banana());
        produtos.add(new Maca());
        produtos.add(new Celular());
        produtos.add(new Notebook());
        produtos.add(new Tablet());

        // Inicializa o carrinho
        carrinho = new Carrinho(contaAutenticada, produtos);

        spinners.clear();

        String[] imagensNomes = {"abacate", "banana", "maca", "celular", "notebook", "tablet"};

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

        telaPrincipal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                carrinho.atualizarQuantidades(spinners);
                carrinho.imprimirRecibo();
            }
        });

        telaPrincipal.setVisible(true);
    }

    private void abrirTelaCarrinho() {
        JFrame telaCarrinho = new JFrame("Carrinho de Compras");
        telaCarrinho.setSize(600, 400);
        telaCarrinho.setLocationRelativeTo(null);
        telaCarrinho.setLayout(new BorderLayout());

        // Painel superior com botão voltar
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton voltarButton = new JButton("Voltar ao Mercado");
        voltarButton.addActionListener(e -> telaCarrinho.dispose());
        headerPanel.add(voltarButton, BorderLayout.EAST);
        telaCarrinho.add(headerPanel, BorderLayout.NORTH);

        // Painel central com lista de produtos
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Título da lista
        JLabel tituloLabel = new JLabel("Itens no Carrinho");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        listPanel.add(tituloLabel);
        listPanel.add(Box.createVerticalStrut(20));

        double totalAtual = 0.0;
        
        // Adiciona os itens à lista
        for (int i = 0; i < produtos.size(); i++) {
            int quantidade = (int) spinners.get(i).getValue();
            if (quantidade > 0) {
                Object produto = produtos.get(i);
                String nome = "";
                double preco = 0.0;
                
                // Verifica o tipo específico do produto
                if (produto instanceof Abacate) {
                    nome = ((Abacate) produto).getNome();
                    preco = ((Abacate) produto).getPreco() * quantidade;
                } else if (produto instanceof Maca) {
                    nome = ((Maca) produto).getNome();
                    preco = ((Maca) produto).getPreco() * quantidade;
                } else if (produto instanceof Banana) {
                    nome = ((Banana) produto).getNome();
                    preco = ((Banana) produto).getPreco() * quantidade;
                } else if (produto instanceof Celular) {
                    nome = ((Celular) produto).getNome();
                    preco = ((Celular) produto).getPreco() * quantidade;
                } else if (produto instanceof Notebook) {
                    nome = ((Notebook) produto).getNome();
                    preco = ((Notebook) produto).getPreco() * quantidade;
                } else if (produto instanceof Tablet) {
                    nome = ((Tablet) produto).getNome();
                    preco = ((Tablet) produto).getPreco() * quantidade;
                }
                
                totalAtual += preco;
                
                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                itemPanel.setMaximumSize(new Dimension(500, 30));
                JLabel itemLabel = new JLabel(String.format("%s - Quantidade: %d - R$ %.2f", 
                    nome, quantidade, preco));
                itemPanel.add(itemLabel);
                listPanel.add(itemPanel);
            }
        }

        // Adiciona o total
        listPanel.add(Box.createVerticalStrut(20));
        JLabel totalLabel = new JLabel(String.format("Total: R$ %.2f", totalAtual));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        listPanel.add(totalLabel);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        telaCarrinho.add(scrollPane, BorderLayout.CENTER);

        // Painel inferior com botão de concluir
        JPanel bottomPanel = new JPanel();
        JButton concluirButton = new JButton("Concluir Compra");
        concluirButton.addActionListener(e -> {
            calcularTotal();
            System.out.println("Compra Finalizada!");
            System.exit(0);
        });
        bottomPanel.add(concluirButton);
        telaCarrinho.add(bottomPanel, BorderLayout.SOUTH);

        telaCarrinho.setVisible(true);
    }

    private void autenticarUsuario() {
        String nome = nomeField.getText().trim();
        String senhaTexto = new String(senhaField.getPassword()).trim();

        try {
            int senha = Integer.parseInt(senhaTexto);
            for (Cliente conta : gerenciador.getContas().values()) {
                if (conta.getNome().equals(nome) && conta.getSenha() == senha) {
                    contaAutenticada = conta;
                    System.out.println("Login bem sucedido para: " + nome);
                    abrirTelaMenu();
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

    private void abrirTelaMenu() {
        JFrame telaMenu = new JFrame("Menu Principal - Mercado USB");
        telaMenu.setSize(600, 400);
        telaMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaMenu.setLocationRelativeTo(null);
        telaMenu.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(600, 50));

        String nomeCliente = contaAutenticada.getNome();
        JLabel nomeClienteLabel = new JLabel("Bem-vindo, " + nomeCliente);
        nomeClienteLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(nomeClienteLabel, BorderLayout.WEST);

        JLabel pontosLabel = new JLabel("Pontos: " + contaAutenticada.getPontos());
        pontosLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(pontosLabel, BorderLayout.EAST);

        telaMenu.add(headerPanel, BorderLayout.NORTH);

        // Botões centralizados
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        JButton comprasButton = new JButton("Ir às Compras");
        comprasButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        comprasButton.setMaximumSize(new Dimension(200, 40));
        comprasButton.addActionListener(e -> {
            abrirTelaPrincipal();
            telaMenu.dispose();
        });

        JButton pontosButton = new JButton("Trocar Pontos");
        pontosButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pontosButton.setMaximumSize(new Dimension(200, 40));
        pontosButton.setBackground(Color.ORANGE);
        pontosButton.setForeground(Color.WHITE);
        pontosButton.addActionListener(e -> abrirTelaPremios());

        centerPanel.add(comprasButton);
        centerPanel.add(Box.createVerticalStrut(20)); // Espaço entre os botões
        centerPanel.add(pontosButton);

        telaMenu.add(centerPanel, BorderLayout.CENTER);
        telaMenu.setVisible(true);
    }

    private void abrirTelaPremios() {
        JFrame telaPremios = new JFrame("Prêmios - Troque seus pontos");
        telaPremios.setSize(800, 600);
        telaPremios.setLocationRelativeTo(null);
        telaPremios.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel pontosLabel = new JLabel("Seus pontos: " + contaAutenticada.getPontos());
        pontosLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(pontosLabel, BorderLayout.CENTER);
        telaPremios.add(headerPanel, BorderLayout.NORTH);

        // Painel de prêmios
        JPanel premiosPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        premiosPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Array com informações dos prêmios
        String[][] premios = {
            {"ferrari.png", "Ferrari", "1000000"},
            {"aviao.png", "Avião Particular", "2000000"},
            {"hotel.png", "3 Diárias em Hotel 5 Estrelas", "50000"}
        };

        for (String[] premio : premios) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            
            // Carregar e redimensionar imagem
            ImageIcon icon = new ImageIcon("C:/Users/danie/Downloads/Trabalho/Trabalho/Trabalho/mercado-usb/Mercado/" + premio[0]);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(img));
            
            JLabel descLabel = new JLabel(premio[1] + " - " + premio[2] + " pontos");
            descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JButton resgatarButton = new JButton("Resgatar");
            int pontosNecessarios = Integer.parseInt(premio[2]);
            
            resgatarButton.addActionListener(e -> {
                if (contaAutenticada.getPontos() >= pontosNecessarios) {
                    int opcao = JOptionPane.showConfirmDialog(telaPremios,
                        "Deseja resgatar " + premio[1] + " por " + premio[2] + " pontos?",
                        "Confirmar Resgate",
                        JOptionPane.YES_NO_OPTION);
                        
                    if (opcao == JOptionPane.YES_OPTION) {
                        contaAutenticada.setPontos(contaAutenticada.getPontos() - pontosNecessarios);
                        gerenciador.salvarContas("C:/Users/danie/Downloads/Trabalho/Trabalho/Trabalho/mercado-usb/Mercado/dados.csv");
                        
                        JOptionPane.showMessageDialog(telaPremios,
                            "Procure um mercado USB para retirar seu prêmio!\nPontos restantes: " + contaAutenticada.getPontos(),
                            "Prêmio Reservado",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Atualiza o label de pontos
                        pontosLabel.setText("Seus pontos: " + contaAutenticada.getPontos());
                    }
                } else {
                    JOptionPane.showMessageDialog(telaPremios,
                        "Pontos insuficientes!\nVocê tem: " + contaAutenticada.getPontos() + " pontos\nNecessário: " + pontosNecessarios + " pontos",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            });

            // Desabilita o botão se não houver pontos suficientes
            if (contaAutenticada.getPontos() < pontosNecessarios) {
                resgatarButton.setEnabled(false);
                resgatarButton.setToolTipText("Pontos insuficientes");
            }

            itemPanel.add(imageLabel);
            itemPanel.add(descLabel);
            itemPanel.add(resgatarButton);
            
            premiosPanel.add(itemPanel);
        }

        JScrollPane scrollPane = new JScrollPane(premiosPanel);
        telaPremios.add(scrollPane, BorderLayout.CENTER);

        // Botão voltar
        JButton voltarButton = new JButton("Voltar ao Menu");
        voltarButton.addActionListener(e -> telaPremios.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(voltarButton);
        telaPremios.add(bottomPanel, BorderLayout.SOUTH);

        telaPremios.setVisible(true);
    }

    private void calcularTotal() {
        carrinho.atualizarQuantidades(spinners);
        double total = carrinho.getValorTotal();
        carrinho.imprimirRecibo();
        
        // Atualiza os pontos no gerenciador após a compra
        contaAutenticada.adicionarPontos((int)total);
        gerenciador.salvarContas("C:/Users/danie/Downloads/Trabalho/Trabalho/Trabalho/mercado-usb/Mercado/dados.csv");
        
        JOptionPane.showMessageDialog(this, 
            String.format("Total da compra: R$ %.2f\nPontos acumulados nesta compra: %d\nTotal de pontos: %d", 
            total, (int)total, contaAutenticada.getPontos()));
    }

    public static void main(String[] args) {
        GerenciadorDeConta gerenciador = new GerenciadorDeConta();
        System.out.println("Tentando carregar arquivo: C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/dados.csv");
        gerenciador.carregarContas("C:/Users/danie/Downloads/Trabalho/Trabalho/Trabalho/mercado-usb/Mercado/dados.csv");
        new Mercado(gerenciador);
    }
}