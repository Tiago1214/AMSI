package com.example.amsi_proj.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amsi_proj.R;
import com.example.amsi_proj.modelo.Pedido;
import com.example.amsi_proj.modelo.Reserva;

import java.util.ArrayList;

public class ListaPedidoAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Pedido> pedidos;

    public ListaPedidoAdaptador(Context context, ArrayList<Pedido> pedidos) {
        this.context = context;
        this.pedidos = pedidos;
    }

    @Override
    public int getCount() {
        return pedidos.size();
    }

    @Override
    public Object getItem(int i) {
        return pedidos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return pedidos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = inflater.inflate(R.layout.item_lista_pedido, null);

        ListaPedidoAdaptador.ViewHolderLista viewHolder = (ListaPedidoAdaptador.ViewHolderLista) view.getTag();
        if(viewHolder == null)
        {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(pedidos.get(i));

        return view;
    }

    private class ViewHolderLista{
        private TextView tvEstado,tvTotal,tvTipoPedido;
        private ImageView imgPedido;

        public ViewHolderLista(View view){
            tvTipoPedido = view.findViewById(R.id.tvTipoPedido);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvEstado=view.findViewById(R.id.tvEstado);
            imgPedido=view.findViewById(R.id.imgPedido);

        }

        public void update(Pedido pedido){
            if(pedido.getTipo_pedido()==0){
                tvTipoPedido.setText("Restaurante");
            }
            else{
                tvTipoPedido.setText("Takeaway");
            }
            tvTotal.setText(pedido.getTotal()+"€");
            tvEstado.setText(pedido.getEstado());
            imgPedido.setImageResource(R.drawable.ic_action_takeaway_foreground);
        }
    }
}
