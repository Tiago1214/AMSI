package com.example.amsi_proj;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amsi_proj.adaptadores.ListaLinhapedidoAdaptador;
import com.example.amsi_proj.listeners.LinhapedidoListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Linhapedido;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditarPedidoActivity extends AppCompatActivity implements LinhapedidoListener {

    private ListView lvLinhaspedido;
    private Linhapedido linhapedido;
    private EditText etQuantidade;
    private Spinner spArtigo;
    private int profile_id;
    int pedido_id;
    private String token;
    private ArrayList<Linhapedido> linhapedidos;
    private SearchView searchView;
    private FloatingActionButton fabLista;
    public static final int ACT_DETALHES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Atribuir valores á lista
        onRefreshListaLinhapedidos(linhapedidos);
        //definir vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);
        //items da vista
        lvLinhaspedido=findViewById(R.id.lvLinhaspedido);
        etQuantidade=findViewById(R.id.etQuantidade);
        spArtigo=findViewById(R.id.spArtigo);
        pedido_id=getIntent().getIntExtra("ID_ARTIGO", 0);
        //spinner
        ArrayList<String> arrayList = new ArrayList<>();

        ArrayList<Artigo> tempLista = new ArrayList<>();
        ArrayList<Artigo> teste = SingletonGersoft.getInstance(getApplicationContext()).getArtigosBD();
        for(Artigo a: teste)
        {
            arrayList.add(a.getNome());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArtigo.setAdapter(arrayAdapter);
        //spinner valroes acabado
        //lista delete
        lvLinhaspedido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                linhapedido= SingletonGersoft.getInstance(getApplicationContext()).getLinhapedido((int)id);
                dialogRemover(linhapedido);
                onRefreshListaLinhapedidos(SingletonGersoft.getInstance(getApplicationContext()).getLinhaspedidosDB());
            }
        });

        //shared
        SharedPreferences sharedPreferences =getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN", "");
        profile_id=sharedPreferences.getInt("PROFILE_ID",0);

        //id do pedido selecionado
        pedido_id=getIntent().getIntExtra("ID_PEDIDO", 0);
        SingletonGersoft.getInstance(getApplicationContext()).setLinhapedidoListener(this);
        SingletonGersoft.getInstance(getApplicationContext()).getAllLinhaspedidoAPI(getApplicationContext(),pedido_id);

    }

    @Override
    public void onResume() {
        if (searchView!=null){
            searchView.onActionViewCollapsed();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingletonGersoft.getInstance(getApplicationContext()).getAllPedidosEmProcessamentoAPI(getApplicationContext());
        finish();
    }

    @Override
    public void onRefreshListaLinhapedidos(ArrayList<Linhapedido> listaLinhapedidos) {
        if(listaLinhapedidos != null){
            lvLinhaspedido.setAdapter(new ListaLinhapedidoAdaptador(getApplicationContext(), listaLinhapedidos));
        }
    }

    //Guardar linhas de pedido e atualizar dinamicamente
    public void onClickGuardar(View view){
        if(etQuantidade!=null&&spArtigo!=null){
            ArrayList<Artigo> teste = SingletonGersoft.getInstance(getApplicationContext()).getArtigosBD();
            double preco=0;
            int idartigo=0;
            String nome=spArtigo.getSelectedItem().toString();
            for(Artigo a: teste)
            {
                if(nome.equals(a.getNome())){
                    preco=a.getPreco();
                    idartigo=a.getId();
                }
            }
            double valoriva=preco*0.23;
            //criar linha de pedido
            Linhapedido linhapedidoAux = new Linhapedido(0, Integer.parseInt(etQuantidade.getText().toString()),23,
                    pedido_id,idartigo,preco,valoriva);
            SingletonGersoft.getInstance(getApplicationContext()).adicionarLinhaPedidoAPI(linhapedidoAux,
                    getApplicationContext(), token);
            onRefreshListaLinhapedidos(SingletonGersoft.getInstance(getApplicationContext()).getLinhaspedidosDB());
        }
    }

    private void dialogRemover(Linhapedido linhapedido) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover Linha do Pedido")
                .setMessage("Tem a certeza que pretende remover a linha do pedido?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SingletonGersoft.getInstance(getApplicationContext()).removerLinhaPedidoAPI(linhapedido, getApplicationContext());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nao fazer nada
                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }
}