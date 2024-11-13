public class Abacate implements Produtos{
    private double preco = 25.98;
    @Override
    public double getValor(int qtd) {
        return qtd * preco;
    }
}
