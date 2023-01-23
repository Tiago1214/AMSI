package com.example.amsi_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.GersoftBDHelper;
import com.example.amsi_proj.modelo.Mesa;
import com.example.amsi_proj.modelo.Pedido;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CriarPedidoActivity extends AppCompatActivity implements DetalhesListener {

    private Pedido pedido;
    //private EditText etMesa;
    private Spinner spMesa;
    private String token;
    private int profile_id;
    private FloatingActionButton fabGuardar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SingletonGersoft.getInstance(getApplicationContext()).getAllMesasAPI(getApplicationContext());
        super.onCreate(savedInstanceState);
        //vista
        setContentView(R.layout.activity_criar_pedido);
        //obter token
        SharedPreferences sharedPreferences =getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN", "");
        profile_id=sharedPreferences.getInt("PROFILE_ID",0);
        //id da reserva
        fabGuardar=findViewById(R.id.fabGuardar);
        InputFilter timeFilter;
        spMesa=findViewById(R.id.spMesa);
        /*ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);
        arrayList.add(6);
        arrayList.add(7);
        arrayList.add(8);
        arrayList.add(9);
        arrayList.add(10);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        ArrayList<Integer> arrayList = new ArrayList<>();
        ArrayList<Integer> tempLista = new ArrayList<>();
        ArrayList<Mesa> teste = SingletonGersoft.getInstance(getApplicationContext()).getMesasDB();
        for(Mesa m: teste)
        {
            arrayList.add(m.getNrmesa());
        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMesa.setAdapter(arrayAdapter);
        //----
        if(pedido!=null){
            carregarPedido();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        //endregion
        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pedido pedidoAux = new Pedido(0, 0,
                        profile_id, Integer.parseInt(spMesa.getSelectedItem().toString()),
                        currentDateandTime,"Em Processamento",0.0);
                SingletonGersoft.getInstance(getApplicationContext()).adicionarPedidoAPI(pedidoAux,
                        getApplicationContext(), token);

                finish();
            }
        });
        SingletonGersoft.getInstance(getApplicationContext()).setDetalhesListener(this);

    }

    private void carregarPedido() {
        Resources res=getResources();
        setTitle("Pedido");
    }

    @Override
    public void onRefreshDetalhes(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(MenuMainActivity.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(pedido!=null){
            getMenuInflater().inflate(R.menu.menu_detalhes_remover,menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemRemover:
                dialogRemover();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogRemover() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover Comentario")
                .setMessage("Tem a certeza que pretende remover o comentario?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //SingletonGersoft.getInstance(getApplicationContext()).removerPedidoAPI(pedido, getApplicationContext());

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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingletonGersoft.getInstance(getApplicationContext()).getAllPedidosEmProcessamentoAPI(getApplicationContext());
        finish();
    }
}