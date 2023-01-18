package com.example.amsi_proj.listeners;

import android.content.Context;

public interface LoginListener {
    void onValidateLogin(String token, String username);
}
