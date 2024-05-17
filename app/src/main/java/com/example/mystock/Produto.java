package com.example.mystock;

public class Produto {

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
        this.criticidade = criticidade;
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
        return criticidade;
    }

    public void setCriticidade(Criticidade criticidade) {
        this.criticidade = criticidade;
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
}
