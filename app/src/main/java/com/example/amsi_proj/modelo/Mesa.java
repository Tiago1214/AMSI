package com.example.amsi_proj.modelo;

public class Mesa {
    private int id,nrmesa,nrlugares;
    private String tipomesa;

    public Mesa(int id, int nrmesa, int nrlugares, String tipomesa) {
        this.id = id;
        this.nrmesa = nrmesa;
        this.nrlugares = nrlugares;
        this.tipomesa = tipomesa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNrmesa() {
        return nrmesa;
    }

    public int getNrlugares() {
        return nrlugares;
    }

    public String getTipomesa() {
        return tipomesa;
    }

}
