package com.example.amsi_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.amsi_proj.modelo.SingletonGersoft;

public class EditarPedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingletonGersoft.getInstance(getApplicationContext()).getAllPedidosEmProcessamentoAPI(getApplicationContext());
        finish();
    }
}