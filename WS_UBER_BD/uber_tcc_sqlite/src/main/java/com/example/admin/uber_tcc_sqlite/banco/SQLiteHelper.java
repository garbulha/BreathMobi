package com.example.admin.uber_tcc_sqlite.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 29/02/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE if not exists[usuario] (\n" +
                    "  [idusuario] INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "  [nome] NVARCHAR(100), \n" +
                    "  [usuario] NVARCHAR(50), \n" +
                    "  [macadress] TEXT \n" +
                    "  );");

            String[] comandos = sb.toString().split(";");

            for (int i = 0; i < comandos.length; i++) {
                db.execSQL(comandos[i].toLowerCase());

            }
        } catch (Exception e) {
            Log.d("CRIAR_BANCO", "Erro: " + e.getMessage());

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS USUARIO");

        String[] comandos = sb.toString().split(";");

        for (int i = 0; i < comandos.length; i++) {
            db.execSQL(comandos[i].toLowerCase());

        }
        onCreate(db);
    }
}
