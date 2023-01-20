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
import com.example.amsi_proj.modelo.Comentario;

import java.util.ArrayList;

public class ListaComentarioAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Comentario> comentarios;

    public ListaComentarioAdaptador(Context context, ArrayList<Comentario> comentarios) {
        this.context = context;
        this.comentarios = comentarios;
    }

    @Override
    public int getCount() {
        return comentarios.size();
    }

    @Override
    public Object getItem(int i) {
        return comentarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return comentarios.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = inflater.inflate(R.layout.item_lista_comentario, null);

        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if(viewHolder == null)
        {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(comentarios.get(i));
        return view;
    }

    private class ViewHolderLista{

        private TextView  tvTitulo, tvComentario;

        public ViewHolderLista(View view){

            tvTitulo = view.findViewById(R.id.tvTitulo);
            tvComentario = view.findViewById(R.id.tvComentario);

        }

        public void update(Comentario comentario){
            tvTitulo.setText(comentario.getTitulo());
            tvComentario.setText(comentario.getDescricao());

        }
    }
}
