package com.example.amsi_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.listeners.PedidoListener;
import com.example.amsi_proj.modelo.GersoftBDHelper;
import com.example.amsi_proj.modelo.Mesa;
import com.example.amsi_proj.modelo.Pedido;
import com.example.amsi_proj.modelo.Reserva;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CriarPedidoActivity extends AppCompatActivity implements DetalhesListener {

    private Pedido pedido;
    private EditText etMesa;
    private String token;
    private int profile_id;
    private FloatingActionButton fabGuardar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        etMesa=findViewById(R.id.etMesa);
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
                        profile_id, Integer.parseInt(etMesa.getText().toString()),
                        currentDateandTime,"Em Processamento",0.0);
                SingletonGersoft.getInstance(getApplicationContext()).adicionarPedidoAPI(pedidoAux,
                        getApplicationContext(), token);
                finish();
            }
        });
    }

    private void carregarPedido() {
        Resources res=getResources();
        setTitle("Pedido");
        etMesa.setText(pedido.getMesa_id());
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
}