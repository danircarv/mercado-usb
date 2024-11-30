import ExcecoesPersonalizadas.SenhaDuplicadaException;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GerenciadorDeConta {

    private HashMap<Integer, Cliente> contas = new HashMap<>();
    private Set<Integer> senhasUtilizadas = new HashSet<>();

    public HashMap<Integer, Cliente> getContas() {
        return contas;
    }

    public void setContas(HashMap<Integer, Cliente> contas) {
        this.contas = contas;
        senhasUtilizadas.clear();
        for (Cliente cliente : contas.values()) {
            senhasUtilizadas.add(cliente.getSenha());
        }
    }

    private boolean senhaDuplicada(int senha, int idAtual) {
        return senhasUtilizadas.contains(senha);
    }

    public void carregarContas(String filePath) {
        senhasUtilizadas.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(",");
                

                int id = Integer.parseInt(valores[0].trim());
                String nome = valores[1].trim();
                boolean isPremium = Integer.parseInt(valores[2].trim()) == 1;
                int senha = Integer.parseInt(valores[3].trim());
                int pontos = Integer.parseInt(valores[4].trim());

                try {
                    if (senhaDuplicada(senha, id)) {
                        throw new SenhaDuplicadaException("Não foi possível criar a conta para " + nome + ". A senha " + senha + " já está em uso.");
                    }

                    Cliente conta;
                    if (isPremium) {
                        conta = new ClientePremium(id, nome, isPremium, senha, pontos);
                    } else {
                        conta = new ClienteDefault(id, nome, isPremium, senha, pontos);
                    }

                    contas.put(id, conta);
                    senhasUtilizadas.add(senha);
                } catch (SenhaDuplicadaException e) {
                    System.out.println("Erro ao carregar conta: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter um valor: " + e.getMessage());
        }
    }

    public void salvarContas(String arquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            writer.println("id,nome,isPremium,senha,pontos"); // cabeçalho
            
            for (Cliente cliente : contas.values()) {
                int isPremium = cliente instanceof ClientePremium ? 1 : 0;
                writer.printf("%d,%s,%d,%d,%d%n",
                    cliente.getId(),
                    cliente.getNome(), 
                    isPremium,
                    cliente.getSenha(), 
                    cliente.getPontos());
            }
            System.out.println("Contas salvas com sucesso em: " + arquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar as contas: " + e.getMessage());
        }
    }

}
