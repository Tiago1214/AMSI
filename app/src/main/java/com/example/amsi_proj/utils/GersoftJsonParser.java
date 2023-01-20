package com.example.amsi_proj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Comentario;
import com.example.amsi_proj.modelo.Reserva;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

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


    //region artigo
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
                String imagemurl=artigo.getString("imagem");
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

    public static ArrayList<Comentario> parserJsonComentarios(JSONArray response) {
        ArrayList<Comentario> comentarios = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject comentario = (JSONObject) response.get(i);
                int id = comentario.getInt("id");
                String titulo = comentario.getString("titulo");
                String descricao = comentario.getString("descricao");
                int profile_id = comentario.getInt("profile_id");

                Comentario auxComentario = new Comentario(id, profile_id,titulo, descricao);
                comentarios.add(auxComentario);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    public static Comentario parserJsonComentario(String response) {
        Comentario auxComentario = null;
        try {
            JSONObject comentario = new JSONObject(response);
            int id = comentario.getInt("id");
            String titulo = comentario.getString("titulo");
            String descricao = comentario.getString("descricao");
            int profile_id = comentario.getInt("profile_id");
            auxComentario = new Comentario(id, profile_id,titulo, descricao);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxComentario;
    }
    //endregion

    //region reserva

    /*public static ArrayList<Reserva> parserJsonArtigos(JSONArray response) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject reserva = (JSONObject) response.get(i);
                int id = reserva.getInt("id");
                int nrpessoas = reserva.getInt("nrpessoas");
                String descricao = artigo.getString("descricao");
                String referencia = artigo.getString("referencia");
                double preco = artigo.getDouble("preco");
                String imagemurl=artigo.getString("imagem");
                int iva_id=artigo.getInt("iva_id");
                int categoria_id=artigo.getInt("categoria_id");

                Artigo auxArtigo = new Artigo(id, nome, descricao, referencia, preco, imagemurl, iva_id,categoria_id);
                reservas.add(auxArtigo);
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
    }*/
    //endregion
}
