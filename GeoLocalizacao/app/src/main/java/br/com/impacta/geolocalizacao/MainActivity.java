package br.com.impacta.geolocalizacao;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static br.com.impacta.geolocalizacao.Constantes.LONGITUDE;

public class MainActivity extends AppCompatActivity {

    private Button btn_getLocation;
    private Context cx;
    //private GeoLocalizacao GPS;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // iniciar variaveis
        btn_getLocation = (Button) findViewById(R.id.btn_getLocation);
        cx = getBaseContext();
        //
        //GPS = new GeoLocalizacao(cx);

        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    Constantes.LATITUDE = String.valueOf(lat);
                    Constantes.LONGITUDE = String.valueOf(lon);

                }
                Toast.makeText(
                        cx,
                        "Latitude: " + Constantes.LATITUDE + " " + "Longitude: " + LONGITUDE,
                        Toast.LENGTH_SHORT).show();

            }
        });


    }



    private void habilitarGPS() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Serviço de Localização Desabilitado");
        alertDialog.setMessage("Ative o serviço de localização");

        // OK
        alertDialog.setPositiveButton("ATIVAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                cx.startActivity(intent);
            }
        });
        alertDialog.show();

    }


}
