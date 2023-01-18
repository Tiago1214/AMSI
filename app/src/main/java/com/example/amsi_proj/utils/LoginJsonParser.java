package com.example.amsi_proj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginJsonParser {

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
}
