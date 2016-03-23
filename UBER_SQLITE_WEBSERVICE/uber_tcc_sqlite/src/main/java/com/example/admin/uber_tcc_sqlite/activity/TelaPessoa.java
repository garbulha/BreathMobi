package com.example.admin.uber_tcc_sqlite.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.uber_tcc_sqlite.R;
import com.example.admin.uber_tcc_sqlite.banco.Constantes;
import com.example.admin.uber_tcc_sqlite.dao.UsuarioDao;
import com.example.admin.uber_tcc_sqlite.model.HMAux;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Admin on 22/03/2016.
 */
public class TelaPessoa extends AppCompatActivity {
    private Context context;

    private EditText et_nome;
    private EditText et_idade;

    private String codigoAtual;
    private Firebase firebase;

    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telapessoa);

        findCampos();
        if (codigoAtual.equals("0")){
            limparCampos();
        }else{
            carregarTela();
        }
    }

    private void limparCampos() {
        et_nome.setText("");
        et_idade.setText("");
    }

    private void carregarTela() {
        try {
            firebase = new Firebase("https://breathmobi.firebaseio.com/");
        } catch (FirebaseException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.child("usuario").child(codigoAtual).getValue();

                et_nome.setText(data.get("nome").toString());
                et_idade.setText(data.get("idade").toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void findCampos() { 
        context = getBaseContext();

        codigoAtual = getIntent().getStringExtra(Constantes.CODIGO);

        et_nome = (EditText) findViewById(R.id.pessoa_et_nome);
        et_idade = (EditText) findViewById(R.id.pessoa_et_idade);
    }

    public void onBackPressed() {
        chamarTelaWebService();
    }

    private void chamarTelaWebService() {
        Intent mIntent = new Intent(context, TelaWebService.class);
        startActivity(mIntent);
        finish();
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
                chamarTelaWebService();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inserirAtualizar() {
        if (codigoAtual.equals("0")){
            try {
                firebase = new Firebase("https://breathmobi.firebaseio.com/");
            } catch (FirebaseException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Firebase refInserir = firebase.child("usuario");
            HashMap<String,Object > hashMap = new HashMap<>();
            hashMap.put("nome", et_nome.getText().toString());
            hashMap.put("idade", et_idade.getText().toString());

            refInserir.push().setValue(hashMap);

            Toast.makeText(context, "Registro salvo!!!", Toast.LENGTH_SHORT).show();
        }else{
            try {
                firebase = new Firebase("https://breathmobi.firebaseio.com/");
            } catch (FirebaseException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Firebase refUpdate = firebase.child("usuario").child(codigoAtual);
            HashMap<String,Object > hashMap = new HashMap<>();
            hashMap.put("nome", et_nome.getText().toString());
            hashMap.put("idade", et_idade.getText().toString());

            refUpdate.updateChildren(hashMap);
            Toast.makeText(context, "Registro alterado!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validar() {
        String nome = et_nome.getText().toString().trim().trim();
        String idade = et_idade.getText().toString().trim();


        if (nome.length() == 0) {
            msg = "Erro: Nome é Obrigatorio!!!";
            return false;
        }

        if (idade.length() == 0) {
            msg = "Erro: Idade é Obrigatorio!!!";
            return false;
        }

        return true;
    }
}
