package com.example.amsi_proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.amsi_proj.adaptadores.ListaArtigoAdaptador;
import com.example.amsi_proj.listeners.ArtigoListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ListaArtigosFragment extends Fragment implements ArtigoListener {

    private ListView lvArtigos;
   // private ArrayList<Artigo> artigos;
    private FloatingActionButton fabLista;
    private SearchView searchView;
    public static final int ACT_DETALHES = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_artigos, container, false);
        setHasOptionsMenu(true);
        fabLista=view.findViewById(R.id.fabLista);
        lvArtigos = view.findViewById(R.id.lvArtigos);

        lvArtigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesArtigoActivity.class);
                intent.putExtra("ID_ARTIGO", (int) id);
                startActivityForResult(intent, ACT_DETALHES);
            }
        });

        fabLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetalhesArtigoActivity.class);
                startActivityForResult(intent, ACT_DETALHES);
            }
        });
        SingletonGersoft.getInstance(getContext()).setArtigosListener(this);
        SingletonGersoft.getInstance(getContext()).getAllArtigosAPI(getContext());
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode== Activity.RESULT_OK && requestCode==ACT_DETALHES){
           SingletonGersoft.getInstance(getContext()).getAllArtigosAPI(getContext()); // quando sai dos detalhes e volta a lsta

            switch (intent.getIntExtra(MenuMainActivity.OPERACAO, 0)){
                case MenuMainActivity.ADD:
                    Toast.makeText(getContext(), "Livro adicionado", Toast.LENGTH_SHORT).show();
                    break;
                case MenuMainActivity.EDIT:
                    Toast.makeText(getContext(), "Livro editado", Toast.LENGTH_SHORT).show();
                    break;
                case MenuMainActivity.DELETE:
                    Toast.makeText(getContext(), "Livro eliminado", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { // pesquisa de aulas projeto
        inflater.inflate(R.menu.menu_pesquisa,menu);
        MenuItem itemPesquisa = menu.findItem(R.id.itemPesquisa);
        searchView = (SearchView) itemPesquisa.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Artigo> tempLista = new ArrayList<>();
                for(Artigo a: SingletonGersoft.getInstance(getContext()).getArtigosBD())
                {
                    if (a.getDescricao().toLowerCase().contains(a.toLowerCase()))
                    {
                        tempLista.add(a);
                    }
                    lvArtigos.setAdapter(new ListaArtigoAdaptador(getContext(), tempLista));
                }
                return true;

            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
    //


    @Override
    public void onResume() {
        if (searchView!=null){
            searchView.onActionViewCollapsed();
        }
        super.onResume();
    }


    @Override
    public void onRefreshListaArtigos(ArrayList<Artigo> listaArtigos) {
        if(listaArtigos != null){
            lvArtigos.setAdapter(new ListaArtigoAdaptador(getContext(), listaArtigos));
        }
    }
}