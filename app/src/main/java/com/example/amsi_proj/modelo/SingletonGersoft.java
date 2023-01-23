package com.example.amsi_proj.modelo;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import com.example.amsi_proj.MenuMainActivity;
import com.example.amsi_proj.R;
import com.example.amsi_proj.listeners.ArtigoListener;
import com.example.amsi_proj.listeners.ComentarioListener;
import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.listeners.LinhapedidoListener;
import com.example.amsi_proj.listeners.LoginListener;
import com.example.amsi_proj.listeners.MesaListener;
import com.example.amsi_proj.listeners.PedidoListener;
import com.example.amsi_proj.listeners.ReservaListener;
import com.example.amsi_proj.utils.GersoftJsonParser;

public class SingletonGersoft {

    private static  SingletonGersoft instance = null;
    private static RequestQueue volleyQueue = null;
    //connect login api
    private static final  String mUrlAPILogin = "http://10.0.2.2/gersoft/backend/web/api/users/auth";
    //connect artigos api
    private static final String mUrlAPIArtigos="http://10.0.2.2/gersoft/backend/web/api/artigos";

    //ir buscar a lista dos comentários do utilizador com sessão iniciada
    private static final String mUrlAPIComentarios="http://10.0.2.2/gersoft/backend/web/api/comentarios/meuscomentarios";
    //criar,eliminar ou editar reservas
    private static final String mUrlAPIComentariosEditAdd="http://10.0.2.2/gersoft/backend/web/api/comentarios";

    private LoginListener loginListener;
    private ArtigoListener artigoListener;
    private DetalhesListener detalhesListener;
    private GersoftBDHelper gersoftBD;
    private ArrayList<Artigo> artigos;
    private ArrayList<Comentario> comentarios;
    private ComentarioListener comentarioListener;

    //ir buscar as reservas do utilizador com sessão iniciada
    private static final String mUrlAPIReservas="http://10.0.2.2/gersoft/backend/web/api/reservas/minhasreservas";
    //criar ou editar reservas
    private static final String mUrlAPIReservasEditAdd="http://10.0.2.2/gersoft/backend/web/api/reservas";
    //cancelar reservas
    private static final String getmUrlAPIReservasCancelar="http://10.0.2.2/gersoft/backend/web/api/reservas/cancelarreserva";
    private DetalhesListener DetalhesListener;
    private ReservaListener reservasListener;

    private ArrayList<Reserva> reservas;

    //pedidos
    private PedidoListener pedidoListener;
    private static final String mUrlAPIPedidos="http://10.0.2.2/gersoft/backend/web/api/pedidos/meuspedidos";
    private static final String mUrlAPIPedidosEditAdd="http://10.0.2.2/gersoft/backend/web/api/pedidos";
    private static final String mUrlAPILinhaPedidoAll="http://10.0.2.2/gersoft/backend/web/api/linhapedidos/linhasdopedido";
    private static final String mUrlAPILinhaPedidoEditAdd="http://10.0.2.2/gersoft/backend/web/api/linhapedidos";
    private ArrayList<Pedido> pedidos;
    private LinhapedidoListener linhapedidoListener;
    private ArrayList<Linhapedido> linhapedidos;

    //mesas
    private static final String mUrlAPIMesas="http://10.0.2.2/gersoft/backend/web/api/mesas";
    private ArrayList<Mesa> mesas;
    private MesaListener mesaListener;

    //region variaveis do singleton
    public SingletonGersoft(Context context) {
        gersoftBD=new GersoftBDHelper(context);
    }

