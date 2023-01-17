package com.example.amsi_proj.modelo;
//package pt.ipleiria.estg.dei.comentario.modelo;

public class Comentario {

    private int id;
    private String data, nome, comentario;

    public Comentario(int id, String data, String nome, String comentario) {
        this.id = id;
        this.data = data;
        this.nome = nome;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {return data; }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String titulo) {
        this.nome = nome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


}
