package com.example.amsi_proj;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.modelo.Artigo;
import com.example.amsi_proj.modelo.Reserva;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Calendar;
import java.util.Locale;

public class DetalhesReservaActivity extends AppCompatActivity implements DetalhesListener {

    private Reserva reserva;
    private EditText etnrPessoas,etData,etHora;
    private String token;
    private FloatingActionButton fabGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //vista
        setContentView(R.layout.activity_detalhes_reserva);
        //obter token
        SharedPreferences sharedPreferences =getSharedPreferences(String.valueOf(R.string.SHARED_USER), Context.MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN", "");

        //id da reserva
        int id=getIntent().getIntExtra("ID_RESERVA", 0);
        reserva= SingletonGersoft.getInstance(this).getReserva(id);
        etnrPessoas=findViewById(R.id.etNrpessoas);
        etData=findViewById(R.id.etData);
        etHora=findViewById(R.id.etHora);
        fabGuardar=findViewById(R.id.fabGuardar);
        SingletonGersoft.getInstance(getApplicationContext()).setDetalhesListener(this);

        //region validaredittextdate
        etData.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<= Year.now().getValue())?Year.now().getValue():(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    etData.setText(current);
                    etData.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //end region

        //region validaredittexthora
        etHora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion

        if(reserva != null){
            carregarReserva();
        }

        //region verificar estado
        //verificar se a reserva se encontra concluída
        if(reserva.getEstado()==1){
            Toast.makeText(this.getApplicationContext(),"Não se pode alterar" +
                    "e visualizar reservas Concluídas",Toast.LENGTH_LONG).show();
            Intent intent;
            intent = new Intent(this, MenuMainActivity.class);
            startActivity(intent);
        }
        //verificar se a reserva se encontra cancelada
        if(reserva.getEstado()==2){
            Toast.makeText(this.getApplicationContext(),"Não se pode visualizar" +
                    "reservas cancelas",Toast.LENGTH_LONG).show();
            Intent intent;
            intent = new Intent(this, MenuMainActivity.class);
            startActivity(intent);
        }
        //endregion
        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReservaValida()) {
                    if (reserva != null && token!=null) {
                        reserva.setNrpessoas(Integer.parseInt(etnrPessoas.getText().toString()));
                        reserva.setData(etData.getText().toString());
                        reserva.setHora(etHora.getText().toString());
                        SingletonGersoft.getInstance(getApplicationContext()).editarReservaAPI(reserva, getApplicationContext(), token);
                        Intent intent;
                        intent = new Intent(view.getContext(), MenuMainActivity.class);
                        startActivity(intent);
                    } else {
                    }
                }

            }
        });
    }

    private boolean isReservaValida() {
        int nrpessoas = Integer.parseInt(etnrPessoas.getText().toString());

        if (nrpessoas<0){
            etnrPessoas.setText("Erro");
            return false;
        }
        return true;
    }

    @Override
    public void onRefreshDetalhes(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(MenuMainActivity.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void carregarReserva() {
        Resources res=getResources();
        setTitle("reserva");
        etnrPessoas.setText(reserva.getNrpessoas()+"");
        etData.setText(reserva.getData());
        etHora.setText(reserva.getHora());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(reserva!=null){
            getMenuInflater().inflate(R.menu.menu_pesquisa,menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }
}