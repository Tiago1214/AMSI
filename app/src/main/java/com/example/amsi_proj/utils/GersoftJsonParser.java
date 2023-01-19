package com.example.amsi_proj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.amsi_proj.modelo.Artigo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GersoftJsonParser {

    //dar return do token
    public static String parserJsonLogin(String response) { // static para nao ter de fazer new
        return response;
    }

    //ver se o user ta ligado a internet
    public static Boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    public static ArrayList<Artigo> parserJsonArtigos(JSONArray response) {
        ArrayList<Artigo> livros = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject artigo = (JSONObject) response.get(i);
                int id = artigo.getInt("id");
                String nome = artigo.getString("nome");
                String descricao = artigo.getString("descricao");
                String referencia = artigo.getString("referencia");
                double preco = artigo.getDouble("preco");
                String imagemurl=artigo.getString("imagemurl");
                int iva_id=artigo.getInt("iva_id");
                int categoria_id=artigo.getInt("categoria_id");

                Artigo auxArtigo = new Artigo(id, nome, descricao, referencia, preco, imagemurl, iva_id,categoria_id);
                livros.add(auxArtigo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return livros;
    }

    public static Artigo parserJsonArtigo(String response) {
        Artigo auxArtigo = null;
        try {
            JSONObject artigo = new JSONObject(response);
            int id = artigo.getInt("id");
            String nome = artigo.getString("nome");
            String descricao = artigo.getString("descricao");
            String referencia = artigo.getString("referencia");
            double preco = artigo.getDouble("preco");
            String imagemurl=artigo.getString("imagemurl");
            int iva_id=artigo.getInt("iva_id");
            int categoria_id=artigo.getInt("categoria_id");
            auxArtigo = new Artigo(id, nome, descricao, referencia, preco, imagemurl, iva_id,categoria_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxArtigo;
    }
}
