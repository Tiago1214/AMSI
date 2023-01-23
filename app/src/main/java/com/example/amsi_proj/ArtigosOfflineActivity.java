package com.example.amsi_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.amsi_proj.adaptadores.ListaArtigoAdaptador;
import com.example.amsi_proj.listeners.ArtigoListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.example.amsi_proj.utils.GersoftJsonParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ArtigosOfflineActivity extends AppCompatActivity implements ArtigoListener {

    private ListView lvArtigos;
    // private ArrayList<Artigo> artigos;
    private SearchView searchView;
    private FloatingActionButton fabInternet;
    public static final int ACT_DETALHES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //carregar vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artigos_offline);
        fabInternet=findViewById(R.id.fabInternet);
        lvArtigos = findViewById(R.id.lvArtigos);
        SingletonGersoft.getInstance(getApplicationContext()).setArtigosListener(this);
        SingletonGersoft.getInstance(getApplicationContext()).getAllArtigosAPI(getApplicationContext());

        //verificar se tem internet ou não quando carrega no botão
        fabInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!GersoftJsonParser.isConnectionInternet(getApplicationContext())){
                    Toast.makeText(ArtigosOfflineActivity.this, "Sem ligação á internet", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

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