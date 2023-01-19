package com.example.amsi_proj.modelo;

public class Artigo {
    private int id,iva_id,categoria_id;
    private double preco;
    private String nome,descricao,referencia,imagemurl;

    public Artigo(int id,String nome,String descricao,String referencia,double preco,String imagemurl,int iva_id,int categoria_id){
        this.id=id;
        this.nome=nome;
        this.descricao=descricao;
        this.referencia=referencia;
        this.preco=preco;
        this.imagemurl=imagemurl;
        this.iva_id=iva_id;
        this.categoria_id=categoria_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getReferencia() {
        return referencia;
    }

    public double getPreco() {
        return preco;
    }

    public String getImagemurl() {
        return imagemurl;
    }

    public int getIva_id() {
        return iva_id;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public CharSequence toLowerCase() {
        throw new RuntimeException("Stub!");
    }
}
