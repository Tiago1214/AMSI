package com.example.amsi_proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.example.amsi_proj.DetalhesComentarioActivity;
import com.example.amsi_proj.adaptadores.ListaComentarioAdaptador;
import com.example.amsi_proj.listeners.ComentarioListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Comentario;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ComentariosFragment extends Fragment implements ComentarioListener {

    private ListView lvComentarios;
    // private ArrayList<Livro> livros;
    private FloatingActionButton fabLista;
    private SearchView searchView;
    public static final int ACT_DETALHES = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Definir vista
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);
        setHasOptionsMenu(true);
        fabLista=view.findViewById(R.id.floating_AdicionarComentario);
        lvComentarios = view.findViewById(R.id.list_Comentarios);

        //carregar editar
        lvComentarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesComentarioActivity.class);
                intent.putExtra("ID_COMENTARIO", (int) id);
                startActivityForResult(intent, ACT_DETALHES);
            }
        });

        //criar novo comentario
        fabLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetalhesComentarioActivity.class);
                startActivityForResult(intent, ACT_DETALHES);
            }
        });
        SingletonGersoft.getInstance(getContext()).setComentariosListener(this);
        SingletonGersoft.getInstance(getContext()).getAllComentariosAPI(getContext());
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode== Activity.RESULT_OK && requestCode==ACT_DETALHES){
            SingletonGersoft.getInstance(getContext()).getAllComentariosAPI(getContext()); // quando sai dos detalhes e volta a lsta

            switch (intent.getIntExtra(MenuMainActivity.OPERACAO, 0)){
                case MenuMainActivity.ADD:
                    Toast.makeText(getContext(), "Comentario adicionado", Toast.LENGTH_SHORT).show();
                    break;
                case MenuMainActivity.EDIT:
                    Toast.makeText(getContext(), "Comentario editado", Toast.LENGTH_SHORT).show();
                    break;
                case MenuMainActivity.DELETE:
                    Toast.makeText(getContext(), "Comentario eliminado", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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
                ArrayList<Comentario> tempLista = new ArrayList<>();
                ArrayList<Comentario> teste = SingletonGersoft.getInstance(getContext()).getComentariosDB();
                for(Comentario c: teste)
                {
                    if (c.getTitulo().toLowerCase().contains(s.toLowerCase()))
                    {
                        tempLista.add(c);
                    }
                    lvComentarios.setAdapter(new ListaComentarioAdaptador(getContext(), tempLista));
                }
                return true;

            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        if (searchView!=null){
            searchView.onActionViewCollapsed();
        }
        super.onResume();
    }

    //carregar adaptador da lista de comentários
    @Override
    public void onRefreshListaComentarios(ArrayList<Comentario> listaComentarios) {
        if(listaComentarios != null){
            lvComentarios.setAdapter(new ListaComentarioAdaptador(getContext(), listaComentarios));
        }

    }
}