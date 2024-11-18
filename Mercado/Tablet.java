public class Tablet implements Eletronicos {
    private double preco = 800.00;

    @Override
    public double getValor(int quantidade) {
        return preco * quantidade;
    }

    @Override
    public String getNome() {
        return "Tablet";
    }
} 