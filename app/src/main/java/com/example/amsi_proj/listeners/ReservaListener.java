package com.example.amsi_proj.listeners;

import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Reserva;

import java.util.ArrayList;

public interface ReservaListener {
    void onRefreshListaReservas(ArrayList<Reserva> listaReservas);
}
