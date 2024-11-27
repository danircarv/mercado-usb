abstract class Cliente {
    private int id;
    private String nome;
    private boolean isPremium;
    private int senha;
    private int pontos;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente(int id, String nome, boolean isPremium, int senha, int pontos) {
        this.id = id;
        this.nome = nome;
        this.isPremium = isPremium;
        this.senha = senha;
        this.pontos = pontos;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", isPremium=" + isPremium +
                ", senha=" + senha +
                ", pontos=" + pontos +
                '}';
    }

    public void adicionarPontos(int pontos) {
        this.pontos += pontos;
    }
}