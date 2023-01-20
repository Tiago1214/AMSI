package com.example.amsi_proj.modelo;

public class Metodopagamento {

    private int id;
    private String nomepagamento;

    public Metodopagamento(int id, String nomepagamento) {
        this.id = id;
        this.nomepagamento = nomepagamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomepagamento() {
        return nomepagamento;
    }

}
