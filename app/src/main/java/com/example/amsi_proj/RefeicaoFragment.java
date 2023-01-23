package com.example.amsi_proj;

import android.content.Intent;
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

        View view = inflater.inflate(R.layout.fragment_refeicao, container, false);
        setHasOptionsMenu(true);
        tvNrpedidos=view.findViewById(R.id.tvNrPedidos);
        btnPedgratis=view.findViewById(R.id.btnPedgratis);
        btnPedgratis.setEnabled(false);
    }

}