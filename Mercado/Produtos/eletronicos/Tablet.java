package Produtos.eletronicos;

import Produtos.interfaces.Eletronicos;

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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}