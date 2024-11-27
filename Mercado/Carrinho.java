import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JSpinner;

public class Carrinho {
    private Map<String, Integer> quantidades;
    private Map<String, Double> precos;
    private Cliente cliente;
    private ArrayList<Object> produtos;

    public Carrinho(Cliente cliente, ArrayList<Object> produtos) {
        this.cliente = cliente;
        this.produtos = produtos;
        this.quantidades = new HashMap<>();
        this.precos = new HashMap<>();
        inicializarMaps();
    }

    private void inicializarMaps() {
        for (Object produto : produtos) {
            String nome = "";
            if (produto instanceof Alimentos) {
                nome = ((Alimentos) produto).getNome();
            } else if (produto instanceof Eletronicos) {
                nome = ((Eletronicos) produto).getNome();
            }
            quantidades.put(nome, 0);
            precos.put(nome, getPrecoUnitario(produto));
        }
    }

    private double getPrecoUnitario(Object produto) {
        if (produto instanceof Alimentos) {
            return ((Alimentos) produto).getValor(1);
        } else if (produto instanceof Eletronicos) {
            return ((Eletronicos) produto).getValor(1);
        }
        return 0.0;
    }

    public void atualizarQuantidades(ArrayList<JSpinner> spinners) {
        for (int i = 0; i < produtos.size(); i++) {
            String nome = "";
            if (produtos.get(i) instanceof Alimentos) {
                nome = ((Alimentos) produtos.get(i)).getNome();
            } else if (produtos.get(i) instanceof Eletronicos) {
                nome = ((Eletronicos) produtos.get(i)).getNome();
            }
            quantidades.put(nome, (Integer) spinners.get(i).getValue());
        }
    }

    public void imprimirRecibo() {
        System.out.println("\n====== RECIBO DE COMPRA ======");
        System.out.println("Cliente: " + cliente.getNome());
        System.out.println("Tipo: " + (cliente instanceof ClientePremium ? "Premium" : "Default"));
        System.out.println("---------------------------");

        double subtotal = 0.0;

        for (String produto : quantidades.keySet()) {
            int qtd = quantidades.get(produto);
            if (qtd > 0) {
                double precoUnitario = precos.get(produto);
                double precoTotal = precoUnitario * qtd;
                subtotal += precoTotal;
                System.out.printf("%s: %d x R$ %.2f = R$ %.2f%n", 
                    produto, qtd, precoUnitario, precoTotal);
            }
        }

        System.out.println("---------------------------");
        System.out.printf("Subtotal: R$ %.2f%n", subtotal);

        double desconto = 0.0;
        if (cliente instanceof ClientePremium) {
            desconto = subtotal * 0.20;
            System.out.printf("Desconto (20%%): R$ %.2f%n", desconto);
        }

        double total = subtotal - desconto;
        System.out.printf("Total: R$ %.2f%n", total);

        // Calcula e adiciona pontos
        int pontosGanhos = (int) (total / 10.0);
        cliente.adicionarPontos(pontosGanhos);
        System.out.println("Pontos ganhos: " + pontosGanhos);
        System.out.println("Total de pontos: " + cliente.getPontos());
        System.out.println("============================\n");
    }

    public double getValorTotal() {
        double subtotal = 0.0;
        for (String produto : quantidades.keySet()) {
            subtotal += quantidades.get(produto) * precos.get(produto);
        }
        
        if (cliente instanceof ClientePremium) {
            return subtotal * 0.80; // 20% de desconto
        }
        return subtotal;
    }
}
