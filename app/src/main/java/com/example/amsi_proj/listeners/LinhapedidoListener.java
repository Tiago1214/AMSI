package com.example.amsi_proj.listeners;

import com.example.amsi_proj.modelo.Linhapedido;
import com.example.amsi_proj.modelo.Mesa;

import java.util.ArrayList;

public interface LinhapedidoListener {
    void onRefreshListaLinhapedidos(ArrayList<Linhapedido> listaLinhaspedido);
}
