package com.example.amsi_proj;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.amsi_proj.adaptadores.ListaArtigoAdaptador;
import com.example.amsi_proj.adaptadores.ListaReservaAdaptador;
import com.example.amsi_proj.listeners.ReservaListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Reserva;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ReservaFragment extends Fragment implements ReservaListener {

    private ListView lvReservas;
    // private ArrayList<Artigo> artigos;
    private SearchView searchView;
    private FloatingActionButton fabLista;
    public static final int ACT_DETALHES = 1;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva, container, false);
        setHasOptionsMenu(true);
        lvReservas = view.findViewById(R.id.list_Reservas);
        fabLista=view.findViewById(R.id.floating_AdicionarReserva);

        lvReservas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesReservaActivity.class);
                intent.putExtra("ID_RESERVA", (int) id);
                startActivity(intent);
            }
        });

        fabLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetalhesReservaActivity.class);
                startActivityForResult(intent, ACT_DETALHES);
            }
        });
        SingletonGersoft.getInstance(getContext()).setReservaListener(this);
        SingletonGersoft.getInstance(getContext()).getAllReservasAPI(getContext());
        return view;
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
                ArrayList<Reserva> tempLista = new ArrayList<>();
                ArrayList<Reserva> teste = SingletonGersoft.getInstance(getContext()).getReservasBD();
                for(Reserva r: teste)
                {
                    if (r.getData().toLowerCase().contains(s.toLowerCase()))
                    {
                        tempLista.add(r);
                    }
                    lvReservas.setAdapter(new ListaReservaAdaptador(getContext(), tempLista));
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
    public void onRefreshListaReservas(ArrayList<Reserva> listaReservas) {
        if(listaReservas != null){
            lvReservas.setAdapter(new ListaReservaAdaptador(getContext(), listaReservas));
        }
    }
}