public class ClienteDefault extends Cliente implements Operacoes {

    public ClienteDefault(int id, String nome, boolean isPremium, int senha, int pontos) {
        super(id, nome, isPremium, senha, pontos);
    }
}
