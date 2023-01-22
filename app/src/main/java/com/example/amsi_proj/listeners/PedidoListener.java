package com.example.amsi_proj.listeners;

import com.example.amsi_proj.modelo.Pedido;
import com.example.amsi_proj.modelo.Reserva;

import java.util.ArrayList;

public interface PedidoListener {
    void onRefreshListaPedidos(ArrayList<Pedido> listaPedidos);
}
