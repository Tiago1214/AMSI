package com.example.amsi_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.amsi_proj.adaptadores.ListaLinhapedidoAdaptador;
import com.example.amsi_proj.listeners.LinhapedidoListener;
import com.example.amsi_proj.modelo.Linhapedido;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VisualizarPedidoConcluidoActivity extends AppCompatActivity implements LinhapedidoListener {

    private ListView lvLinhaspedido;
    private ArrayList<Linhapedido> linhapedidos;
    private SearchView searchView;
    private FloatingActionButton fabLista;
    public static final int ACT_DETALHES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onRefreshListaLinhapedidos(linhapedidos);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pedido_concluido);
        lvLinhaspedido = findViewById(R.id.lvLinhasPedido);
        int id=getIntent().getIntExtra("ID_PEDIDO", 0);
        //Chamar o listener do pedido
        SingletonGersoft.getInstance(getApplicationContext()).setLinhapedidoListener(this);
        //Pedir á api as linhas de pedido
        SingletonGersoft.getInstance(getApplicationContext()).getAllLinhaspedidoAPI(getApplicationContext(),id);
        //Pedir á api os pedidos concluidos
        SingletonGersoft.getInstance(getApplicationContext()).getAllPedidosConcluidosAPI(getApplicationContext());
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


    //Função para atribuir os pedidos concluidos quando se carregar no botao para voltar á página anterior
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingletonGersoft.getInstance(getApplicationContext()).getAllPedidosConcluidosAPI(getApplicationContext());
        finish();
    }
}