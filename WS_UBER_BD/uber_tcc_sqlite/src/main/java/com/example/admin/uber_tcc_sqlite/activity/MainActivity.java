package com.example.admin.uber_tcc_sqlite.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.example.admin.uber_tcc_sqlite.dao.UsuarioDao;
import com.example.admin.uber_tcc_sqlite.model.HMAux;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private ListView lv_usuario;

    private float LAT;
    private float LON;

    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telalista);

        findCampos();
        carregarLista();
        acoesTela();
    }

    private void acoesTela() {
        lv_usuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                chamarTelaUsuario(Integer.parseInt(item.get(HMAux.ID)));
            }
        });

        lv_usuario.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                HMAux item = (HMAux) parent.getItemAtPosition(position);
                final int pID;
                pID = Integer.parseInt(item.get(HMAux.ID));

                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Excluir");
                alerta.setTitle("Deseja excluir o registro?");
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usuarioDao.apagarContato(pID);
                        Toast.makeText(context, "Regitro apagado com sucesso!!!", Toast.LENGTH_SHORT).show();
                        carregarLista();
                    }
                });
                alerta.setNegativeButton("Não", null);
                alerta.show();

                return true;
            }
        });
    }

    private void chamarTelaUsuario(int id) {
        Intent mIntent = new Intent(context, TelaUsuario.class);
        mIntent.putExtra(Constantes.CODIGO, id);
        startActivity(mIntent);
        finish();
    }

    private void carregarLista() {
        String[] from = {HMAux.TEXTO01, HMAux.TEXTO02};
        int[] to = {R.id.celula_tv_nome, R.id.celula_tv_usuario};
        lv_usuario.setAdapter(
                new SimpleAdapter(
                        context,
                        usuarioDao.obterListausuario(),
                        R.layout.celula,
                        from,
                        to)
        );
    }

    private void findCampos() {
        context = getBaseContext();

        usuarioDao = new UsuarioDao(context);

        lv_usuario = (ListView) findViewById(R.id.telalista_lv_usuario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_incluir, menu);
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
            case R.id.menu_incluir:
                chamarTelaUsuario(-1);
                break;
            case R.id.menu_uber:
                LAT = -23.5384581069176f;
                LON = -46.43885709999995f;
                chamarTelaUber();
                break;
            case R.id.menu_webservice:
                chamarTelaWebService();
                break;
            default:
                Log.d("Default_menu", "Erro no menu");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void chamarTelaWebService() {
        Intent mIntent = new Intent(context, TelaWebService.class);
        startActivity(mIntent);
        finish();
    }

    private void chamarTelaUber() {
        Intent mIntent = new Intent(context, ApiUber.class);
        mIntent.putExtra(Constantes.LATITUDE, LAT);
        mIntent.putExtra(Constantes.LONGITUDE, LON);
        startActivity(mIntent);
        finish();
    }

}
