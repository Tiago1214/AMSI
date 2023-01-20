package com.example.amsi_proj.modelo;

public class Iva {
    private int id,taxaiva;
    private String descricao;

    public Iva(int id, int taxaiva, String descricao) {
        this.id = id;
        this.taxaiva = taxaiva;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaxaiva() {
        return taxaiva;
    }

    public void setTaxaiva(int taxaiva) {
        this.taxaiva = taxaiva;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
