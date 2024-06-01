package com.example.mystock;

import java.util.Comparator;

public class Produto /*implements Comparable*/{

    public static Comparator<Produto> comparatorApiUnder24 = new Comparator<Produto>() {
        @Override
        public int compare(Produto p1, Produto p2) {
            int categoryComparator = p1.getCategoria().compareTo(p2.getCategoria());
            if (categoryComparator != 0) {
                return categoryComparator;
            }

            return p1.getNome().compareTo(p2.getNome());
        }
    };

    private String nome;
    private int quantidade;
    private boolean importante;
    private Criticidade criticidade;
    private Categoria categoria;



    public Produto() {
    }

    public Produto(String nome, int quantidade, boolean importante, Criticidade criticidade, Categoria categoria) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.importante = importante;
        if (importante){
            this.criticidade = criticidade;
        } else {
            this.criticidade = Criticidade.Nenhuma;
        }

        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isImportante() {
        return importante;
    }

    public void setImportante(boolean importante) {
        this.importante = importante;
    }

    public Criticidade getCriticidade() {
        return importante ? criticidade : Criticidade.Nenhuma;
    }

    public void setCriticidade(Criticidade criticidade) {
        if (importante){
            this.criticidade = criticidade;
        } else {
            this.criticidade = Criticidade.Nenhuma;
        }
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", importante=" + importante +
                ", criticidade=" + criticidade +
                ", categoria=" + categoria +
                '}';
    }


//    @Override
//    public int compareTo(Produto p) {
//        return Comparator.comparing(Produto::getCategoria)
//                .thenComparing(Produto::getNome)
//                .compare(this, p);
//    }
}
