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

import com.example.amsi_proj.adaptadores.ListaPedidoAdaptador;
import com.example.amsi_proj.listeners.PedidoListener;
import com.example.amsi_proj.modelo.Pedido;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class PedidosFragment extends Fragment implements PedidoListener{

    private ListView lvPedidos;
    private ArrayList<Pedido> pedidos;
    private SearchView searchView;
    private FloatingActionButton fabLista;
    public static final int ACT_DETALHES = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Atribuir os valores á lista
        onRefreshListaPedidos(pedidos);
        //Definir a vista
        View view = inflater.inflate(R.layout.fragment_pedidos_concluidos, container, false);
        setHasOptionsMenu(true);
        lvPedidos = view.findViewById(R.id.list_Pedidos);

        //Visualizar pedido concluído
        lvPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), VisualizarPedidoConcluidoActivity.class);
                intent.putExtra("ID_PEDIDO", (int) id);
                //onactivityresult
                startActivityForResult(intent,ACT_DETALHES);
            }
        });

        //Definir o listener do pedido
        SingletonGersoft.getInstance(getContext()).setPedidoListener(this);

        //Pedir todos os pedidos concluídos á api
        SingletonGersoft.getInstance(getContext()).getAllPedidosConcluidosAPI(getContext());
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode== Activity.RESULT_OK && requestCode==ACT_DETALHES){
            SingletonGersoft.getInstance(getContext()).getAllPedidosConcluidosAPI(getContext()); // quando sai dos detalhes e volta a lsta
            onResume();
            switch (intent.getIntExtra(MenuMainActivity.OPERACAO, 0)){
                case MenuMainActivity.ADD:
                    Toast.makeText(getContext(), "Pedido adicionado", Toast.LENGTH_SHORT).show();
                    break;
                case MenuMainActivity.EDIT:
                    Toast.makeText(getContext(), "Pedido editado", Toast.LENGTH_SHORT).show();
                    break;
                case MenuMainActivity.DELETE:
                    Toast.makeText(getContext(), "Pedido eliminado", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    //Barra de Pesquisa
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
                ArrayList<Pedido> tempLista = new ArrayList<>();
                ArrayList<Pedido> teste = SingletonGersoft.getInstance(getContext()).getPedidosDB();
                for(Pedido p: teste)
                {
                    if (p.getData().toLowerCase().contains(s.toLowerCase()))
                    {
                        tempLista.add(p);
                    }
                    lvPedidos.setAdapter(new ListaPedidoAdaptador(getContext(), tempLista));
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

    //Definir adaptador da lista de pedidos
    @Override
    public void onRefreshListaPedidos(ArrayList<Pedido> listaPedidos) {
        if(listaPedidos != null){
            lvPedidos.setAdapter(new ListaPedidoAdaptador(getContext(), listaPedidos));
        }
    }
}