    //instancia do singleton
    public static synchronized SingletonGersoft getInstance(Context context){
        if(instance == null)
        {
            instance = new SingletonGersoft(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    //detalhes do que o utilizador está a fazer
    public void setDetalhesListener(DetalhesListener detalhesListener ) {
        this.detalhesListener=detalhesListener;
    }

    public void setMesaListener(MesaListener mesaListener){
        this.mesaListener=mesaListener;
    }
    //endregion

    //region Login

    //validar o login ao fazer o método get á api com o username e password
    public void loginAPI(final String username, final String password, final Context context){
        //verificar se o utilizador tem ligação a internet
        if (!GersoftJsonParser.isConnectionInternet(context)){

            Toast.makeText(context, R.string.SemligacaoInternet, Toast.LENGTH_LONG).show();
        }else{
            JsonObjectRequest req=new JsonObjectRequest(Request.Method.GET, mUrlAPILogin, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ArrayList<String> arrayList = GersoftJsonParser.parserJsonLogin(response);
                    if (loginListener != null)
                        loginListener.onValidateLogin(arrayList.get(0),username,Integer.parseInt(arrayList.get(1)));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, R.string.ErroLogin,Toast.LENGTH_LONG).show();
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

    //region artigos
    public ArrayList<Artigo> getArtigosBD() { // return da copia dos artigos
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
                    adicionarReservasBD(reservas,context);

                    if (reservasListener!=null)
                    {
                        reservasListener.onRefreshListaReservas(reservas);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Erro", Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public void adicionarReservasBD(ArrayList<Reserva> reservas,Context context)
    {
        gersoftBD.removerAllReservas();
        for(Reserva r:reservas)
        {
            adicionarReservaBD(r,context);
        }
    }

    public void adicionarReservaBD(Reserva r,Context context)
    {
        gersoftBD.adicionarReservaBD(r,context);
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
                StringRequest req = new StringRequest(Request.Method.PATCH, mUrlAPIReservasEditAdd+ "/"+reserva.getId()+"?access-token="+token, new Response.Listener<String>() {
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

    public void cancelarReservaAPI(Reserva reserva, final Context context,String token) {
        if (!GersoftJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        } else {
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, getmUrlAPIReservasCancelar + "/" + reserva.getId()
                    + "?access-token=" + token, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    cancelarReservaDB(reserva);

                    reservasListener.onRefreshListaReservas(reservas);

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
    public void cancelarReservaDB(Reserva r){
        Reserva auxReserva = getReserva(r.getId());
        if(auxReserva!=null)
        {
            gersoftBD.cancelarReservaDB(r);
        }
    }

    public void adicionarReservaAPI(final Reserva reserva, Context context, String token) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIReservasEditAdd+"?access-token="+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    adicionarReservaBD(GersoftJsonParser.parserJsonReserva(response),context);
                    if (DetalhesListener!=null)
                    {
                        DetalhesListener.onRefreshDetalhes(MenuMainActivity.ADD);
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
                    SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
                    int profile_id=sharedPreferences.getInt("PROFILE_ID",0);
                    int estado=0;
                    params.put("token", token);
                    params.put("nrpessoas", reserva.getNrpessoas()+"");
                    params.put("estado", estado+"");
                    params.put("profile_id", profile_id+"");
                    params.put("data",reserva.getData());
                    params.put("hora",reserva.getHora());
                    return params;
                }
            };
            volleyQueue.add(req);
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

    public void adicionarComentariosBD(ArrayList<Comentario> comentarios,Context context)
    {
        gersoftBD.removerAllComentarios();
        for(Comentario c : comentarios)
        {
            adicionarComentarioBD(c,context);
        }
    }
    public void adicionarComentarioBD(Comentario c,Context context)
    {
        gersoftBD.adicionarComentarioBD(c,context);
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
                    adicionarComentariosBD(comentarios,context);

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

            StringRequest req = new StringRequest(Request.Method.PATCH, mUrlAPIComentariosEditAdd+ "/"+comentario.getId()+"?access-token="+token
                    , new Response.Listener<String>() {
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
                    params.put("titulo", comentario.getTitulo());
                    params.put("descricao", comentario.getDescricao());
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
            gersoftBD.editarComentarioBD(c);
        }
    }


    public void adicionarComentarioAPI(final Comentario comentario, final Context context, String token) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIComentariosEditAdd+"?access-token="+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    adicionarComentarioBD(GersoftJsonParser.parserJsonComentario(response),context);
                    if (DetalhesListener!=null)
                    {
                        DetalhesListener.onRefreshDetalhes(MenuMainActivity.ADD);
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
                    SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
                    int profile_id=sharedPreferences.getInt("PROFILE_ID",0);
                    params.put("token", token);
                    params.put("titulo", comentario.getTitulo());
                    params.put("descricao", comentario.getDescricao());
                    params.put("profile_id", profile_id+"");
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
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            StringRequest req = new StringRequest(Request.Method.DELETE, mUrlAPIComentariosEditAdd+ "/"+comentario.getId()+"?access-token="+token_logado
                    , new Response.Listener<String>() {
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
                gersoftBD.removerComentarioDB(id);
    }
    //endregion

    //region Pedidos
    public void setPedidoListener(PedidoListener pedidoListener) {
        this.pedidoListener=pedidoListener;
    }

    public void getAllPedidosConcluidosAPI(final Context context) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
            if (pedidoListener!=null)
            {
                pedidoListener.onRefreshListaPedidos(gersoftBD.getAllPedidosBD());
            }
        }else
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            JsonArrayRequest req=new JsonArrayRequest(Request.Method.GET, mUrlAPIPedidosEditAdd+"/pedidosconcluidos?access-token="+token_logado, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    pedidos = GersoftJsonParser.parserJsonPedidos(response);
                    adicionarPedidosBD(pedidos,context);

                    if (pedidoListener!=null)
                    {
                        pedidoListener.onRefreshListaPedidos(pedidos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public void getAllPedidosEmProcessamentoAPI(final Context context) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
            if (pedidoListener!=null)
            {
                pedidoListener.onRefreshListaPedidos(gersoftBD.getAllPedidosBD());
            }
        }else
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            JsonArrayRequest req=new JsonArrayRequest(Request.Method.GET, mUrlAPIPedidosEditAdd+"/pedidosemprocessamento?access-token="+token_logado, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    pedidos = GersoftJsonParser.parserJsonPedidos(response);
                    adicionarPedidosBD(pedidos,context);

                    if (pedidoListener!=null)
                    {
                        pedidoListener.onRefreshListaPedidos(pedidos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public void adicionarPedidosBD(ArrayList<Pedido> pedidos,Context context)
    {
        gersoftBD.removerAllPedidos();
        for(Pedido p : pedidos)
        {
            adicionarPedidoBD(p,context);
        }
    }

    public void adicionarPedidoBD(Pedido p,Context context)
    {
        gersoftBD.adicionarPedidoBD(p,context);
    }

    public ArrayList getPedidosDB() {
        pedidos=gersoftBD.getAllPedidosBD();
        return new ArrayList(pedidos);
    }

    public Pedido getPedido(int id) {
        for (Pedido p : pedidos){
            if(p.getId() == id)
                return p;
        }
        return null;
    }
    //endregion

    //region mesas
    public void getAllMesasAPI(final Context context) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
            if(mesaListener!=null){
                mesaListener.onRefreshListaMesas(gersoftBD.getAllMesasDB());
            }
        }else
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            JsonArrayRequest req=new JsonArrayRequest(Request.Method.GET, mUrlAPIMesas+"?access-token="+token_logado, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    mesas = GersoftJsonParser.parserJsonMesas(response);
                    adicionarMesasBD(mesas,context);
                    if(mesaListener!=null){
                        mesaListener.onRefreshListaMesas(mesas);
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

    private void adicionarMesasBD(ArrayList<Mesa> mesas, Context context) {
        gersoftBD.removerAllMesas();
        for(Mesa m : mesas)
        {
            adicionarMesaBD(m,context);
        }
    }

    private void adicionarMesaBD(Mesa m, Context context) {
        gersoftBD.adicionarMesaBD(m,context);
    }

    public void adicionarPedidoAPI(Pedido pedido, Context context, String token) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIPedidosEditAdd+"?access-token="+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    adicionarPedidoBD(GersoftJsonParser.parserJsonPedido(response),context);
                    if (DetalhesListener!=null)
                    {
                        DetalhesListener.onRefreshDetalhes(MenuMainActivity.ADD);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
                    int profile_id=sharedPreferences.getInt("PROFILE_ID",0);
                    params.put("tipo_pedido",0+"");
                    params.put("profile_id", profile_id+"");
                    params.put("mesa_id",pedido.getMesa_id()+"");
                    params.put("data",currentDateandTime);
                    params.put("estado","Em Processamento");
                    params.put("total",0+"");
                    return params;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void setLinhapedidoListener(LinhapedidoListener linhapedidoListener) {
        this.linhapedidoListener=linhapedidoListener;
    }

    public void getAllLinhaspedidoAPI(final Context context,int id) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
            if (linhapedidoListener!=null)
            {
                linhapedidoListener.onRefreshListaLinhapedidos(gersoftBD.getAllLinhapedidosBD());
            }
        }else
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            JsonArrayRequest req=new JsonArrayRequest(Request.Method.GET, mUrlAPILinhaPedidoAll+
                    "/"+id+"?access-token="+token_logado, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    linhapedidos = GersoftJsonParser.parserJsonLinhaPedidos(response);
                    adicionarLinhapedidosBD(linhapedidos,context);

                    if (linhapedidoListener!=null)
                    {
                        linhapedidoListener.onRefreshListaLinhapedidos(linhapedidos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    private void adicionarLinhapedidosBD(ArrayList<Linhapedido> linhapedidos,Context context) {
        gersoftBD.removerAllLinhapedidos();
        for(Linhapedido l : linhapedidos)
        {
            adicionarLinhapedidoBD(l,context);
        }
    }

    public void adicionarLinhapedidoBD(Linhapedido l,Context context)
    {
        gersoftBD.adicionarLinhapedidoDB(l,context);
    }

    public ArrayList getMesasDB() {
        mesas=gersoftBD.getAllMesasDB();// return da copia das mesas
        return new ArrayList(mesas);
    }

    public ArrayList getLinhaspedidosDB() {
        linhapedidos=gersoftBD.getAllLinhapedidosBD();// return da copia das linhas de pedido
        return new ArrayList(linhapedidos);
    }

    public void adicionarLinhaPedidoAPI(Linhapedido linhapedido, Context context, String token) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.POST, mUrlAPILinhaPedidoEditAdd+"?access-token="+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    adicionarLinhapedidoBD(GersoftJsonParser.parserJsonLinhaPedido(response),context);
                    if (DetalhesListener!=null)
                    {
                        DetalhesListener.onRefreshDetalhes(MenuMainActivity.ADD);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("quantidade",linhapedido.getQuantidade()+"");
                    params.put("valorunitario", linhapedido.getValorunitario()+"");
                    params.put("valoriva",linhapedido.getValoriva()+"");
                    params.put("taxaiva",linhapedido.getTaxaiva()+"");
                    params.put("pedido_id",linhapedido.getPedido_id()+"");
                    params.put("artigo_id",linhapedido.getArtigo_id()+"");
                    return params;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void removerLinhaPedidoAPI(Linhapedido linhapedido, Context context) {
        if (!GersoftJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            SharedPreferences sharedPreferences= context.getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
            String user_logado=sharedPreferences.getString("USERNAME","");
            String token_logado=sharedPreferences.getString("TOKEN","");
            StringRequest req = new StringRequest(Request.Method.DELETE, mUrlAPILinhaPedidoEditAdd+ "/"+linhapedido.getId()
                    +"?access-token="+token_logado
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    removerLinhaPedidoDB(linhapedido.getId());
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

    private void removerLinhaPedidoDB(int id) {
        Linhapedido auxLinhapedido = getLinhapedido(id);
        if (auxLinhapedido!=null)
            gersoftBD.removerLinhapedidoDB(id);
    }

    public Linhapedido getLinhapedido(int id) {
        for (Linhapedido p : linhapedidos){
            if(p.getId() == id)
                return p;
        }
        return null;
    }
    //endregion
}
