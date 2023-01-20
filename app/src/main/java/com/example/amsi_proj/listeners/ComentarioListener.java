package com.example.amsi_proj.listeners;

import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Comentario;

import java.util.ArrayList;

public interface ComentarioListener {
    void onRefreshListaComentarios(ArrayList<Comentario> listaComentarios);
}
