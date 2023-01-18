package com.example.amsi_proj.modelo;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.listeners.LoginListener;
import com.example.amsi_proj.utils.LoginJsonParser;

public class SingletonGersoft {

    private static  SingletonGersoft instance = null;
    private static RequestQueue volleyQueue = null;
    private static final  String mUrlAPILogin = "http://10.0.2.2/gersoft/backend/web/api/users/auth";
    private static final String TOKEN = "SwqsYsSzen1wSXRZYb_-P1pPEbbFXcey";
    private DetalhesListener DetalhesListener;
    private LoginListener loginListener;

    public SingletonGersoft(Context context) {
    }

    public static synchronized SingletonGersoft getInstance(Context context){
        if(instance == null)
        {
            instance = new SingletonGersoft(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    //region Login
    public void loginAPI(final String username, final String password, final Context context){
        if (!LoginJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação á internet", Toast.LENGTH_LONG).show();
        }else
        {
            StringRequest req = new StringRequest(Request.Method.GET, mUrlAPILogin, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String token = LoginJsonParser.parserJsonLogin(response);
                    if (loginListener != null)
                        loginListener.onValidateLogin(token);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("LOGIN: Error" + error.getMessage());
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
}
