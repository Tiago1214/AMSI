package com.example.amsi_proj.modelo;

public class Pedido {

    private int id,tipo_pedido,profile_id,metodo_pagamento_id,mesa_id;
    private String data,estado;
    private Double total;

    public Pedido(int id, int tipo_pedido, int profile_id, int metodo_pagamento_id
            , int mesa_id, String data, String estado, Double total) {
        this.id = id;
        this.tipo_pedido = tipo_pedido;
        this.profile_id = profile_id;
        this.metodo_pagamento_id = metodo_pagamento_id;
        this.mesa_id = mesa_id;
        this.data = data;
        this.estado = estado;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo_pedido() {
        return tipo_pedido;
    }

    public void setTipo_pedido(int tipo_pedido) {
        this.tipo_pedido = tipo_pedido;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public int getMetodo_pagamento_id() {
        return metodo_pagamento_id;
    }

    public void setMetodo_pagamento_id(int metodo_pagamento_id) {
        this.metodo_pagamento_id = metodo_pagamento_id;
    }

    public int getMesa_id() {
        return mesa_id;
    }

    public void setMesa_id(int mesa_id) {
        this.mesa_id = mesa_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
