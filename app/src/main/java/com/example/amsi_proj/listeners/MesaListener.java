package com.example.amsi_proj.listeners;

import com.example.amsi_proj.modelo.Mesa;
import com.example.amsi_proj.modelo.Reserva;

import java.util.ArrayList;

public interface MesaListener {
    void onRefreshListaMesas(ArrayList<Mesa> listaMesas);
}
