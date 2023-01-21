package com.example.amsi_proj.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class GersoftBDHelper extends SQLiteOpenHelper {

    //region variaveis de sistema
    //Nome da base de dados
    private static final String DB_NAME="gersoft";
    //versão da base de dados
    private static final int DB_VERSION=2;
    //iniciar base de dados
    private final SQLiteDatabase db;

    //criar tabelas da base de dados
    private static final String TABLE_ARTIGO="artigo",TABLE_CATEGORIA="categoria",
    TABLE_COMENTARIO="comentario",TABLE_LINHAPEDIDO="linhapedido",TABLE_MESA="mesa",
    TABLE_METODOPAGAMENTO="metodopagamento",TABLE_PEDIDO="pedido",TABLE_RESERVA="reserva",TABLE_IVA="iva";

    //variaveis db comuns
    private static final String ID="id",NOME="nome", DESCRICAO="descricao",ESTADO="estado"
            ,PROFILE_ID="profile_id",TAXAIVA="taxaiva";
    //tabela artigo
    private static final String  REFERENCIA="referencia",PRECO="preco",IMAGEMURL="imagemurl"
            ,IVA_ID="iva_id",CATEGORIA_ID="categoria_id";
    //tabela comentario
    private static final String TITULO="titulo";
    //tabela linhapedido
    private static final String QUANTIDADE="quantidade",VALORUNITARIO="valorunitario"
            ,VALORIVA="valoriva",PEDIDO_ID="pedido_id",ARTIGO_ID="artigo_id";
    //tabela mesa
    private static final String NRMESA="nrmesa",NRLUGARES="nrlugares",TIPOMESA="tipomesa";
    //tabela metodopagamento
    private static final String NOMEPAGAMENTO="nomepagamento";
    //tabela pedido
    private static final String DATA="data",TOTAL="total",
            TIPO_PEDIDO="tipo_pedido",METODO_PAGAMENTO_ID="metodo_pagamento_id",MESA_ID="mesa_id";
    //tabela reserva
    private static final String HORA="hora",NRPESSOAS="nrpessoas";
    //endregion


    public GersoftBDHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
        db=getWritableDatabase();
    }
    //region criar base de dados
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreateTableArtigo="CREATE TABLE "+TABLE_ARTIGO+"("+
                ID + " INTEGER PRIMARY KEY, "+
                NOME + " TEXT NOT NULL, "+
                DESCRICAO + " TEXT NOT NULL, "+
                REFERENCIA + " TEXT NOT NULL,"+
                PRECO + " DOUBLE NOT NULL, "+
                IMAGEMURL+" TEXT, "+
                IVA_ID+" INTEGER NOT NULL,"+
                CATEGORIA_ID+" INTEGER NOT NULL );";

        String sqlCreateTableCategoria="CREATE TABLE "+TABLE_CATEGORIA+"("+
                ID+" INTEGER PRIMARY KEY, "+
                NOME+" TEXT NOT NULL, "+
                DESCRICAO+" TEXT NOT NULL);";

        String sqlCreateTableComentario="CREATE TABLE "+TABLE_COMENTARIO+"("+
                ID+" INTEGER PRIMARY KEY, "+
                TITULO+" TEXT NOT NULL, "+
                DESCRICAO+" TEXT NOT NULL, "+
                PROFILE_ID+"INTEGER NOT NULL);";

        String sqlCreateTableLinhapedido="CREATE TABLE "+TABLE_LINHAPEDIDO+"("+
                ID+" INTEGER PRIMARY KEY, "+
                QUANTIDADE+" INTEGER NOT NULL, "+
                VALORUNITARIO+" DOUBLE NOT NULL, "+
                VALORIVA+" DOUBLE NOT NULL, "+
                TAXAIVA+" INTEGER NOT NULL, "+
                PEDIDO_ID+" INTEGER NOT NULL, "+
                ARTIGO_ID+" INTEGER NOT NULL);";

        String sqlCreateTableMesa="CREATE TABLE "+TABLE_MESA+"("+
                ID+" INTEGER PRIMARY KEY, "+
                NRMESA+" INTEGER NOT NULL, "+
                NRLUGARES+" INTEGER NOT NULL, "+
                TIPOMESA+"TEXT NOT NULL);";

        String sqlCreateTableMetodopagamento="CREATE TABLE "+TABLE_METODOPAGAMENTO+"("+
                ID+" INTEGER PRIMARY KEY, "+
                NOMEPAGAMENTO+" TEXT NOT NULL);";

        String sqlCreateTablePedido="CREATE TABLE "+TABLE_PEDIDO+"("+
                ID+" INTEGER PRIMARY KEY, "+
                DATA+" TEXT NOT NULL, "+
                TOTAL+" DOUBLE NOT NULL, "+
                TIPO_PEDIDO+" INTEGER NOT NULL, "+
                ESTADO+" TEXT NOT NULL, "+
                PROFILE_ID+" INTEGER NOT NULL, "+
                METODO_PAGAMENTO_ID+"INTEGER, "+
                MESA_ID+"INTEGER);";

        String sqlCreateTableReserva="CREATE TABLE "+TABLE_RESERVA+"("+
                ID+" INTEGER PRIMARY KEY, "+
                DATA+" TEXT NOT NULL, "+
                HORA+" TEXT NOT NULL, "+
                NRPESSOAS+" INTEGER NOT NULL, "+
                ESTADO+" INTEGER NOT NULL, "+
                PROFILE_ID+" INTEGER NOT NULL);";

        String sqlCreateTableIva="CREATE TABLE "+TABLE_IVA+"("+
                ID+" INTEGER PRIMARY KEY, "+
                DESCRICAO+" TEXT NOT NULL, "+
                TAXAIVA+" INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(sqlCreateTableArtigo);
        sqLiteDatabase.execSQL(sqlCreateTableCategoria);
        sqLiteDatabase.execSQL(sqlCreateTableComentario);
        sqLiteDatabase.execSQL(sqlCreateTableLinhapedido);
        sqLiteDatabase.execSQL(sqlCreateTableMesa);
        sqLiteDatabase.execSQL(sqlCreateTableMetodopagamento);
        sqLiteDatabase.execSQL(sqlCreateTablePedido);
        sqLiteDatabase.execSQL(sqlCreateTableReserva);
        sqLiteDatabase.execSQL(sqlCreateTableIva);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sqlDeleteTableArtigo="DROP TABLE IF EXISTS "+TABLE_ARTIGO;
        String sqlDeleteTableCategoria="DROP TABLE IF EXISTS "+TABLE_CATEGORIA;
        String sqlDeleteTableComentario="DROP TABLE IF EXISTS "+TABLE_COMENTARIO;
        String sqlDeleteTableLinhapedido="DROP TABLE IF EXISTS "+TABLE_LINHAPEDIDO;
        String sqlDeleteTableMesa="DROP TABLE IF EXISTS "+TABLE_MESA;
        String sqlDeleteTableMetodopagamento="DROP TABLE IF EXISTS "+TABLE_METODOPAGAMENTO;
        String sqlDeleteTablePedido="DROP TABLE IF EXISTS "+TABLE_PEDIDO;
        String sqlDeleteTableReserva="DROP TABLE IF EXISTS "+TABLE_RESERVA;
        String sqlDeleteTableIva="DROP TABLE IF EXISTS "+TABLE_IVA;

        sqLiteDatabase.execSQL(sqlDeleteTableArtigo);
        sqLiteDatabase.execSQL(sqlDeleteTableCategoria);
        sqLiteDatabase.execSQL(sqlDeleteTableComentario);
        sqLiteDatabase.execSQL(sqlDeleteTableLinhapedido);
        sqLiteDatabase.execSQL(sqlDeleteTableMesa);
        sqLiteDatabase.execSQL(sqlDeleteTableMetodopagamento);
        sqLiteDatabase.execSQL(sqlDeleteTablePedido);
        sqLiteDatabase.execSQL(sqlDeleteTableReserva);
        sqLiteDatabase.execSQL(sqlDeleteTableIva);


        onCreate(sqLiteDatabase);
    }
    //endregion

    //region CRUD Artigo
    public Artigo adicionarArtigoBD(Artigo a)
    {
        ContentValues values = new ContentValues();
        values.put(ID, a.getId());
        values.put(NOME, a.getNome());
        values.put(DESCRICAO, a.getDescricao());
        values.put(REFERENCIA, a.getReferencia());
        values.put(PRECO, a.getPreco());
        values.put(IMAGEMURL, a.getImagemurl());
        values.put(IVA_ID,a.getIva_id());
        values.put(CATEGORIA_ID,a.getCategoria_id());
        // db.insert retorna -1 em caso de erro ou o id que foi criado
        int id = (int)db.insert(TABLE_ARTIGO, null, values);
        if(id>-1)
        {
            a.setId(id);
            return a;
        }
        return null;
    }

    public void removerAllArtigos()
    {
        db.delete(TABLE_ARTIGO, null, null);
    }

    public ArrayList<Artigo> getAllArtigosBD(){
        ArrayList<Artigo> artigos=new ArrayList<>();
        Cursor cursor=db.query(TABLE_ARTIGO,new String[]{ID,NOME,DESCRICAO,REFERENCIA,PRECO,IMAGEMURL,IVA_ID,CATEGORIA_ID},
                null,null,null,null,null);

        if(cursor.moveToFirst()){
            do {
            Artigo auxArtigo = new Artigo(cursor.getInt(0), cursor.getString(1)
                    , cursor.getString(2), cursor.getString(3),
                    cursor.getDouble(4), cursor.getString(5),cursor.getInt(6),cursor.getInt(7));

            artigos.add(auxArtigo);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return artigos;
    }

    public ArrayList<Comentario> getAllComentariosBD() {
        ArrayList<Comentario> comentarios=new ArrayList<>();
        Cursor cursor=db.query(TABLE_COMENTARIO,new String[]{ID,TITULO,DESCRICAO,PROFILE_ID},
                null,null,null,null,null);

        if(cursor.moveToFirst()){
            do {
                Comentario auxComentario = new Comentario(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3)
                );
                
                comentarios.add(auxComentario);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return comentarios;

    }

    public void removerAllCometarios() {
        db.delete(TABLE_COMENTARIO, null, null);
    }



    public Boolean editarComentarioBD(Comentario comentario) {

        ContentValues values = new ContentValues();
        values.put(TITULO, comentario.getTitulo());
        values.put(DESCRICAO, comentario.getDescricao());
        // db.update retorna o numero de linhas atualizadas
        return db.update(TABLE_COMENTARIO, values, ID+"=?", new String[]{comentario.getId()+""})==1;

    }
    public Boolean removerLivroBD(int id)
    {
        // db.delete
        return db.delete(TABLE_COMENTARIO,ID+"=?", new String[]{id+""})==1;
    }

    public Comentario adicionarComentarioBD(Comentario comentario)
    {
        ContentValues values = new ContentValues();
        values.put(ID, comentario.getId());
        values.put(TITULO, comentario.getTitulo());
        values.put(DESCRICAO, comentario.getDescricao());
        int id = (int)db.insert(TABLE_COMENTARIO, null, values);
        if(id>-1)
        {
            comentario.setId(id);
            return comentario;
        }
        return null;
    }
    //endregion

    //region Reserva
    public ArrayList<Reserva> getAllReservasBD(){
        ArrayList<Reserva> reservas=new ArrayList<>();
        Cursor cursor=db.query(TABLE_RESERVA,new String[]{ID,NRPESSOAS,ESTADO,PROFILE_ID,DATA,HORA},
                null,null,null,null,null);

        if(cursor.moveToFirst()){
            do {
                Reserva auxReserva = new Reserva(cursor.getInt(0), cursor.getInt(1)
                        , cursor.getInt(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getString(5));
                reservas.add(auxReserva);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return reservas;
    }

    public Reserva adicionarReservaBD(Reserva r)
    {
        ContentValues values = new ContentValues();
        values.put(ID, r.getId());
        values.put(NRPESSOAS, r.getNrpessoas());
        values.put(ESTADO, r.getEstado());
        values.put(PROFILE_ID, r.getProfile_id());
        values.put(DATA, r.getData());
        values.put(HORA, r.getHora());
        // db.insert retorna -1 em caso de erro ou o id que foi criado
        int id = (int)db.insert(TABLE_RESERVA, null, values);
        if(id>-1)
        {
            r.setId(id);
            return r;
        }
        return null;
    }

    public void removerAllReservas()
    {
        db.delete(TABLE_RESERVA, null, null);
    }

    public Boolean editarReservaBD(Reserva reserva)
    {
        ContentValues values = new ContentValues();
        values.put(NRPESSOAS, reserva.getNrpessoas());
        values.put(DATA,reserva.getData());
        values.put(HORA,reserva.getHora());
        // db.update retorna o numero de linhas atualizadas
        return db.update(TABLE_RESERVA, values, ID+"=?", new String[]{reserva.getId()+""})==1;
    }

    //end region
}
