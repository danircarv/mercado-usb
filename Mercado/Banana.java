public class Banana implements Alimentos {
    private double preco = 3.50;

    @Override
    public double getValor(int quantidade) {
        return preco * quantidade;
    }

    @Override
    public String getNome() {
        return "Banana";
    }
} 