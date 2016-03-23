package com.example.admin.uber_tcc_sqlite.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.admin.uber_tcc_sqlite.dao.UsuarioDao;
import com.example.admin.uber_tcc_sqlite.model.HMAux;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback{
    private static final int REQUEST_PERMISSIONS_CODE = 14;
    private Context context;
    private ListView lv_usuario;

    private UsuarioDao usuarioDao;

    private Firebase firebase;
    AlertDialog.Builder mMaterialDialog;
    private GoogleApiClient googleApiLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telalista);
        Firebase.setAndroidContext(this);

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
                pedirPermicao();
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

    private void pedirPermicao() {
        if( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){

            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.ACCESS_FINE_LOCATION ) ){
                callDialog("Ative o serviço de localização", new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            }
            else{
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );
            }
        }
        else{
            cnnGPS();
        }
    }

    private void chamarTelaWebService() {
        Intent mIntent = new Intent(context, TelaWebService.class);
        startActivity(mIntent);
        finish();
    }


    public synchronized void cnnGPS() {
        googleApiLocation = new GoogleApiClient.Builder(context)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        googleApiLocation.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location l = LocationServices.FusedLocationApi.getLastLocation(googleApiLocation);

        if (l != null) {
            Intent mIntent = new Intent(context, ApiUber.class);
            mIntent.putExtra(Constantes.LATITUDE, l.getLatitude());
            mIntent.putExtra(Constantes.LONGITUDE, l.getLongitude());
            startActivity(mIntent);
            finish();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Serviço de GPS");
            alertDialog.setMessage("Ative o serviço de localização");

            // Ativar GPS
            alertDialog.setPositiveButton("ATIVAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getApplicationContext().startActivity(intent);
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch( requestCode ){
            case REQUEST_PERMISSIONS_CODE:
                for( int i = 0; i < permissions.length; i++ ){

                    if( permissions[i].equalsIgnoreCase( Manifest.permission.ACCESS_FINE_LOCATION )
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){

                        cnnGPS();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callDialog( String message, final String[] permissions ){
        AlertDialog.Builder mMaterialDialog = new AlertDialog.Builder(this)
                .setTitle("Serviço de Localização")
                .setMessage(message)
                .setPositiveButton("ATIVAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
                    }
                });
        mMaterialDialog.show();
    }
}
