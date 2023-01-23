package com.example.amsi_proj.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.amsi_proj.R;
import com.example.amsi_proj.modelo.Reserva;

import java.util.ArrayList;

public class ListaReservaAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Reserva> reservas;

    public ListaReservaAdaptador(Context context, ArrayList<Reserva> reservas) {
        this.context = context;
        this.reservas = reservas;
    }

    @Override
    public int getCount() {
        return reservas.size();
    }

    @Override
    public Object getItem(int i) {
        return reservas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return reservas.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = inflater.inflate(R.layout.item_lista_reserva, null);

        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if(viewHolder == null)
        {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(reservas.get(i));

        return view;
    }

    private class ViewHolderLista{
        private TextView tvNumeropessoas, tvData, tvHora,tvEstado;
        private ImageView imgCapa;

        public ViewHolderLista(View view){
            tvNumeropessoas = view.findViewById(R.id.tvNumeroPessoas);
            tvData = view.findViewById(R.id.tvData);
            tvHora = view.findViewById(R.id.tvHora);
            tvEstado=view.findViewById(R.id.tvEstado);
            imgCapa=view.findViewById(R.id.imageView3);

        }

        public void update(Reserva reserva){
            tvNumeropessoas.setText(reserva.getNrpessoas()+"");
            tvData.setText(reserva.getData());
            tvHora.setText(reserva.getHora()+"");
            imgCapa.setImageResource(R.drawable.ic_baseline_bookmark);
            if(reserva.getEstado()==0){
                tvEstado.setText("Em processamento");
            }
            else if(reserva.getEstado()==1){
                tvEstado.setText("Concluído");
            }
            else if(reserva.getEstado()==2){
                tvEstado.setText("Cancelado");
            }
        }
    }
}
