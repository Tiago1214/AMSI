package com.example.amsi_proj.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amsi_proj.R;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.GersoftBDHelper;
import com.example.amsi_proj.modelo.Linhapedido;
import com.example.amsi_proj.modelo.SingletonGersoft;

import java.util.ArrayList;

public class ListaLinhapedidoAdaptador extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Linhapedido> linhapedidos;

    public ListaLinhapedidoAdaptador(Context context, ArrayList<Linhapedido> linhapedidos) {
        this.context = context;
        this.linhapedidos = linhapedidos;
    }

    @Override
    public int getCount() {
        return linhapedidos.size();
    }

    @Override
    public Object getItem(int i) {
        return linhapedidos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return linhapedidos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = inflater.inflate(R.layout.item_lista_linhapedido, null);

        ListaLinhapedidoAdaptador.ViewHolderLista viewHolder = (ListaLinhapedidoAdaptador.ViewHolderLista) view.getTag();
        if(viewHolder == null)
        {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(linhapedidos.get(i));

        return view;
    }

    private class ViewHolderLista{
        private TextView tvQuantidade,tvTotal,tvNomeartigo,tvIvatotal;
        private ImageView imgLinhapedido;

        public ViewHolderLista(View view){
            tvQuantidade = view.findViewById(R.id.tvQuantidade);
            tvTotal = view.findViewById(R.id.tvValortotal);
            tvNomeartigo = view.findViewById(R.id.tvNomeartigo);
            tvIvatotal=view.findViewById(R.id.tvValoriva);
            imgLinhapedido=view.findViewById(R.id.imgLinhapedido);
        }

        public void update(Linhapedido linhapedido){
            if(linhapedido!=null){
                tvQuantidade.setText(linhapedido.getQuantidade()+"");
                tvTotal.setText(linhapedido.getValorunitario()*linhapedido.getQuantidade()+"€");
                tvIvatotal.setText((linhapedido.getValoriva()*linhapedido.getQuantidade())+"€");
                String nome="";
                String imgurl="";
                ArrayList<Artigo> tempLista = new ArrayList<>();
                ArrayList<Artigo> teste = SingletonGersoft.getInstance(context).getArtigosBD();
                for(Artigo a: teste)
                {
                    if(a.getId()==linhapedido.getId()){
                        nome=a.getNome();
                        imgurl=a.getImagemurl();
                    }
                }
                if(nome!=""&&imgurl!=""){
                    tvNomeartigo.setText(nome);
                    Glide.with(context)
                            .load("http://10.0.2.2/gersoft/backend/web/images/"+imgurl)
                            .into(imgLinhapedido);
                }

            }
        }
    }
}
