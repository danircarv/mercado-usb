public class Notebook implements Eletronicos {
    private double preco = 3500.00;

    @Override
    public double getValor(int quantidade) {
        return preco * quantidade;
    }

    @Override
    public String getNome() {
        return "Notebook";
    }
} 