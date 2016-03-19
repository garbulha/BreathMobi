package com.example.admin.uber_tcc_sqlite.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Admin on 29/02/2016.
 */
public class Dao {
    protected SQLiteDatabase db;
    private Context context;

    public Dao(Context context) {
        this.context = context;
    }

    protected void abrirBanco() {
        try {
            SQLiteHelper var_SqliteHelper = new SQLiteHelper(
                    context,
                    Constantes.DATBASE,
                    null,
                    Constantes.VERSAO
            );
            this.db = var_SqliteHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.d("ABRIR_BANCO", "Erro: " + e.getMessage());

        }
    }

    protected void fecharBanco() {
        if (this.db != null) {
            this.db.close();
        }
    }
}
