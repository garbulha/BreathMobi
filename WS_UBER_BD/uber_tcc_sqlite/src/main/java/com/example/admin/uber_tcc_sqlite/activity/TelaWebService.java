package com.example.admin.uber_tcc_sqlite.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.admin.uber_tcc_sqlite.R;
import com.example.admin.uber_tcc_sqlite.model.EnviarJson;
import com.example.admin.uber_tcc_sqlite.model.HMAux;
import com.example.admin.uber_tcc_sqlite.model.Pessoa;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin on 14/03/2016.
 */
public class TelaWebService extends AppCompatActivity {

    private ListView lv_pessoa;
    private Context context;
    private ArrayList<HMAux> pessoa = new ArrayList<>();
    private Firebase firebase;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telawebservice);
        Firebase.setAndroidContext(this);

        lv_pessoa = (ListView) findViewById(R.id.telawebservice_lv_pessoa);
        context = getBaseContext();

        new AsynckMau().execute();
    }

    private class AsynckMau extends AsyncTask<Void, Integer, Void> {
        //Pode acessar a UI
        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(TelaWebService.this, "Por favor Aguarde ...", "Recuperando Informações do Servidor...");
        }

        //Não pode acessar a UI
        @Override
        protected Void doInBackground(Void... params) {
            try{
                firebase = new Firebase("https://smartbaf.firebaseio.com/");
            }catch (FirebaseException e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        String json = dataSnapshot.getValue().toString();
                        String formulario = "\"result\"" + ":";

                        json = json.replace("formulario=", formulario);

                        JSONObject objectResult = new JSONObject(json);
                        JSONObject jsonForm = objectResult.getJSONObject("result");
                        JSONArray jsonArray = jsonForm.getJSONArray("formulario");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            HMAux hmAux = new HMAux();
                            JSONObject object = jsonArray.getJSONObject(i);

                            hmAux.put(HMAux.TEXTO01, object.getString("nome"));
                            hmAux.put(HMAux.TEXTO02, object.getString("idade"));

                            pessoa.add(hmAux);
                        }
                        publishProgress();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(context, "Sem acesso a internet", Toast.LENGTH_SHORT).show();
                }

            });

            return null;
        }

        //Pode acessar a UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            String[] from = {HMAux.TEXTO01, HMAux.TEXTO02};
            int[] to = {R.id.celulawebservice_tv_nome, R.id.celulawebservice_tv_idade};
            lv_pessoa.setAdapter(
                    new SimpleAdapter(
                            context,
                            pessoa,
                            R.layout.celulawebservice,
                            from,
                            to)
            );

            load.dismiss();
        }

        //Pode acessar a UI
        @Override
        protected void onPostExecute(Void aVoid) {
            firebase.onDisconnect();
        }
    }


    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(context, MainActivity.class);
        startActivity(mIntent);
        finish();
    }
}
