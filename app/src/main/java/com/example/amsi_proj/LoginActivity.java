package com.example.amsi_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amsi_proj.listeners.LoginListener;
import com.example.amsi_proj.modelo.SingletonGersoft;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    // Declarar variaveis
    private EditText etUsername, etPassword;
    private final int MIN_PASS = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SingletonGersoft.getInstance(getApplicationContext()).setLoginListener(this);

        // Atribuir as editText ás variaveis para poder acessar
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
        String user_logado=sharedPreferences.getString("USERNAME","");
        String token_logado=sharedPreferences.getString("TOKEN","");
        Boolean isloggedin=sharedPreferences.getBoolean("ISLOGGEDIN",false);
        int perfil_logado=sharedPreferences.getInt("PROFILE_ID",0);
        if(isloggedin==true){
            Intent intent=new Intent(getApplicationContext(),MenuMainActivity.class);
            startActivity(intent);
        }

    }

    // validar o email e o login e apresentar o resultado da validação num Toast.
    public void onClickLogin(View view) {

        // Ir buscar o email e password introduzidos
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        SingletonGersoft.getInstance(getApplicationContext()).loginAPI(username,password,getApplicationContext());

    }


    // Verifica se o email introduzido é válido
    private boolean isEmailValid(String email)
    {
        if(email == null)
            return false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches(); // verificar se condiz com o padrao standart, abc@abc (true)
    }

    // Verifica se a password introduzida é válida
    private boolean isPasswordValid(String pass)
    {
        if(pass==null)
            return false;
        return pass.length()>=MIN_PASS; // verifica se tem no minimo 4 caracteres (true)
    }


    @Override
    public void onValidateLogin(String token,String username,int profile_id) {
        if (token!=null)
        {
            Intent intent = new Intent(this, MenuMainActivity.class);
            intent.putExtra(MenuMainActivity.TOKEN, token);
            intent.putExtra(MenuMainActivity.USERNAME,username);
            intent.putExtra(MenuMainActivity.PROFILE_ID,profile_id);
            startActivity(intent);
            finish();
        }else
        {
            Toast.makeText(getApplicationContext(), R.string.ErroLogin,Toast.LENGTH_LONG).show();
        }
    }

}