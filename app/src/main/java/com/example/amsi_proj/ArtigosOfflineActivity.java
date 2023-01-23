package com.example.amsi_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.amsi_proj.adaptadores.ListaArtigoAdaptador;
import com.example.amsi_proj.listeners.ArtigoListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.SingletonGersoft;

import java.util.ArrayList;

public class ArtigosOfflineActivity extends AppCompatActivity implements ArtigoListener {

    private ListView lvArtigos;
    // private ArrayList<Artigo> artigos;
    private SearchView searchView;
    public static final int ACT_DETALHES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artigos_offline);

        lvArtigos = findViewById(R.id.lvArtigos);
        SingletonGersoft.getInstance(getApplicationContext()).setArtigosListener(this);
        SingletonGersoft.getInstance(getApplicationContext()).getArtigosBD();

    }

    @Override
    public void onRefreshListaArtigos(ArrayList<Artigo> listaArtigos) {
        if(listaArtigos != null){
            lvArtigos.setAdapter(new ListaArtigoAdaptador(getApplicationContext(), listaArtigos));
        }

    }

    @Override
    public void onResume() {
        if (searchView!=null){
            searchView.onActionViewCollapsed();
        }
        super.onResume();
    }
}