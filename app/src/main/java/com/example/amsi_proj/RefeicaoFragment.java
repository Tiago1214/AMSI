package com.example.amsi_proj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.amsi_proj.adaptadores.ListaPedidoAdaptador;
import com.example.amsi_proj.listeners.PedidoListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Pedido;
import com.example.amsi_proj.modelo.SingletonGersoft;

import java.util.ArrayList;


public class RefeicaoFragment extends Fragment {

    private TextView tvNrpedidos;
    private ArrayList<Pedido> pedidos;
    private Button btnPedgratis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Definir variaveis
        View view = inflater.inflate(R.layout.fragment_refeicao, container, false);
        setHasOptionsMenu(true);
        tvNrpedidos=view.findViewById(R.id.tvNrPedidos);
        btnPedgratis=view.findViewById(R.id.btnPedgratis);
        btnPedgratis.setEnabled(false);

        //Definir sharedPrefences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");
        int profile_id=sharedPreferences.getInt("PROFILE_ID",0);

        /** Pedir todos os pedidos guardados na base de dados local
         * Procurar pelos pedidos que são diferentes de cancelado para o cliente com sessão iniciada
         * Quando o cliente tiver 10 registos irá ser ativado um botão para resgatar uma refeição grátis
         */
        ArrayList<Pedido> tempLista = new ArrayList<>();
        ArrayList<Pedido> teste = SingletonGersoft.getInstance(getContext()).getPedidosDB();
        int contador=0;
        for(Pedido p: teste)
        {
            if(p.getProfile_id()==profile_id&&p.getEstado()!="Cancelado"){
                contador++;
            }
        }
        if(contador==10){
            //Ativar o botão
            btnPedgratis.setEnabled(true);
            btnPedgratis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getContext(),CriarPedidoActivity.class);
                    startActivity(intent);
                }
            });
        }
        tvNrpedidos.setText(contador+"");
        return view;
    }
}