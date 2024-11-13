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
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(",");

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

    public void salvarContas(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Cliente conta : contas.values()) {
                bw.write(conta.getId() + "," + conta.getNome() + "," +
                        (conta.isPremium() ? "1" : "0") + "," + conta.getSenha() + "," +
                        conta.getPontos());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    public void exibirContas() {
        for (Integer id : contas.keySet()) {
            System.out.println("ID: " + id + " - " + contas.get(id));
        }
    }

    public static void main(String[] args) {
        GerenciadorDeConta gerenciador = new GerenciadorDeConta();
        gerenciador.carregarContas("C:/Users/prodi/OneDrive/Documentos/Trabalho/mercado-usb/Mercado/dados.csv");
        gerenciador.exibirContas();
    }
}
