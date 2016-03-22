package com.example.admin.uber_tcc_sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.admin.uber_tcc_sqlite.banco.Dao;
import com.example.admin.uber_tcc_sqlite.model.HMAux;
import com.example.admin.uber_tcc_sqlite.model.Usuario;

import java.util.ArrayList;

/**
 * Created by Admin on 29/02/2016.
 */
public class UsuarioDao extends Dao {
    private static final String TABELA = "usuario";
    private static final String IDUSUARIO = "idusuario";
    private static final String NOME = "nome";
    private static final String USUARIO = "usuario";
    private static final String MACADRESS = "macadress";


    public UsuarioDao(Context context) {
        super(context);
    }

    public void inserirContato(Usuario contato) {
        abrirBanco();
        ContentValues cv = new ContentValues();

        cv.put(NOME, contato.getNome());
        cv.put(USUARIO, contato.getUsuario());
        cv.put(MACADRESS, contato.getMacadress());

        db.insert(TABELA, null, cv);

        fecharBanco();
    }

    public void atualizarContato(Usuario contato) {
        abrirBanco();
        ContentValues cv = new ContentValues();

        String[] arg = new String[]{String.valueOf(contato.getIdcontato())};
        String FILTRO = IDUSUARIO + " = ?";

        cv.put(NOME, contato.getNome());
        cv.put(USUARIO, contato.getUsuario());
        cv.put(MACADRESS, contato.getMacadress());

        db.update(TABELA, cv, FILTRO, arg);

        fecharBanco();

    }

    public void apagarContato(long idcontato) {
        abrirBanco();

        String[] arg = new String[]{String.valueOf(idcontato)};
        String FILTRO = IDUSUARIO + " = ?";

        db.delete(TABELA, FILTRO, arg);
        fecharBanco();

    }

    public Usuario obterContatoById(long idcontato) {
        Usuario cAux = null;

        abrirBanco();
        Cursor cursor = null;

        try {
            String[] arg = new String[]{String.valueOf(idcontato)};
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM usuario WHERE idusuario = ?");

            cursor = db.rawQuery(sb.toString(), arg);

            while (cursor.moveToNext()) {
                cAux = new Usuario();
                cAux.setIdcontato(cursor.getInt(cursor.getColumnIndex(IDUSUARIO)));
                cAux.setNome(cursor.getString(cursor.getColumnIndex(NOME)));
                cAux.setUsuario(cursor.getString(cursor.getColumnIndex(USUARIO)));
                cAux.setMacadress(cursor.getString(cursor.getColumnIndex(MACADRESS)));
            }

        } catch (Exception e) {
            Log.d("obterContatoById", "Erro: " + e.getMessage());

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        fecharBanco();

        return cAux;
    }

    public ArrayList<HMAux> obterListausuario() {
        ArrayList<HMAux> usuario = new ArrayList<>();

        abrirBanco();
        Cursor cursor = null;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT idusuario, nome, usuario FROM usuario ORDER BY nome");

            cursor = db.rawQuery(sb.toString(), null);

            while (cursor.moveToNext()) {
                HMAux hmAux = new HMAux();

                hmAux.put(HMAux.ID, String.valueOf(cursor.getInt(cursor.getColumnIndex(IDUSUARIO))));
                hmAux.put(HMAux.TEXTO01, cursor.getString(cursor.getColumnIndex(NOME)));
                hmAux.put(HMAux.TEXTO02, cursor.getString(cursor.getColumnIndex(USUARIO)));

                usuario.add(hmAux);
            }

        } catch (Exception e) {
            Log.d("obterListausuario", "Erro: " + e.getMessage());

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        fecharBanco();

        return usuario;
    }

    public int proximoID() {
        int proID = 0;

        abrirBanco();
        Cursor cursor = null;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT MAX(idusuario)+1 ID FROM usuario");

            cursor = db.rawQuery(sb.toString(), null);

            while (cursor.moveToNext()) {
                proID = cursor.getInt(cursor.getColumnIndex("ID"));

            }
            if (proID == 0) {
                proID = 1;
            }

        } catch (Exception e) {
            Log.d("proximoID", "Erro: " + e.getMessage());

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        fecharBanco();

        return proID;
    }
}
