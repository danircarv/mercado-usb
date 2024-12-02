package Produtos.alimentos;

import Produtos.interfaces.Alimentos;

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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}