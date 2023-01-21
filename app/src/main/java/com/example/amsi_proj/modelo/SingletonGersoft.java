package com.example.amsi_proj.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


import com.example.amsi_proj.DetalhesArtigoActivity;
import com.example.amsi_proj.ListaArtigosFragment;
import com.example.amsi_proj.LoginActivity;
import com.example.amsi_proj.MenuMainActivity;
import com.example.amsi_proj.R;
import com.example.amsi_proj.ReservaFragment;
import com.example.amsi_proj.listeners.ArtigoListener;
import com.example.amsi_proj.listeners.ComentarioListener;
import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.listeners.LoginListener;
import com.example.amsi_proj.listeners.ReservaListener;
import com.example.amsi_proj.utils.GersoftJsonParser;

public class SingletonGersoft {

    private static  SingletonGersoft instance = null;
    private static RequestQueue volleyQueue = null;
    private static final  String mUrlAPILogin = "http://10.0.2.2/gersoft/backend/web/api/users/auth";
    private static final String mUrlAPIArtigos="http://10.0.2.2/gersoft/backend/web/api/artigos";

    private static final String mUrlAPIComentarios="http://10.0.2.2/gersoft/backend/web/api/comentarios/meuscomentarios";

    private LoginListener loginListener;
    private ArtigoListener artigoListener;
    private DetalhesListener detalhesListener;
    private GersoftBDHelper comentariosBD;
    private GersoftBDHelper gersoftBD;
    private ArrayList<Artigo> artigos;
    private ArrayList<Comentario> comentarios;
    private ComentarioListener comentarioListener;

    private static final String mUrlAPIReservas="http://10.0.2.2/gersoft/backend/web/api/reservas/minhasreservas";
    private static final String mUrlAPIReservasEditar="http://10.0.2.2/gersoft/backend/web/api/reservas";
    private DetalhesListener DetalhesListener;
    private ReservaListener reservasListener;

    private ArrayList<Reserva> reservas;

    //region variaveis do singleton
    public SingletonGersoft(Context context) {
        gersoftBD=new GersoftBDHelper(context);
    }

