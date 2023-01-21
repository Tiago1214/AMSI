package com.example.amsi_proj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amsi_proj.listeners.DetalhesListener;
import com.example.amsi_proj.modelo.Reserva;
import com.example.amsi_proj.modelo.SingletonGersoft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
        InputFilter timeFilter;


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
        //endregion

        //region validaredittexthora

        etHora.addTextChangedListener(new TextWatcher() {
            String beforeTXT;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("before TEXT TEXXT", " this : "+s+" and "+start+" and "+count+"and "+after);
                beforeTXT= ""+s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int input ;

                //first determine whether user is at hrs side or min side
                if (s.toString().equals("")){
                    return;
                }
                if(s.toString().length()>2 && start<=2){ //means the user is at hour side
                    input = Integer.parseInt(s.toString().substring(0,1)) % 10;

                }
                else if(s.toString().length()>2 && start>=3) {//means that user is at min side
                    input = Integer.parseInt("0"+s.toString().substring(3))%10;

                }
                else if(s.toString().indexOf(":")==1){ // if we have for eg 1: or 0: then we take first character for parsing
                    input = Integer.parseInt(s.toString().charAt(0)+"");
                }
                else{ //else it is default where the user is at first position
                    input = Integer.parseInt(s.toString()) % 10;
                }

                //Special case where 00: is autommatically converted to 12: in 12hr time format
                if(s.toString().contains("00:")){
                    Log.i("INsisde )))","i am called ");
                    etHora.setText("24:");
                    return;
                }

                //Now we manipulate the input and its formattin and cursor movement
                if(input<=1 && start ==0){ //thiis is for first input value to check .... time shouldnt exceed 12 hr
                    //do nothing
                }
                else if (input>2 && start==0){ //if at hour >1 is press then automaticc set the time as 02: or 05: etc
                    etHora.setText("0"+s+":");
                }
                else if(input>3 && start==1 && !s.toString().startsWith("0")){ //whe dont have greater than 12 hrs so second postionn shouldn't exceed value 2
                    etHora.setText(beforeTXT);
                }
                else if(start==1 && !beforeTXT.contains(":")){  //if valid input 10 or 11 or 12 is given then convert it to 10: 11: or 12:
                    etHora.setText(s.toString()+":");

                    if(s.toString().length()==1 && s.toString().startsWith("0")){
                        etHora.setText("");
                    }
                    if(s.toString().startsWith("1")&& s.toString().length()==1){ //on back space convert 1: to 01:
                        etHora.setText("0"+etHora.getText().toString());
                    }

                }
                else if(start == 3 && input >5 ){ //min fig shouldn't exceed 59 so ...if at first digit of min input >5 then do nothing or codpy the earlier text
                    etHora.setText(beforeTXT);
                }
                else if (start>4 && s.toString().length()>5){ // the total string lenght shouldn't excced 5
                    etHora.setText(beforeTXT);
                }
                else if(start<2 && beforeTXT.length()>2){
                    etHora.setText(beforeTXT);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i("after  TEXT TEXXT", " this : "+s);
                etHora.setSelection(etHora.getText().toString().length());

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
                    " e visualizar reservas Concluídas",Toast.LENGTH_LONG).show();
            Intent intent;
            intent = new Intent(this, MenuMainActivity.class);
            startActivity(intent);
        }
        //verificar se a reserva se encontra cancelada
        if(reserva.getEstado()==2){
            Toast.makeText(this.getApplicationContext(),"Não se pode visualizar" +
                    " reservas cancelas",Toast.LENGTH_LONG).show();
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

    //region CancelarReserva
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemRemover:
                //dialogRemover();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void dialogRemover() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancelar Reserva")
                .setMessage("Tem a certeza que pretende cancelar a reserva?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SingletonGersoft.getInstance(getApplicationContext()).cancelarReservaAPI(reserva, getApplicationContext());

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nao fazer nada
                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(reserva!=null){
            getMenuInflater().inflate(R.menu.menu_detalhes_cancelar,menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }
    //endregion

    //region validar reserva
    private boolean isReservaValida() {
        int nrpessoas = Integer.parseInt(etnrPessoas.getText().toString());
        String data=etData.getText().toString();
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate inicio = LocalDate.parse(data, parser);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String datetime = dateformat.format(c.getTime());
        LocalDate fim = LocalDate.parse(datetime, parser);
        String hora=etHora.getText().toString();

        if (nrpessoas<0){
            etnrPessoas.setText("Erro");
            return false;
        }
        if(inicio.isBefore(fim)){
            etData.setText("Insira uma data superior á data de hoje");
            return false;
        }
        if(data.length()<9){
            etData.setText("A data é um campo obrigatório");
            return false;
        }
        if(hora.length()<5){
            etHora.setText("A hora é um campo obrigatório");
            return false;
        }
        return true;
    }
    //endregion

    //region
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
}