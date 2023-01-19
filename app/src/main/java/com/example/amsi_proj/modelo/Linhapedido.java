package com.example.amsi_proj.modelo;

public class Linhapedido {

    private int id,quantidade,taxaiva,pedido_id,artigo_id;
    private double valorunitario,valoriva;

    public Linhapedido(int id, int quantidade, int taxaiva, int pedido_id,
                       int artigo_id, double valorunitario, double valoriva) {
        this.id = id;
        this.quantidade = quantidade;
        this.taxaiva = taxaiva;
        this.pedido_id = pedido_id;
        this.artigo_id = artigo_id;
        this.valorunitario = valorunitario;
        this.valoriva = valoriva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getTaxaiva() {
        return taxaiva;
    }

    public void setTaxaiva(int taxaiva) {
        this.taxaiva = taxaiva;
    }

    public int getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(int pedido_id) {
        this.pedido_id = pedido_id;
    }

    public int getArtigo_id() {
        return artigo_id;
    }

    public void setArtigo_id(int artigo_id) {
        this.artigo_id = artigo_id;
    }

    public double getValorunitario() {
        return valorunitario;
    }

    public void setValorunitario(double valorunitario) {
        this.valorunitario = valorunitario;
    }

    public double getValoriva() {
        return valoriva;
    }

    public void setValoriva(double valoriva) {
        this.valoriva = valoriva;
    }
}
