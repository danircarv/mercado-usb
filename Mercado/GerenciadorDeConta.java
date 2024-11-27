import java.io.*;
import java.util.HashMap;

public class GerenciadorDeConta {

    private HashMap<Integer, Cliente> contas = new HashMap<>();

    public HashMap<Integer, Cliente> getContas() {
        return contas;
    }

    public void setContas(HashMap<Integer, Cliente> contas) {
        this.contas = contas;
    }

    public void carregarContas(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linha = br.readLine(); // Pula o cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(",");
                
                // Formato esperado: id,nome,isPremium,senha,pontos
                int id = Integer.parseInt(valores[0].trim());
                String nome = valores[1].trim();
                boolean isPremium = Integer.parseInt(valores[2].trim()) == 1;
                int senha = Integer.parseInt(valores[3].trim());
                int pontos = Integer.parseInt(valores[4].trim());

                Cliente conta;
                if (isPremium) {
                    conta = new ClientePremium(id, nome, isPremium, senha, pontos);
                } else {
                    conta = new ClienteDefault(id, nome, isPremium, senha, pontos);
                }

                contas.put(id, conta);
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

    public void exibirContas() {
        for (Integer id : contas.keySet()) {
            System.out.println("ID: " + id + " - " + contas.get(id));
        }
    }


}
