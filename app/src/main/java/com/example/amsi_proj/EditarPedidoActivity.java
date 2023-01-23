package com.example.amsi_proj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.amsi_proj.adaptadores.ListaLinhapedidoAdaptador;
import com.example.amsi_proj.adaptadores.ListaReservaAdaptador;
import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.listeners.LinhapedidoListener;
import com.example.amsi_proj.modelo.Comentario;
import com.example.amsi_proj.modelo.Linhapedido;
import com.example.amsi_proj.modelo.Reserva;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditarPedidoActivity extends AppCompatActivity implements LinhapedidoListener {

    private ListView lvLinhaspedido;
    private ArrayList<Linhapedido> linhapedidos;
    private SearchView searchView;
    private FloatingActionButton fabLista;
    public static final int ACT_DETALHES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onRefreshListaLinhapedidos(linhapedidos);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);
        lvLinhaspedido = findViewById(R.id.lvLinhasPedido);
        int id=getIntent().getIntExtra("ID_PEDIDO", 0);
        SingletonGersoft.getInstance(getApplicationContext()).setLinhapedidoListener(this);
        SingletonGersoft.getInstance(getApplicationContext()).getAllLinhaspedidoAPI(getApplicationContext(),id);
    }

    @Override
    public void onResume() {
        if (searchView!=null){
            searchView.onActionViewCollapsed();
        }
        super.onResume();
    }

    @Override
    public void onRefreshListaLinhapedidos(ArrayList<Linhapedido> listaLinhapedidos) {
        if(listaLinhapedidos != null){
            lvLinhaspedido.setAdapter(new ListaLinhapedidoAdaptador(getApplicationContext(), listaLinhapedidos));
        }
    }
}