package com.example.admin.uber_tcc_sqlite.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.admin.uber_tcc_sqlite.R;
import com.example.admin.uber_tcc_sqlite.banco.Constantes;
import com.example.admin.uber_tcc_sqlite.model.HMAux;
import com.example.admin.uber_tcc_sqlite.model.Pessoa;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Admin on 14/03/2016.
 */
public class TelaWebService extends AppCompatActivity {

    private ListView lv_pessoa;
    private Context context;
    private ProgressDialog load;

    private Firebase firebase;
    private ArrayList<HMAux> pessoa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telawebservice);
        Firebase.setAndroidContext(this);

        lv_pessoa = (ListView) findViewById(R.id.telawebservice_lv_pessoa);
        context = getBaseContext();

        new AsynckMau().execute();

        lv_pessoa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                chamarTelaPessoa(item.get(HMAux.ID));
            }
        });

        lv_pessoa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                final String pID;
                pID = item.get(HMAux.ID);

                AlertDialog.Builder alerta = new AlertDialog.Builder(TelaWebService.this);
                alerta.setTitle("Excluir");
                alerta.setTitle("Deseja excluir o registro?");
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            firebase = new Firebase("https://breathmobi.firebaseio.com/");
                        } catch (FirebaseException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        Firebase refDelete = firebase.child("usuario").child(pID);
                        refDelete.removeValue();
                        Toast.makeText(context, "Registro excluido", Toast.LENGTH_SHORT).show();
                    }
                });
                alerta.setNegativeButton("Não", null);
                alerta.show();

                return true;
            }
        });
    }

    private void chamarTelaPessoa(String codigo) {
        Intent mIntent = new Intent(context, TelaPessoa.class);
        mIntent.putExtra(Constantes.CODIGO, codigo);
        startActivity(mIntent);
        finish();
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
            try {
                firebase = new Firebase("https://breathmobi.firebaseio.com/");
            } catch (FirebaseException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            //Select no webService Firebase
            Query queryRef = firebase.orderByKey();
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    try {
                        HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                        Object[] id = users.keySet().toArray();
                        int i = 0;

                        for (Object user : users.values()) {
                            HashMap<String, Object> userMap = (HashMap<String, Object>) user;
                            HMAux hmAux = new HMAux();

                            hmAux.put(HMAux.ID, (String) id[i]);
                            hmAux.put(HMAux.TEXTO01, (String) userMap.get("nome"));
                            hmAux.put(HMAux.TEXTO02, (String) userMap.get("idade"));

                            pessoa.add(hmAux);
                            i++;
                        }
                        publishProgress();
                    } catch (Exception e) {
                        Log.d("queryRef", e.getMessage());
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Toast.makeText(context, "onChildRemoved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_firebase, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.menu_incluir_firebase:
                chamarTelaPessoa("0");
                break;
            default:
                Log.d("Default_menu", "Erro no menu");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
