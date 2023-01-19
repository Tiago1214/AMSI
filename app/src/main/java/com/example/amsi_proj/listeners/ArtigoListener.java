package com.example.amsi_proj.listeners;

import com.example.amsi_proj.modelo.Artigo;

import java.util.ArrayList;

public interface ArtigoListener {
    void onRefreshListaArtigos(ArrayList<Artigo> listaArtigos);
}