    public static synchronized SingletonGersoft getInstance(Context context){
        if(instance == null)
        {
            instance = new SingletonGersoft(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }
    //endregion
    //region Login
    public void loginAPI(final String username, final String password, final Context context){
        if (!GersoftJsonParser.isConnectionInternet(context)){
            //Intent intent=new Intent(LoginActivity.class);
            Toast.makeText(context, R.string.SemligacaoInternet, Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.GET, mUrlAPILogin, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String token = GersoftJsonParser.parserJsonLogin(response);
                    if (loginListener != null)
                        loginListener.onValidateLogin(token,username);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("LOGIN: Error" + error.getMessage());
                    //para dar mensagem de erro no login
                    loginListener.onValidateLogin(null,username);

                }
            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Basic "+Base64.getEncoder().encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8)));
                    return params;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
    //endregion
    public void setDetalhesListener(DetalhesListener detalhesListener ) {
        this.detalhesListener=detalhesListener;
    }
    //region artigos
    public ArrayList<Artigo> getArtigosBD() { // return da copia dos livros
        artigos=gersoftBD.getAllArtigosBD();
        return new ArrayList(artigos);
    }

    public void setArtigosListener(ArtigoListener artigoListener) {
        this.artigoListener=artigoListener;
    }

    public void adicionarArtigosBD(ArrayList<Artigo> artigos)
    {
        gersoftBD.removerAllArtigos();
        for(Artigo a:artigos)
        {
            adicionarArtigoBD(a);
        }
    }

    public void adicionarArtigoBD(Artigo a)
    {
        gersoftBD.adicionarArtigoBD(a);
    }

    public void getAllArtigosAPI(final Context context){
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
            if (artigoListener!=null)
            {
                artigoListener.onRefreshListaArtigos(gersoftBD.getAllArtigosBD());
            }
        }else
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            JsonArrayRequest req=new JsonArrayRequest(Request.Method.GET, mUrlAPIArtigos+"?access-token="+token_logado, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    artigos = GersoftJsonParser.parserJsonArtigos(response);
                    adicionarArtigosBD(artigos);

                    if (artigoListener!=null)
                    {
                        artigoListener.onRefreshListaArtigos(artigos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public Artigo getArtigo(int idArtigo){
        for (Artigo a : artigos){
            if(a.getId() == idArtigo)
                return a;
        }
        return null;
    }


    //endregion

    //region reservas
    public void setReservaListener(ReservaListener reservaListener) {
        this.reservasListener=reservaListener;
    }

    public void getAllReservasAPI(final Context context){
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
            if (reservasListener!=null)
            {
                reservasListener.onRefreshListaReservas(gersoftBD.getAllReservasBD());
            }
        }else
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            JsonArrayRequest req=new JsonArrayRequest(Request.Method.GET, mUrlAPIReservas+"?access-token="+token_logado, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    reservas = GersoftJsonParser.parserJsonReservas(response);
                    adicionarReservasBD(reservas);

                    if (reservasListener!=null)
                    {
                        reservasListener.onRefreshListaReservas(reservas);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public void adicionarReservasBD(ArrayList<Reserva> reservas)
    {
        gersoftBD.removerAllReservas();
        for(Reserva r:reservas)
        {
            adicionarReservaBD(r);
        }
    }

    public void adicionarReservaBD(Reserva r)
    {
        gersoftBD.adicionarReservaBD(r);
    }

    public ArrayList<Reserva> getReservasBD() { // return da copia dos livros
        reservas=gersoftBD.getAllReservasBD();
        return new ArrayList(reservas);
    }


    public Reserva getReserva(int idReserva){
        for (Reserva r : reservas){
            if(r.getId() == idReserva)
                return r;
        }
        return null;
    }

    public void editarReservaAPI(final Reserva reserva,final Context context, String token) {
        if(!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context,"Sem ligação á internet",Toast.LENGTH_LONG).show();
        }else{
            StringRequest req = new StringRequest(Request.Method.PATCH, mUrlAPIReservasEditar+ "/"+reserva.getId()+"?access-token="+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    editarReservaDB(reserva);
                    if (DetalhesListener!=null)
                    {
                        DetalhesListener.onRefreshDetalhes(MenuMainActivity.EDIT);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("nrpessoas", reserva.getNrpessoas()+"");
                    params.put("data",reserva.getData());
                    params.put("hora",reserva.getHora());
                    //params.put("profile_id",);

                    return params;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void editarReservaDB(Reserva r)
    {
        Reserva auxReserva = getReserva(r.getId());
        if(auxReserva!=null)
        {
            gersoftBD.editarReservaBD(r);
        }

    }


        //endregion


    //region comentarios

    public ArrayList getComentariosDB() {
        comentarios=gersoftBD.getAllComentariosBD();
        return new ArrayList(comentarios);
    }
    public void setComentariosListener(ComentarioListener comentarioListener) {
        this.comentarioListener=comentarioListener;
    }

    public void adicionarComentariosBD(ArrayList<Comentario> comentarios)
    {
        gersoftBD.removerAllCometarios();
        for(Comentario c : comentarios)
        {
            adicionarComentarioBD(c);
        }
    }
    public void adicionarComentarioBD(Comentario c)
    {
        gersoftBD.adicionarComentarioBD(c);
    }

    public void getAllComentariosAPI(final Context context){
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
            if (comentarioListener!=null)
            {
                comentarioListener.onRefreshListaComentarios(gersoftBD.getAllComentariosBD());
            }
        }else
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            JsonArrayRequest req=new JsonArrayRequest(Request.Method.GET, mUrlAPIComentarios+"?access-token="+token_logado, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    comentarios = GersoftJsonParser.parserJsonComentarios(response);
                    adicionarComentariosBD(comentarios);

                    if (comentarioListener!=null)
                    {
                        comentarioListener.onRefreshListaComentarios(comentarios);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(req);
        }
    }
    public Comentario getComentario(int idComentario){
        for (Comentario c : comentarios){
            if(c.getId() == idComentario)
                return c;
        }
        return null;
    }

    public void editarComentarioAPI(final Comentario comentario , final Context context, String token){
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.PUT, mUrlAPIComentarios+ "/"+comentario.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    editarComentarioBD(comentario);

                    if (detalhesListener!=null)
                    {
                        detalhesListener.onRefreshDetalhes(MenuMainActivity.EDIT);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", token);
                    params.put("titulo", comentario.getTitulo());
                    params.put("comentario", comentario.getDescricao());
                    return params;
                }
            };
            volleyQueue.add(req);
        }
    }

    private void editarComentarioBD(Comentario c) {
        Comentario auxComentario = getComentario(c.getId());
        if(auxComentario!=null)
        {
            comentariosBD.editarComentarioBD(c);
        }
    }

    public void adicionarComentarioAPI(final Comentario comentario, final Context context, String token) {

        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIComentarios, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    adicionarComentarioBD(GersoftJsonParser.parserJsonComentario(response));
                    if (detalhesListener!=null)
                    {
                        detalhesListener.onRefreshDetalhes(MenuMainActivity.ADD);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", token);
                    params.put("titulo", comentario.getTitulo());
                    params.put("descricao", comentario.getDescricao());
                    return params;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void removerComentarioAPI(final Comentario comentario, final Context context){
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.DELETE, mUrlAPIComentarios+ "/"+comentario.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    removerComentarioBD(comentario.getId());
                    if (detalhesListener!=null)
                    {
                        detalhesListener.onRefreshDetalhes(MenuMainActivity.DELETE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    private void removerComentarioBD(int id) {
            Comentario auxComentario = getComentario(id);
            if (auxComentario!=null)
                comentariosBD.removerLivroBD(id);

    }


    //endregion
}
