public class Abacate implements Alimentos {
    private double preco = 25.98;
    @Override
    public double getValor(int qtd) {
        return qtd * preco;
    }
    @Override
    public String getNome() {
        return "Abacate";
    }
}
