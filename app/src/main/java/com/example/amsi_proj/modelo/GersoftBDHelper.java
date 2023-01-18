package com.example.amsi_proj.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GersoftBDHelper extends SQLiteOpenHelper {

    //Nome da base de dados
    private static final String DB_NAME="gersoft";
    //versão da base de dados
    private static final int DB_VERSION=1;
    //iniciar base de dados
    private final SQLiteDatabase db;

    //criar tabelas da base de dados
    private static final String TABLE_ARTIGO="artigo",TABLE_CATEGORIA="categoria",
    TABLE_COMENTARIO="comentario",TABLE_LINHAPEDIDO="linhapedido",TABLE_MESA="mesa",
    TABLE_METODOPAGAMENTO="metodopagamento",TABLE_PEDIDO="pedido",TABLE_RESERVA="reserva",TABLE_IVA="iva";

    public GersoftBDHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
        db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
