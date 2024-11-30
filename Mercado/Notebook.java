public class Notebook implements Eletronicos {
    private double preco = 6000.00;

    @Override
    public double getValor(int quantidade) {
        return preco * quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String getNome() {
        return "Notebook";
    }
} 