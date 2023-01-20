package com.example.amsi_proj.modelo;

public class Reserva {
    private int id,nrpessoas,estado,profile_id;
    private String data,hora;

    public Reserva(int id, int nrpessoas, int estado, int profile_id, String data, String hora) {
        this.id = id;
        this.nrpessoas = nrpessoas;
        this.estado = estado;
        this.profile_id = profile_id;
        this.data = data;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNrpessoas() {
        return nrpessoas;
    }

    public void setNrpessoas(int nrpessoas) {
        this.nrpessoas = nrpessoas;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
