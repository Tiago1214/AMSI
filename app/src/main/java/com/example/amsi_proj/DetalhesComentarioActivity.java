package com.example.amsi_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.modelo.Comentario;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.BreakIterator;
import java.util.Calendar;

public class DetalhesComentarioActivity extends AppCompatActivity implements DetalhesListener {

    private Comentario comentario;
    private String token;
    private EditText etTitulo, etComentario;
    private FloatingActionButton fabGuardar;
    public static final int MIN_CHAR = 3, MIN_NUMERO=4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_comentario);
        SharedPreferences sharedPreferences =getSharedPreferences(MenuMainActivity.SHARED_USER, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(MenuMainActivity.TOKEN, null);
        int id=getIntent().getIntExtra("ID_COMENTARIO", 0);
        comentario= SingletonGersoft.getInstance(this).getComentario(id);
        etTitulo=findViewById(R.id.etTitulo);
        etComentario=findViewById(R.id.etComentario);
        fabGuardar=findViewById(R.id.fabGuardar);

        SingletonGersoft.getInstance(getApplicationContext()).setDetalhesListener(this);

        if(comentario != null){
            carregarComentario();
            fabGuardar.setImageResource(R.drawable.ic_action_guardar);
        }
        else {
            setTitle(getString(R.string.addcomentario));
            fabGuardar.setImageResource(R.drawable.ic_action_adicionar);

        }

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isComentarioValido()) {
                    if (comentario != null && token!=null) {
                        comentario.setTitulo(etTitulo.getText().toString());
                        comentario.setDescricao(etComentario.getText().toString());
                        SingletonGersoft.getInstance(getApplicationContext()).editarComentarioAPI(comentario, getApplicationContext(), token);
                    } else {
                        Comentario comentarioAux = new Comentario(comentario.getId(),comentario.getProfile_id(),etTitulo.getText().toString(), etComentario.getText().toString());
                        SingletonGersoft.getInstance(getApplicationContext()).adicionarComentarioAPI(comentarioAux, getApplicationContext(), token);
                    }
                }
            }

        });

    }

    private void carregarComentario() {
        Resources res=getResources();
        String titulo = String.format(res.getString(R.string.act_titulo), comentario.getTitulo());
        setTitle(titulo);
        etTitulo.setText(comentario.getTitulo());
        etComentario.setText(comentario.getDescricao());

    }

    private boolean isComentarioValido() {
        String titulo = etTitulo.getText().toString();

        String descricao = etComentario.getText().toString();



        if (titulo.length()<MIN_CHAR){
            etTitulo.setError("Titulo invalida");
            return false;
        }
        if (descricao.length()<=MIN_CHAR){
            etComentario.setError("Descricao invalido");
            return false;
        }

        return true;
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
        if(comentario!=null){
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
                        SingletonGersoft.getInstance(getApplicationContext()).removerComentarioAPI(comentario, getApplicationContext());

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