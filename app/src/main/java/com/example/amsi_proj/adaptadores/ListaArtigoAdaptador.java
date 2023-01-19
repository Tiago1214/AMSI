package com.example.amsi_proj.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.amsi_proj.ListaArtigosFragment;
import com.example.amsi_proj.R;
import com.example.amsi_proj.modelo.Artigo;

import java.util.ArrayList;


public class ListaArtigoAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Artigo> artigos;

    public ListaArtigoAdaptador(Context context, ArrayList<Artigo> artigos) {
        this.context = context;
        this.artigos = artigos;
    }

    @Override
    public int getCount() {
        return artigos.size();
    }

    @Override
    public Object getItem(int i) {
        return artigos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return artigos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = inflater.inflate(R.layout.item_lista_artigo, null);

        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if(viewHolder == null)
        {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }
        viewHolder.update(artigos.get(i));
        return view;
    }

    private class ViewHolderLista{
        private ImageView imgArtigo;
        private TextView tvNome, tvPreco;

        public ViewHolderLista(View view){
            tvNome = view.findViewById(R.id.tvNome);
            tvPreco = view.findViewById(R.id.tvPreco);
            imgArtigo=view.findViewById(R.id.imgArtigo);
        }
        public void update(Artigo artigo){
            tvNome.setText(artigo.getNome());
            tvPreco.setText(artigo.getPreco()+" €");
            Glide.with(context)
                    .load("http://10.0.2.2/gersoft/backend/web/images/"+artigo.getImagemurl())
                    .into(imgArtigo);
        }
    }
}
