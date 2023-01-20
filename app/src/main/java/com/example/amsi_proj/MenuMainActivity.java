package com.example.amsi_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Comentario;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.navigation.NavigationView;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private String username;
    private String token;
    public static final String SHARED_USER="DADOS_USER";
    public static final String USERNAME="USERNAME";
    public static final String TOKEN="TOKEN";
    public static final String OPERACAO="OPERACAO";
    public static final int ADD=10, EDIT=20, DELETE=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);

        carregarCabecalho();
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();
    }

    //region atribuir valores ao header e ao menu
    private boolean carregarFragmentoInicial() {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
        return onNavigationItemSelected(item);
    }

    private void carregarCabecalho() {
        username=getIntent().getStringExtra(USERNAME);
        token= getIntent().getStringExtra(TOKEN);
        SharedPreferences infoUser=getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);

        if(username!=null && token!=null)  {
            SharedPreferences.Editor editor =infoUser.edit();
            editor.putString(USERNAME, username);
            editor.putString(TOKEN, token);
            editor.apply();
        }

        else
            username=infoUser.getString(USERNAME, getString(R.string.txt_email));

        if (username != null){
            View headerView = navigationView.getHeaderView(0);
            TextView tvUsername = headerView.findViewById(R.id.tvUsername); // Para ir buscar ao cabeçalho do navigation view
            tvUsername.setText(username);
            //Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
        }

    }
    //endregion

    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Fragment fragment = null;
        switch (item .getItemId()) {
            case R.id.navReserva:
                fragment = new ReservaFragment();
                setTitle(item.getTitle());
                break;

            case R.id.navPedidos:
                fragment = new PedidoMesaFragment();
                setTitle(item.getTitle());
                break;
            case R.id.navArtigos:
                fragment=new ListaArtigosFragment();
                setTitle(item.getTitle());
                break;
            case R.id.navRefeicao:
                fragment = new RefeicaoFragment();
                setTitle(item.getTitle());
                break;

            case R.id.navComentarios:
                fragment = new ComentariosFragment();
                setTitle(item.getTitle());
                break;

        }
        if (fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onClickLogout(View view) {
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Navigate to login activity
        Intent loginIntent = new Intent(MenuMainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        finish();
    }
}