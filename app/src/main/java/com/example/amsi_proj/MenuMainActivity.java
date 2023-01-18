package com.example.amsi_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

public class MenuMainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private DrawerLayout drawer;
    public static final String USERNAME="USERNAME";
    public static final String TOKEN="TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
    }



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
            case R.id.navRefeicao:
                fragment = new RefeicaoFragment();
                setTitle(item.getTitle());
                break;

            case R.id.navComentarios:
                fragment = new ComentariosFragment();
                setTitle(item.getTitle());
                break;

        }
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentfragment, fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}