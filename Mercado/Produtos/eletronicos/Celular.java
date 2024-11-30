package Produtos.eletronicos;

import Produtos.interfaces.Eletronicos;

public class Celular implements Eletronicos {
    private double preco = 1200.00;

    @Override
    public double getValor(int quantidade) {
        return preco * quantidade;
    }

    @Override
    public String getNome() {
        return "Produtos.eletronicos.Celular";
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}