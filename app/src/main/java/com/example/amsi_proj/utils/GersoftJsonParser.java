package com.example.amsi_proj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Comentario;
import com.example.amsi_proj.modelo.Linhapedido;
import com.example.amsi_proj.modelo.Mesa;
import com.example.amsi_proj.modelo.Pedido;
import com.example.amsi_proj.modelo.Reserva;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class GersoftJsonParser {

    //dar return do token
    public static ArrayList<String> parserJsonLogin(JSONObject response) { // static para nao ter de fazer new
        ArrayList<String> teste=new ArrayList<>();
        try{
            String token=response.getString("token");
            String profile_id=response.getString("profile_id");
            teste.add(token);
            teste.add(profile_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teste;
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


    //endregion

    //region reserva

    public static ArrayList<Reserva> parserJsonReservas(JSONArray response) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject reserva = (JSONObject) response.get(i);
                int id = reserva.getInt("id");
                int nrpessoas = reserva.getInt("nrpessoas");
                int estado = reserva.getInt("estado");
                int profile_id = reserva.getInt("profile_id");
                String data = reserva.getString("data");
                String hora=reserva.getString("hora");

                Reserva auxReserva = new Reserva(id, nrpessoas, estado, profile_id, data, hora);
                reservas.add(auxReserva);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    public static Reserva parserJsonReserva(String response) {
        Reserva auxReserva = null;
        try {
            JSONObject reserva = new JSONObject(response);
            int id = reserva.getInt("id");
            int nrpessoas = reserva.getInt("nrpessoas");
            int estado = reserva.getInt("estado");
            int profile_id = reserva.getInt("profile_id");
            String data = reserva.getString("data");
            String hora=reserva.getString("hora");
            auxReserva = new Reserva(id, nrpessoas, estado, profile_id, data, hora);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxReserva;
    }
    //endregion

    //region comentários
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

    //region Pedidos
    public static ArrayList<Pedido> parserJsonPedidos(JSONArray response) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject pedido = (JSONObject) response.get(i);
                int id = pedido.getInt("id");
                int tipo_pedido=pedido.getInt("tipo_pedido");
                int profile_id=pedido.getInt("profile_id");
                int metodo_pagamento_id=0;
                if(pedido.get("metodo_pagamento_id")==null){
                    metodo_pagamento_id=pedido.getInt("metodo_pagamento_id");
                }
                int mesa_id=0;
                if(pedido.get("mesa_id")==null){
                    mesa_id=pedido.getInt("mesa_id");
                }
                String data=pedido.getString("data");
                String estado=pedido.getString("estado");
                Double total=pedido.getDouble("total");
                Pedido auxPedido = new Pedido(id, tipo_pedido,profile_id,mesa_id,data,estado,total);
                pedidos.add(auxPedido);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public static Pedido parserJsonPedido(String response) {
        Pedido auxPedido = null;
        try {
            JSONObject pedido = new JSONObject(response);
            int id = pedido.getInt("id");
            int tipo_pedido=pedido.getInt("tipo_pedido");
            int profile_id=pedido.getInt("profile_id");
            int mesa_id=0;
            if(pedido.get("mesa_id")==null){
                mesa_id=pedido.getInt("mesa_id");
            }
            String data=pedido.getString("data");
            String estado=pedido.getString("estado");
            Double total=pedido.getDouble("total");
            auxPedido = new Pedido(id, tipo_pedido,profile_id,mesa_id,data,estado,total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxPedido;
    }

    public static ArrayList<Mesa> parserJsonMesas(JSONArray response) {
        ArrayList<Mesa> mesas = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject mesa = (JSONObject) response.get(i);
                int id = mesa.getInt("id");
                int nrmesa=mesa.getInt("nr_mesa");
                int nrlugares=mesa.getInt("nrlugares");
                String tipomesa=mesa.getString("tipomesa");
                Mesa auxMesa = new Mesa(id, nrmesa,nrlugares,tipomesa);
                mesas.add(auxMesa);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mesas;
    }

    public static ArrayList<Linhapedido> parserJsonLinhaPedidos(JSONArray response) {
        ArrayList<Linhapedido> linhapedidos = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject linhapedido = (JSONObject) response.get(i);
                int id = linhapedido.getInt("id");
                int quantidade=linhapedido.getInt("quantidade");
                int taxaiva=linhapedido.getInt("taxaiva");
                int pedido_id=linhapedido.getInt("pedido_id");
                int artigo_id=linhapedido.getInt("artigo_id");
                double valorunitario=linhapedido.getDouble("valorunitario");
                double valoriva=linhapedido.getDouble("valoriva");
                Linhapedido auxLinhapedido = new Linhapedido(id,
                        quantidade,taxaiva,pedido_id,artigo_id,valorunitario,valoriva);
                linhapedidos.add(auxLinhapedido);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return linhapedidos;
    }
    //endregion
}
