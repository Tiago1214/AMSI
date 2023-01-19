package com.example.amsi_proj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.SingletonGersoft;

public class DetalhesArtigoActivity extends AppCompatActivity implements DetalhesListener {

    private Artigo artigo;
    private TextView nome,descricao,preco;
    private ImageView imgArtigo;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_artigo);
        SharedPreferences sharedPreferences =getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
        token = sharedPreferences.getString(MenuMainActivity.TOKEN, "");
        int id=getIntent().getIntExtra("ID_ARTIGO", 0);
        artigo= SingletonGersoft.getInstance(this).getArtigo(id);
        nome=findViewById(R.id.etNome);
        descricao=findViewById(R.id.etDescricao);
        preco=findViewById(R.id.etPreco);
        imgArtigo=findViewById(R.id.imgArtigo);

        SingletonGersoft.getInstance(getApplicationContext()).setDetalhesListener(this);

        if(artigo != null){
            carregarArtigo();
        }
    }

    @Override
    public void onRefreshDetalhes(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(MenuMainActivity.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void carregarArtigo() {
        Resources res=getResources();
        String titulo = String.format(artigo.getNome());
        setTitle(titulo);
        nome.setText(artigo.getNome());
        descricao.setText(artigo.getDescricao());
        preco.setText(artigo.getPreco()+" €");
        Glide.with(getApplicationContext())
                .load("http://10.0.2.2/gersoft/backend/web/images/"+artigo.getImagemurl())
                .into(imgArtigo);
    }
}
