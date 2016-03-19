package com.example.admin.uber_tcc_sqlite.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.uber_tcc_sqlite.R;
import com.example.admin.uber_tcc_sqlite.banco.Constantes;
import com.example.admin.uber_tcc_sqlite.dao.UsuarioDao;
import com.example.admin.uber_tcc_sqlite.model.Usuario;

/**
 * Created by Admin on 02/03/2016.
 */
public class TelaUsuario extends AppCompatActivity {
    private Context context;

    private EditText et_nome;
    private EditText et_usuario;
    private EditText et_macadress;

    private int codigoAtual;

    private UsuarioDao usuarioDao;

    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telausuario);

        findCampos();
        carregarTela();
    }

    private void carregarTela() {
        if (codigoAtual != -1) {
            Usuario cAux = usuarioDao.obterContatoById(codigoAtual);

            et_nome.setText(cAux.getNome());
            et_usuario.setText(cAux.getUsuario());
            et_macadress.setText(String.valueOf(cAux.getMacadress()));
        } else {
            et_nome.setText("");
            et_usuario.setText("");
            et_macadress.setText("");
        }
    }

    private void inserirAtualizar() {
        Usuario cAux = new Usuario();

        cAux.setNome(et_nome.getText().toString().trim());
        cAux.setUsuario(et_usuario.getText().toString().trim());
        cAux.setMacadress(et_macadress.getText().toString().trim());

        if (codigoAtual != -1) {
            cAux.setIdcontato(codigoAtual);
            usuarioDao.atualizarContato(cAux);
            Toast.makeText(context, "Registro Atualizado!!!", Toast.LENGTH_SHORT).show();
        } else {
            codigoAtual = usuarioDao.proximoID();
            cAux.setIdcontato(codigoAtual);
            usuarioDao.inserirContato(cAux);

            Toast.makeText(context, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
        }

    }

    private void findCampos() {
        context = getBaseContext();

        usuarioDao = new UsuarioDao(context);

        codigoAtual = getIntent().getIntExtra(Constantes.CODIGO, 0);

        et_nome = (EditText) findViewById(R.id.usuario_et_nome);
        et_usuario = (EditText) findViewById(R.id.usuario_et_usuario);
        et_macadress = (EditText) findViewById(R.id.usuario_et_macadress);
    }

    private boolean validar() {
        String nome = et_nome.getText().toString().trim().trim();
        String usuraio = et_usuario.getText().toString().trim();
        String macadress = et_macadress.getText().toString().trim();

        if (nome.length() == 0) {
            msg = "Erro: Nome é Obrigatorio!!!";
            return false;
        }

        if (usuraio.length() == 0) {
            msg = "Erro: Usuário é Obrigatorio!!!";
            return false;
        }

        if (macadress.length() == 0) {
            msg = "Erro: MacAdress é Inválida!!!";
            return false;
        }

        return true;
    }


    private void chamarTelaLista() {
        Intent mIntent = new Intent(context, MainActivity.class);
        startActivity(mIntent);
        finish();
    }

    public void onBackPressed() {
        chamarTelaLista();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.menu_salvar:
                if (validar()) {
                    inserirAtualizar();
                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_voltar:
                chamarTelaLista();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
