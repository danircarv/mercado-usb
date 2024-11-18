public class Maca implements Alimentos {
    private double preco = 2.75;

    @Override
    public double getValor(int quantidade) {
        return preco * quantidade;
    }

    @Override
    public String getNome() {
        return "Maçã";
    }
} 